package plus.ojbk.influxdb.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    protected static Logger log = LoggerFactory.getLogger(Op.class);

    public static StringBuilder time(LocalDateTime start, LocalDateTime end) {
        Instant startTime = CommonUtils.parseLocalDateTimeToInstant(start);
        Instant endTime = CommonUtils.parseLocalDateTimeToInstant(end);
        StringBuilder sb = new StringBuilder();
        sb.append("time >='").append(startTime);
        sb.append("' and time <='").append(endTime).append("'");
        return sb;
    }

    /**
     * 默认条件 为 =
     * 其他条件暂时未实现请自行构造
     *
     * @param model
     * @return
     */
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
