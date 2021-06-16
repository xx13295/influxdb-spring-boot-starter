package plus.ojbk.influxdb.core;

import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.Map;


/**
 * @author wxm
 * @version 1.0
 * @since 2021/6/16 15:22
 */
public class QueryModel {
    /**
     *查询的字段
     */
    private String select;
    /**
     * 表
     */
    private String measurement;
    /**
     * 条件
     */
    private String where;
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
     * 开始时间
     */
    private LocalDateTime start;
    /**
     * 结束时间
     */
    private LocalDateTime end;
    /**
     * where 条件额外参数 
     */
    private Map<String, Object> map;


    public QueryModel(){

    }

    public QueryModel(String measurement){
        this.measurement = measurement;
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

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
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

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}
