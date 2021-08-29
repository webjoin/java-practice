package cn.iwuliao.ds.mapper.dba;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author tangyu
 * @since 2019-04-22 22:25
 */
public interface AMapper {


    @Select("select id from a where id = #{id}")
    Integer getId(@Param("id") int id);


    @Insert("insert into template(id_name,value) values(#{id_name},#{value})")
    Integer insertTemplate(@Param("id_name") String id_name, @Param("value") String value);


    @Insert("insert into a(id,id_name,age,desc) values(#{id01},'name',#{age1},'desc01')")
    Integer insert(@Param("id01") int id, @Param("age1") int age1);


    List<String> likeQry(@Param("name") String name);


}
