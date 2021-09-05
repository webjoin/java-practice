package cn.iwuliao.ds.service;

import cn.iwuliao.ds.core.DsScannerConfigurer;
import cn.iwuliao.ds.domain.AEntity;
import cn.iwuliao.ds.mapper.dba.AMapper;
import cn.iwuliao.ds.mapper.dbb.MapperB;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
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
    public Long commit() {
        int age = Integer.parseInt(RandomStringUtils.randomNumeric(3));
        AEntity entity = AEntity.builder().age(age).desc1("desc::" + RandomStringUtils.randomAlphanumeric(5))
            .idName("idName::" + RandomStringUtils.randomAlphanumeric(5)).localTime(LocalTime.now().plusMinutes(age))
            .localDate(LocalDate.now().plusDays(age)).localDateTime(LocalDateTime.now()).build();
        aMapper.insert(entity, "+" + entity.getIdName());
        entity.setIdName("idName::new::" + RandomStringUtils.randomAlphanumeric(5));
        aMapper.update(entity);
        aMapper.updateById(entity.getAge() + 1, entity.getIdKey());

        // aMapper.delete(11, "desc1", entity.getIdKey());
        // aMapper.deleteForeach(Arrays.asList(entity.getIdKey(), entity.getIdKey()));
        return entity.getIdKey();
    }

    @Override
    @Transactional(transactionManager = "dba" + DsScannerConfigurer.TRANSACTIONMANAGER, rollbackFor = Exception.class)
    public Integer rollback() {
        int age = Integer.parseInt(RandomStringUtils.randomNumeric(2));
        AEntity entity = AEntity.builder().age(age).desc1("desc::" + RandomStringUtils.randomAlphanumeric(5))
            .idName("idName::" + RandomStringUtils.randomAlphanumeric(5)).localTime(LocalTime.now().plusMinutes(age))
            .localDate(LocalDate.now().plusDays(age)).localDateTime(LocalDateTime.now()).build();
        Integer id1 = aMapper.insert(entity, "+" + entity.getIdName());

        entity.setIdName("idName::new::" + RandomStringUtils.randomAlphanumeric(5));
        aMapper.update(entity);
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
