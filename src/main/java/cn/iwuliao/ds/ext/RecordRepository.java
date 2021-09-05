package cn.iwuliao.ds.ext;

import cn.iwuliao.ds.domain.Record;
import cn.iwuliao.ds.mapper.dba.AMapper;
import cn.iwuliao.ds.mapper.dba.RecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("RecordRepository")
public class RecordRepository {

    @Autowired
    private AMapper aMapper;

    @Autowired
    private RecordMapper recordMapper;

    public void save(String tableName, String filedName, String fieldValue, String primaryKey, String dataType) {
        Record record = new Record();
        record.setTableName(tableName);
        record.setFieldName(filedName);
        record.setFieldValue(fieldValue);
        record.setDataType(dataType);
        record.setPrimaryKey(primaryKey);
        recordMapper.save(record, record.getDataType());
    }
}
