package com.iquickmove.base.request;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Map;

/**
 * todo ty 非abstract fixed 名称晚点改
 *
 * @author elijah
 */
public class AbstractRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String clientId;
    private Map<String, String> extention;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Map<String, String> getExtention() {
        return extention;
    }

    public void setExtention(Map<String, String> extention) {
        this.extention = extention;
    }

    @Override
    public String toString() {
        ToStringStyle style = ToStringStyle.JSON_STYLE;
        return ReflectionToStringBuilder.toString(this, style, false, false);
    }
}
