package cn.iwuliao.ds.mapper.dba;

import cn.iwuliao.ds.domain.AEntity;
import org.apache.ibatis.annotations.*;

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

    Integer insert(@Param("insert") AEntity aEntity, @Param("idName") String idName);

    Integer update(AEntity aEntity);

    @Update("UPDATE a SET  age = #{age} WHERE id = #{idKey}")
    Integer updateById(@Param("age") Integer abc, @Param("idKey") Long id);

    // @Delete()
    Integer deleteForeach(List<Long> idList);

    Integer delete(@Param("age") Integer abc, @Param("desc1") String desc1, @Param("val") Long key);

    List<String> likeQry(@Param("name") String name);

}
