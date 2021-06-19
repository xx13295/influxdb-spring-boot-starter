package plus.ojbk.influxdb.core.model;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author wxm
 * @version 1.0
 * @since 2021/6/17 11:00
 */
public class BaseModel {
    /**
     * 表
     */
    private String measurement;
    /**
     * 条件
     */
    private String where;
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

    public BaseModel() {

    }

    public BaseModel(String measurement) {
        this.measurement = measurement;
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
