package cn.iwuliao.ds.mapper.dba;

import java.util.List;

import cn.iwuliao.ds.domain.Record;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author tangyu
 * @since 2019-04-22 22:25
 */
public interface RecordMapper {

    @Select("INSERT INTO dba.record (table_name, field_name, field_value, primary_key, data_type) VALUES (#{record01.tableName}, #{record01.fieldName},  #{record01.fieldValue},#{record01.primaryKey}, #{dataType} )")
    Integer save(@Param("record01") Record record, @Param("dataType") String dataType);

}
