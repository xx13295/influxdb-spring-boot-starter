package plus.ojbk.influxdb.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import plus.ojbk.influxdb.util.CommonUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * @author wxm
 * @version 1.0
 * @since 2021/6/16 17:13
 */
public class Query {

    private static Logger log = LoggerFactory.getLogger(Query.class);
    /**
     * 构造查询条件
     *
     * @param queryModel
     * @return
     */
    public static String build(QueryModel queryModel) {
        Objects.requireNonNull(queryModel.getMeasurement(), "QueryModel.Measurement");
        StringBuilder query = new StringBuilder();
        query.append("select ").append(queryModel.getSelect());
        query.append(" from ").append(queryModel.getMeasurement());
        if (!ObjectUtils.isEmpty(queryModel.getWhere())) {
            query.append(" where ").append(queryModel.getWhere());
        }
        if (!ObjectUtils.isEmpty(queryModel.getOrder())) {
            query.append(" order by time ").append(queryModel.getOrder());
        }
        if (!ObjectUtils.isEmpty(queryModel.getCurrent()) && !ObjectUtils.isEmpty(queryModel.getSize())) {
            query.append(" ").append(queryModel.getPageQuery());
        }
        if (queryModel.getUseTimeZone()) {
            query.append(" ").append(queryModel.getTimeZone());
        }
        String queryCmd = query.toString();
        log.info(queryCmd);
        return query.toString();
    }

    /**
     * count Field 字段
     *
     * @param field
     * @return
     */
    public static String count(String field) {
        return "count(" + field + ")";
    }



    private static StringBuilder time(LocalDateTime start, LocalDateTime end) {
        Instant startTime = CommonUtils.parseLocalDateTimeToInstant(start);
        Instant endTime = CommonUtils.parseLocalDateTimeToInstant(end);
        StringBuilder sb = new StringBuilder();
        sb.append("time >='").append(startTime);
        sb.append("' AND time <='").append(endTime).append("'");
        return sb;
    }

    /**
     * where 条件默认带时间
     *
     * @return
     */
    public static String where(QueryModel queryModel) {
        StringBuilder sb = time(queryModel.getStart(), queryModel.getEnd());
        if(!ObjectUtils.isEmpty(queryModel.getMap())){
            for (Map.Entry<String, Object> entry : queryModel.getMap().entrySet()) {
                sb.append(" AND ").append(entry.getKey()).append("=");
                Object value = entry.getValue();
                if (value instanceof String) {
                    sb.append("'").append(value).append("'");
                } else {
                    sb.append(entry.getValue());
                }
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        Map<String, Object> map = new TreeMap<>();
        map.put("device_id", "666");
        map.put("temp", 5.1);

        QueryModel model = new QueryModel();
       // model.setMap(map);
        model.setStart(LocalDateTime.now().plusHours(-10L));
        model.setEnd(LocalDateTime.now());

        //model.setCurrent(2L);
        //model.setSelect(count("temp"));
        // model.setSize(10L);
        model.setMeasurement("ojbk");
        model.setUseTimeZone(true);
        model.setWhere(Query.where(model));
        //model.setOrder(Order.ASC);

        System.err.println(Query.build(model));



    }

}
