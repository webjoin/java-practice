package cn.iwuliao.ds.service;

import cn.iwuliao.ds.core.DsScannerConfigurer;
import cn.iwuliao.ds.mapper.dba.AMapper;
import cn.iwuliao.ds.mapper.dbb.MapperB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author tangyu
 * @since 2019-04-23 00:56
 */
@Service
public class HeloServiceImpl implements HeloService {


    @Autowired
    private AMapper aMapper;

    @Autowired
    private MapperB mapperB;

    @Override
    public Integer hia() {
        Integer id = aMapper.getId(1);
        return id;
    }

    @Override
    @Transactional(transactionManager = "dba" + DsScannerConfigurer.TRANSACTIONMANAGER, rollbackFor = Exception.class)
    public Integer commit() {
        Integer id = aMapper.insert(2);
        return id;
    }

    @Override
    @Transactional(transactionManager = "dba" + DsScannerConfigurer.TRANSACTIONMANAGER, rollbackFor = Exception.class)
    public Integer rollback() {
        Integer id = aMapper.insert(3);
        int i = id / 0;
        return id;
    }

    @Override
    public Integer hib() {
        Integer id = mapperB.getId(1);
        return id;
    }

}
