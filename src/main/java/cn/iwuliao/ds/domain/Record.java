package cn.iwuliao.ds.domain;

import lombok.Data;

/**
 * @author elijah
 */
@Data
public class Record {
    private String tableName;
    private String fieldName;
    private String fieldValue;
    private String dataType;
    private String primaryKey;
}
