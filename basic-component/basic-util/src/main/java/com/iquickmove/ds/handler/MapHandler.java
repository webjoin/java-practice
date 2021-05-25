package com.iquickmove.ds.handler;

import com.iquickmove.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class MapHandler extends BaseTypeHandler<Map<String, String>> {

    private static final String NULL = "null";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Map<String, String> parameter, JdbcType jdbcType)
        throws SQLException {
        if (jdbcType == null) {
            ps.setString(i, convertToString(parameter));
        } else {
            // see r3589
            ps.setObject(i, convertToString(parameter), jdbcType.TYPE_CODE);
        }
    }

    @Override
    public Map<String, String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseFromString(rs.getString(columnName));
    }

    @Override
    public Map<String, String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseFromString(rs.getString(columnIndex));
    }

    @Override
    public Map<String, String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseFromString(cs.getString(columnIndex));
    }

    /**
     * Map 转化为 String
     */
    private static String convertToString(Map<String, String> map) {
        if (map == null || map.size() == 0) {
            return null;
        }
        return JsonUtil.toJsonStr(map);
    }

    /**
     * String 转化为 Map
     */
    private static Map<String, String> parseFromString(String mapString) {
        if (StringUtils.isBlank(mapString) || StringUtils.equalsIgnoreCase(NULL, mapString)) {
            return null;
        }
        try {
            return JsonUtil.json2Bean(mapString, new TypeReference<Map<String, String>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Json非Map格式", e);
        }
    }

}