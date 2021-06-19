package plus.ojbk.influxdb.core;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;
import plus.ojbk.influxdb.util.CommonUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author wxm
 * @version 1.0
 * @since 2021/6/18 18:54
 */
public class Insert extends Op {

    /**
     * 构造条件
     *
     * @param
     * @return
     */
    public static String build(Object object) {
        Objects.requireNonNull(object, "实体不能为空");
        StringBuilder insert = new StringBuilder();
        String time = "";
        Class<?> clazz = object.getClass();
        Measurement measurement = clazz.getAnnotation(Measurement.class);
       // insert.append("insert ");
        insert.append(measurement.name());
        Field[] fields = clazz.getDeclaredFields();
        int i = 0;
        for (Field field : fields) {
            try {
                Column column = field.getAnnotation(Column.class);
                field.setAccessible(true);
                if (column.tag()) {
                    if (field.get(object) != null) {
                        insert.append(",").append(column.name()).append("=").append(field.get(object));
                    }
                } else {
                    if (field.get(object) != null) {
                        if ("time".equals(column.name())) {
                            time = CommonUtils.parseLocalDateTimeToInstant((LocalDateTime) field.get(object)).getEpochSecond() + "000000000";
                        } else {
                            if (i == 0) {
                                insert.append(" ");
                            } else {
                                insert.append(",");
                            }
                            insert.append(column.name()).append("=").append(field.get(object));
                            i++;
                        }

                    }
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                log.error("Influxdb save error. error :{}", e.getMessage());
            }
        }
        insert.append(" ").append(time);
        String sql = insert.toString();
        log.debug("insert " + sql);
        return sql;
    }
}
