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


    @Insert("insert into a(id) values(#{id})")
    Integer insert(@Param("id") int id);


    List<String> likeQry(@Param("name") String name);


}
