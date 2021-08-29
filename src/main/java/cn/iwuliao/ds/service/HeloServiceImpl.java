package cn.iwuliao.ds.service;

import cn.iwuliao.ds.core.DsScannerConfigurer;
import cn.iwuliao.ds.mapper.dba.AMapper;
import cn.iwuliao.ds.mapper.dbb.MapperB;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        int id = Integer.parseInt(RandomStringUtils.randomNumeric(3));
        int age = Integer.parseInt(RandomStringUtils.randomNumeric(2));
        aMapper.insert(id, age);
        return id;
    }

    @Override
    @Transactional(transactionManager = "dba" + DsScannerConfigurer.TRANSACTIONMANAGER, rollbackFor = Exception.class)
    public Integer rollback() {
        int id = Integer.parseInt(RandomStringUtils.randomNumeric(3));
        int age = Integer.parseInt(RandomStringUtils.randomNumeric(3));
        Integer id1 = aMapper.insert(id, age);

        int i = id1 / 0;
        return id1;
    }

    @Override
    public Integer hib() {
        Integer id = mapperB.getId(1);
        return id;
    }

    @Override
    public List<String> likeQry(String name) {
        return aMapper.likeQry(name);
    }

}
