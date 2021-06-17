package plus.ojbk.influxdb.util;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;
import org.influxdb.dto.Point;
import org.influxdb.dto.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author wxm
 * @version 1.0
 * @since 2021/6/15 18:02
 */
public class InfluxdbUtils {
    private static Logger log = LoggerFactory.getLogger(InfluxdbUtils.class);

    /**
     * 将queryResult 转换为实体对象List
     * influxdb-java 本身提供了 InfluxDBResultMapper.toPOJO(queryResult, clazz);
     * 上述方式得用 Instant 时间就很难受
     *
     * @param queryResult
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> toPOJO(final QueryResult queryResult, final Class<T> clazz) {
        Objects.requireNonNull(queryResult, "queryResult");
        Objects.requireNonNull(clazz, "clazz");
        List<T> results = new LinkedList<T>();
        for (QueryResult.Result result : queryResult.getResults()) {
            if (result.getSeries() != null) {
                for (QueryResult.Series series : result.getSeries()) {
                    List<String> columns = series.getColumns();
                    int fieldSize = columns.size();
                    for (List<Object> value : series.getValues()) {
                        T obj = null;
                        try {
                            obj = clazz.newInstance();
                            for (int i = 0; i < fieldSize; i++) {
                                String fieldName = columns.get(i);
                                Field field = null;
                                try {
                                    field = clazz.getDeclaredField(CommonUtils.lineToHump(fieldName));
                                    field.setAccessible(true);
                                } catch (NoSuchFieldException e) {
                                    log.error("Field :{} Not fount, error :{}", fieldName, e.getMessage());
                                }
                                if (field != null) {
                                    Class<?> type = field.getType();
                                    setFieldValue(type, value, i, obj, field);
                                }
                            }
                            //tags 仅在group by tag 字段时使用
                            if (series.getTags() != null && !series.getTags().isEmpty()) {
                                for (Map.Entry<String, String> entry : series.getTags().entrySet()) {
                                    Field field = null;
                                    try {
                                        field = clazz.getDeclaredField(CommonUtils.lineToHump(entry.getKey()));
                                        field.setAccessible(true);
                                    } catch (NoSuchFieldException e) {
                                        log.error("Field :{} Not fount, error :{}", entry.getKey(), e.getMessage());
                                    }
                                    if (field != null) {
                                        setFieldValue(obj, field, entry.getValue());
                                    }

                                }
                            }


                        } catch (SecurityException | InstantiationException | IllegalAccessException e) {
                            log.error("Influxdb toPOJO error :{}", e.getMessage());
                        }
                        results.add(obj);
                    }

                }
            } else {
                log.info("QueryResult.Result Is Null.");
            }

        }
        return results;
    }

    /**
     * 获取数据count
     *
     * @param queryResult
     * @return
     */
    public static long count(QueryResult queryResult) {
        for (QueryResult.Result result : queryResult.getResults()) {
            if (result.getSeries() != null) {
                for (QueryResult.Series series : result.getSeries()) {
                    List<String> columns = series.getColumns();
                    int index = columns.indexOf("count");
                    if (index != -1) {
                        BigDecimal count = new BigDecimal(series.getValues().get(0).get(index).toString());
                        return count.longValue();
                    }

                }
            }
        }
        return 0;
    }

    /**
     * 保存
     * Point.Builder.field 虽然已过时 理论上不会被删除吧
     *
     * Point.Builder.addField方法不够灵活 如果我是 BigDecimal 就傻了
     *
     * @param object 实体对象
     * @return
     */
    public static Point save(Object object) {
        Class<?> clazz = object.getClass();
        Measurement measurement = clazz.getAnnotation(Measurement.class);
        Point.Builder builder = Point.measurement(measurement.name());
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            try {
                Column column = field.getAnnotation(Column.class);
                field.setAccessible(true);
                if (column.tag()) {
                    builder.tag(column.name(), field.get(object).toString());
                } else {
                    if (field.get(object) != null) {
                        builder.field(column.name(), field.get(object));
                    }
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                log.error("Influxdb save error. error :{}", e.getMessage());
            }
        }
        return builder.build();
    }

    /**
     * 删除数据
     * influxdb-java 本身删除不会返回清理的条数
     * 因此这里的 1 仅为默认成功返回
     * 如果存在错误将抛出具体的错误
     *
     * @param queryResult
     * @return
     */
    public static long delete(QueryResult queryResult) {
        for (QueryResult.Result result : queryResult.getResults()) {
            if (result.getError() != null) {
                throw new RuntimeException(result.getError());
            }
        }
        return 1;
    }


    /**
     * 赋值
     *
     * @param type
     * @param value
     * @param i
     * @param obj
     * @param field
     * @throws IllegalAccessException
     */
    private static void setFieldValue(Class<?> type, List<Object> value, int i, Object obj, Field field) throws IllegalAccessException {
        if (type.equals(Long.class)) {
            BigDecimal v = new BigDecimal(value.get(i).toString());
            field.set(obj, v.longValue());
        } else if (type.equals(Integer.class)) {
            BigDecimal v = new BigDecimal(value.get(i).toString());
            field.set(obj, v.intValue());
        } else if (type.equals(Float.class)) {
            BigDecimal v = new BigDecimal(value.get(i).toString());
            field.set(obj, v.floatValue());
        } else if (type.equals(Double.class)) {
            BigDecimal v = new BigDecimal(value.get(i).toString());
            field.set(obj, v.doubleValue());
        } else if (type.equals(BigDecimal.class)) {
            BigDecimal v = new BigDecimal(value.get(i).toString());
            field.set(obj, v);
        } else if (type.equals(LocalDateTime.class)) {
            field.set(obj, CommonUtils.parseStringToLocalDateTime(value.get(i).toString()));
        } else {
            field.set(obj, value.get(i));
        }
    }

    /**
     * 赋值
     *
     * @param obj
     * @param field
     * @param value
     * @throws IllegalAccessException
     */
    private static void setFieldValue(Object obj, Field field, Object value) throws IllegalAccessException {
        field.set(obj, value);
    }


}