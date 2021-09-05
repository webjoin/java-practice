package cn.iwuliao.ds.mapper.plugin;

import cn.iwuliao.ds.domain.AEntity;
import cn.iwuliao.ds.domain.DicEntity;
import cn.iwuliao.ds.ext.RecordRepository;
import cn.iwuliao.ds.util.SpringBeanUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
@Service("MyBatisPlugin")
public class MyBatisPlugin implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement ms = (MappedStatement)invocation.getArgs()[0];
        Object arg = invocation.getArgs()[1];
        Object proceed = invocation.proceed();
        doRecord(ms, arg, proceed);
        return proceed;
    }

    private void doRecord(MappedStatement ms, Object arg, Object proceed) {
        String tableName = getTableName(ms, arg);
        if (StringUtils.isEmpty(tableName)) {
            return;
        }
        if (SqlCommandType.INSERT != ms.getSqlCommandType() && SqlCommandType.UPDATE != ms.getSqlCommandType()
            && SqlCommandType.DELETE != ms.getSqlCommandType()) {
            return;
        }
        if (0 == (Integer)proceed) {
            return;
        }
        process(ms, arg, proceed);
    }

    private final static Map<String, List<DicEntity>> dic = new HashMap<>();

    static {
        List<DicEntity> aColumnNames = new ArrayList<>();
        aColumnNames
            .add(DicEntity.builder().entityName(AEntity.class.getName()).fieldName("id").primaryKey("Y").build());
        aColumnNames.add(DicEntity.builder().entityName(AEntity.class.getName()).fieldName("id_name").build());
        aColumnNames.add(DicEntity.builder().entityName(AEntity.class.getName()).fieldName("age").build());
        aColumnNames.add(DicEntity.builder().entityName(AEntity.class.getName()).fieldName("desc1").build());
        aColumnNames.add(DicEntity.builder().entityName(AEntity.class.getName()).fieldName("local_time").build());
        aColumnNames.add(DicEntity.builder().entityName(AEntity.class.getName()).fieldName("local_date").build());
        aColumnNames.add(DicEntity.builder().entityName(AEntity.class.getName()).fieldName("local_date_time").build());
        dic.put("a", aColumnNames);

        // List<String> aColumnNamesB = new ArrayList<>();
        // aColumnNamesB.add("uid");
        // dic.put("test01", aColumnNamesB);
    }

    private boolean containColumn(String tableName, Column column) {
        return dic.get(tableName).contains(DicEntity.builder().fieldName(column.getColumnName()).build());
    }

    private void process(MappedStatement ms, Object arg, Object proceed) {
        Statement statement = parseSql(ms, arg, "解析列名");
        if (statement == null) {
            return;
        }
        String tableName = getTableName(ms, arg);
        if (!dic.containsKey(tableName)) {
            return;
        }
        if (SqlCommandType.INSERT == ms.getSqlCommandType()) {
            Insert parse = (Insert)statement;
            String primaryKeyValue = getPrimaryKeyValueForEntity(arg, tableName);
            processInsert(parse, tableName, ms, arg, primaryKeyValue);
        } else if (SqlCommandType.UPDATE == ms.getSqlCommandType()) {
            Update parse = (Update)statement;
            String primaryKeyValue = getPrimaryKeyValueForEntity(arg, tableName);
            if (primaryKeyValue == null) {
                //
            }
            processUpdate(parse, tableName, ms, arg, primaryKeyValue);
        } else if (SqlCommandType.DELETE == ms.getSqlCommandType()) {
            Delete parse = (Delete)statement;
            Expression whereExpression = parse.getWhere();
            DicEntity dicEntity =
                dic.get(tableName).stream().filter(x -> "Y".equals(x.getPrimaryKey())).findFirst().orElse(null);
            Objects.requireNonNull(dicEntity);
            processDelete(tableName, getValue(whereExpression, dicEntity, ms, arg));
        }
    }

    private String getValue(Expression expression, DicEntity dicEntity, MappedStatement ms, Object arg) {
        Expression primaryKeyExpression = getPrimaryKeyExpression(expression, dicEntity.getFieldName());
        Objects.requireNonNull(primaryKeyExpression);
        if (primaryKeyExpression instanceof EqualsTo) {
            JdbcParameter rightExpression = (JdbcParameter)((EqualsTo)primaryKeyExpression).getRightExpression();
            Integer index = rightExpression.getIndex();
            return getValueByIndex(ms, arg, index - 1);
        } else if (primaryKeyExpression instanceof InExpression) {
            List<Expression> expressions =
                ((ExpressionList)((InExpression)primaryKeyExpression).getRightExpression()).getExpressions();
            StringBuilder sb = new StringBuilder();
            for (Expression jdbcExpression : expressions) {
                sb.append(",").append(getValueByIndex(ms, arg, ((JdbcParameter)jdbcExpression).getIndex() - 1));
            }
            return sb.deleteCharAt(0).toString();
        }

        return null;
    }

    private void processDelete(String tableName, String primaryKeyValue) {
        save(null, null, tableName, "D", primaryKeyValue);
    }

    private Expression getPrimaryKeyExpression(Expression wholeExpression, String fieldName) {
        if (wholeExpression instanceof EqualsTo) {
            EqualsTo where = (EqualsTo)wholeExpression;
            Expression leftExpression = where.getLeftExpression();
            if (leftExpression instanceof Column) {
                if (fieldName.equals(((Column)leftExpression).getColumnName())) {
                    return where;
                }
            }
        } else if (wholeExpression instanceof InExpression) {
            InExpression expression = (InExpression)wholeExpression;
            Expression leftExpression = expression.getLeftExpression();
            if (leftExpression instanceof Column) {
                if (fieldName.equals(((Column)leftExpression).getColumnName())) {
                    return expression;
                }
            }
        } else if (wholeExpression instanceof AndExpression) {
            AndExpression multiCondition = (AndExpression)wholeExpression;
            Expression leftExpression = multiCondition.getLeftExpression();
            Expression keyExpression = getPrimaryKeyExpression(leftExpression, fieldName);
            if (keyExpression != null) {
                return keyExpression;
            } else {
                Expression rightExpression = multiCondition.getRightExpression();
                return getPrimaryKeyExpression(rightExpression, fieldName);
            }
        }
        return null;
    }

    private String getPrimaryKeyValueForEntity(Object arg, String tableName) {
        String entityName = dic.get(tableName).get(0).getEntityName();
        Collection<?> values;
        if (!(arg instanceof MapperMethod.ParamMap)) {
            values = Collections.singletonList(arg);
        } else {
            values = ((MapperMethod.ParamMap<?>)arg).values();
        }
        // for entity
        for (Object value : values) {
            if (!value.getClass().getName().equals(entityName)) {
                continue;
            }
            Field[] declaredFields = value.getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if (!declaredField.isAnnotationPresent(Id.class)) {
                    continue;
                }
                declaredField.setAccessible(true);
                try {
                    return Objects.toString(declaredField.get(value), null);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("版本发布获取变更数据时获取主建值异常:", e);
                }
            }
        }
        //
        throw new RuntimeException(tableName + "版本发布获取变更数据时没有查找到主键");
    }

    private void processUpdate(Update parse, String tableName, MappedStatement ms, Object arg, String primaryKeyValue) {
        saveRecord(parse.getColumns(), parse.getExpressions(), tableName, ms, arg, "U", primaryKeyValue);
    }

    private void processInsert(Insert parse, String tableName, MappedStatement ms, Object arg, String primaryKeyValue) {
        saveRecord(parse.getColumns(), ((ExpressionList)parse.getItemsList()).getExpressions(), tableName, ms, arg, "C",
            primaryKeyValue);
    }

    //
    private void saveRecord(List<Column> columns, List<Expression> expressions, String tableName, MappedStatement ms,
        Object arg, String dataType, String primaryKeyValue) {
        for (int i = 0; i < columns.size(); i++) {
            if (containColumn(tableName, columns.get(i))) {
                Object value = getParameterValue(i, expressions, ms, arg);
                save(columns.get(i).getColumnName(), Objects.toString(value, null), tableName, dataType,
                    primaryKeyValue);
            }
        }
    }

    void save(String columnName, String value, String tableName, String dataType, String primaryKeyValue) {
        RecordRepository bean = SpringBeanUtil.getBean(RecordRepository.class);
        Objects.requireNonNull(bean);
        bean.save(tableName, columnName, value, primaryKeyValue, dataType);
        log.info("监听到了表：{},字段：{} ，值 {} 入库记录数据", tableName, columnName, value);
    }

    private String getTableName(MappedStatement ms, Object arg) {
        Statement statement = parseSql(ms, arg, "解析表名");
        List<String> tableList = new TablesNamesFinder().getTableList(statement);
        if (CollectionUtils.isEmpty(tableList)) {
            return null;
        }
        return tableList.get(0);
    }

    Statement parseSql(MappedStatement ms, Object arg, String message) {
        String sql = ms.getSqlSource().getBoundSql(arg).getSql();
        log.info("sql拦截器解析 sql：{}, 参数{}", sql, arg);
        try {
            // 解析表名
            return CCJSqlParserUtil.parse(sql);
        } catch (JSQLParserException e) {
            log.error("{}失败 解析sql：{}", message, sql);
        }
        return null;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        System.out.println(properties);
    }
    //

    private static String getParameterValue(int indx, List<Expression> expressions, MappedStatement ms, Object arg) {
        // if (itemsList instanceof ExpressionList) {
        // List<Expression> expressions = ((ExpressionList)itemsList).getExpressions();

        Expression expression = expressions.get(indx);
        if (expression instanceof JdbcParameter) {
            List<Expression> collect =
                expressions.stream().filter(x -> x instanceof JdbcParameter).collect(Collectors.toList());
            int index = collect.indexOf(expression);
            return getValueByIndex(ms, arg, index);
        } else {
            return getParameterValue(expression);
        }

        // if (metaObject.hasGetter(propertyName)) {
        // Object obj = metaObject.getValue(propertyName);
        // sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
        // } else if (boundSql.hasAdditionalParameter(propertyName)) {
        // Object obj = boundSql.getAdditionalParameter(propertyName);
        // sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
        // } else {
        // sql = sql.replaceFirst("\\?", "缺失");
        // } // 打印出缺失，提醒该参数缺失并防止错位
        // } else {
        // log.error("error {}", itemsList);
        // return null;
        // }
    }

    private static String getValueByIndex(MappedStatement ms, Object arg, int index) {
        BoundSql boundSql = ms.getSqlSource().getBoundSql(arg);
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        Configuration configuration = ms.getConfiguration();
        Object parameterObject = boundSql.getParameterObject();
        MetaObject metaObject = configuration.newMetaObject(parameterObject);
        ParameterMapping parameterMapping = parameterMappings.get(index);
        String propertyName = parameterMapping.getProperty();
        return Objects.toString(metaObject.getValue(propertyName), "null value");
    }

    private static String getParameterValue(Expression obj) {

        if (obj instanceof StringValue) {
            return ((StringValue)obj).getValue();
        }
        log.error("没有找到相应的类型 {}", obj);
        return null;

    }
}
