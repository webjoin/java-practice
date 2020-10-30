package cn.iwuliao.trade.busiservice.mapper.dbb;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author tangyu
 * @since 2019-04-22 22:25
 */
public interface MapperB {


    @Select("select id from b where id = #{id}")
    Integer getId(@Param("id") int id);
}
