package plus.ojbk.influxdb.core.model;

import org.springframework.util.ObjectUtils;
import plus.ojbk.influxdb.core.enums.Order;


/**
 * @author wxm
 * @version 1.0
 * @since 2021/6/16 15:22
 */
public class QueryModel extends BaseModel {
    /**
     * 查询的字段
     */
    private String select;
    /**
     * influxdb仅支持time排序
     * Order.DESC Order.ASC
     */
    private Order order;
    /**
     * 当前页
     */
    private Long current;
    /**
     * 每页的大小
     */
    private Long size;
    /**
     * 默认不使用时区
     */
    private Boolean useTimeZone = false;
    /**
     * 默认时区 Asia/Shanghai
     */
    private String timeZone = "tz('Asia/Shanghai')";
    /**
     * 分组
     */
    private String group;

    public QueryModel() {

    }

    public QueryModel(String measurement) {
        super(measurement);
    }

    public String getPageQuery() {
        return "limit " + size + " offset " + (current - 1) * size;
    }

    public String getSelect() {
        if (ObjectUtils.isEmpty(select)) {
            select = "*";
        }
        return select;
    }


    public void setSelect(String select) {
        this.select = select;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Long getCurrent() {
        return current;
    }

    public void setCurrent(Long current) {
        this.current = current;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Boolean getUseTimeZone() {
        return useTimeZone;
    }

    public void setUseTimeZone(Boolean useTimeZone) {
        this.useTimeZone = useTimeZone;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
