package cn.iwuliao.trade.service;


import java.util.List;

/**
 * @author tangyu
 * @since 2019-04-23 00:55
 */
public interface HeloService {



    Integer hia();

    Integer commit();

    Integer rollback();

    Integer hib();

    List<String> likeQry(String name);
}
