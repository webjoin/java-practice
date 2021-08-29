package cn.iwuliao.ds.mapper.plugin;

import cn.iwuliao.ds.mapper.dba.AMapper;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.apache.commons.lang3.RandomStringUtils;
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
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;

@Slf4j
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
@Service("MyBatisPlugin")
public class MyBatisPlugin implements Interceptor {

    @Autowired
    AMapper aMapper;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        Object arg = invocation.getArgs()[1];
        System.out.println(">>>" + ms);
        BoundSql boundSql = ms.getSqlSource().getBoundSql(arg);
        System.out.println(boundSql);
        doRecord(ms, arg);

        Object proceed = invocation.proceed();
        System.out.println("<<<" + ms);
        return proceed;
    }

    private void doRecord(MappedStatement ms, Object arg) {
        String tableName = getTableName(ms, arg);
        if (StringUtils.isEmpty(tableName)) {
            return;
        }
        List<Column> columnNames = getColumnNames(ms, arg);
        if (CollectionUtils.isEmpty(columnNames)) {
            return;
        }
        if ("a".equalsIgnoreCase(tableName)) {
            for (Column columnName : columnNames) {
                if (containColumn(columnName)) {
                    Object value = getValue(ms, arg);
//                    Object value = ((MapperMethod.ParamMap<?>) arg).get(columnName.getColumnName());
                    aMapper.insertTemplate(RandomStringUtils.randomAlphabetic(10), Objects.toString(value, ""));
                    log.info("监听到了表：{},字段：{} ，变更数据 {} 入库记录数据", tableName, columnName.getColumnName(), value);
                }

            }
        }
    }

    private String getValue(MappedStatement ms, Object arg) {
        Configuration configuration = ms.getConfiguration();
        TypeHandlerRegistry typeHandlerRegistry = configuration
                .getTypeHandlerRegistry();
        BoundSql boundSql = ms.getSqlSource().getBoundSql(arg);
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        Object parameterObject = boundSql.getParameterObject();
        if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
            sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(parameterObject)));
        } else {
            MetaObject metaObject = configuration.newMetaObject(parameterObject);
            for (ParameterMapping parameterMapping : parameterMappings) {
                String propertyName = parameterMapping.getProperty();
                if (metaObject.hasGetter(propertyName)) {
                    Object obj = metaObject.getValue(propertyName);
                    sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                } else if (boundSql.hasAdditionalParameter(propertyName)) {
                    Object obj = boundSql
                            .getAdditionalParameter(propertyName);
                    sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                } else {
                    sql = sql.replaceFirst("\\?", "缺失");
                }//打印出缺失，提醒该参数缺失并防止错位
            }
        }
        return sql;
    }

    String[] listenerColumeNames = new String[]{"id_name"};

    private boolean containColumn(Column columnName) {
        List<String> strings = Arrays.asList(listenerColumeNames);
        return strings.contains(columnName.getColumnName());
    }


    private List<Column> getColumnNames(MappedStatement ms, Object arg) {
        Statement statement = parseSql(ms, arg, "解析列名");
        if (statement == null) {
            return null;
        }
        if (SqlCommandType.INSERT == ms.getSqlCommandType()) {
            Insert parse = (Insert) statement;
            return parse.getColumns();
        } else if (SqlCommandType.UPDATE == ms.getSqlCommandType()) {
            Update parse = (Update) statement;
            return parse.getColumns();
        }
        return null;
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
        try {
            //解析表名
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

    private static String getParameterValue(Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(new Date()) + "'";
        } else if (obj instanceof LocalDate) {
            //DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + ((LocalDate) obj).format(DateTimeFormatter.ISO_LOCAL_DATE) + "'";
        } else if (obj instanceof LocalDateTime) {
            //DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + ((LocalDateTime) obj).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            }

        }
        return value;
    }

}
