package plus.ojbk.influxdb.core.enums;


/**
 * 常用的聚合函数枚举
 * @author wxm
 * @version 1.0
 * @since 2021/6/24 12:42
 */
public enum Function {

    SUM("sum", "累加"),
    LAST("last", "最后一条数据"),
    MEAN("mean", "平均数")

    ;
    private final String tag;

    private final String content;

    Function(String tag, String content) {
        this.tag = tag;
        this.content = content;
    }

    public String getTag() {
        return tag;
    }

    public String getContent() {
        return content;
    }
}
