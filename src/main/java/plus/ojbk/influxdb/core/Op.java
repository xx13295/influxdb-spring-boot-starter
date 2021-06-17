package plus.ojbk.influxdb.core;

import org.springframework.util.ObjectUtils;
import plus.ojbk.influxdb.core.model.BaseModel;
import plus.ojbk.influxdb.util.CommonUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author wxm
 * @version 1.0
 * @since 2021/6/16 20:09
 */
public class Op {

    protected static StringBuilder time(LocalDateTime start, LocalDateTime end) {
        Instant startTime = CommonUtils.parseLocalDateTimeToInstant(start);
        Instant endTime = CommonUtils.parseLocalDateTimeToInstant(end);
        StringBuilder sb = new StringBuilder();
        sb.append("time >='").append(startTime);
        sb.append("' and time <='").append(endTime).append("'");
        return sb;
    }

    public static String where(BaseModel model) {
        StringBuilder sb = new StringBuilder();
        if (!ObjectUtils.isEmpty(model.getStart()) && !ObjectUtils.isEmpty(model.getEnd())) {
            sb = time(model.getStart(), model.getEnd());
            if (!ObjectUtils.isEmpty(model.getMap())) {
                for (Map.Entry<String, Object> entry : model.getMap().entrySet()) {
                    sb.append(" and ").append("\"").append(entry.getKey()).append("\"").append("=");
                    Object value = entry.getValue();
                    if (value instanceof String) {
                        sb.append("'").append(value).append("'");
                    } else {
                        sb.append(entry.getValue());
                    }
                }
            }
        } else {
            if (!ObjectUtils.isEmpty(model.getMap())) {
                int i = 0;
                for (Map.Entry<String, Object> entry : model.getMap().entrySet()) {
                    if (i != 0) {
                        sb.append(" and ");
                    }
                    sb.append("\"").append(entry.getKey()).append("\"").append("=");
                    Object value = entry.getValue();
                    if (value instanceof String) {
                        sb.append("'").append(value).append("'");
                    } else {
                        sb.append(entry.getValue());
                    }
                    i++;
                }
            }
        }
        return sb.toString();
    }
}
