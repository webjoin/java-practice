package cn.iwuliao.ds.core.yml;

public class YamlConversionResult {
    ConversionStatus status;
    String yaml;

    public static YamlConversionResult EMPTY = new YamlConversionResult(ConversionStatus.EMPTY, "");

    YamlConversionResult(ConversionStatus status, String output) {
        this.status = status;
        this.yaml = output;
    }

    public String getYaml() {
        return yaml;
    }

    public ConversionStatus getStatus() {
        return status;
    }

    public int getSeverity() {
        return status.getSeverity();
    }
}