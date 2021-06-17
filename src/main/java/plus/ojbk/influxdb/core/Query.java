package plus.ojbk.influxdb.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import plus.ojbk.influxdb.core.model.QueryModel;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * @author wxm
 * @version 1.0
 * @since 2021/6/16 17:13
 */
public class Query extends Op {

    private static Logger log = LoggerFactory.getLogger(Query.class);

    /**
     * 构造条件
     *
     * @param model
     * @return
     */
    public static String build(QueryModel model) {
        Objects.requireNonNull(model.getMeasurement(), "QueryModel.Measurement");
        StringBuilder query = new StringBuilder();
        query.append("select ").append(model.getSelect());
        query.append(" from ").append(model.getMeasurement());
        if (!ObjectUtils.isEmpty(model.getWhere())) {
            query.append(" where ").append(model.getWhere());
        }
        if (!ObjectUtils.isEmpty(model.getOrder())) {
            query.append(" order by time ").append(model.getOrder());
        }
        if (!ObjectUtils.isEmpty(model.getCurrent()) && !ObjectUtils.isEmpty(model.getSize())) {
            query.append(" ").append(model.getPageQuery());
        }
        if (model.getUseTimeZone()) {
            query.append(" ").append(model.getTimeZone());
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
        StringBuilder sb = new StringBuilder();
        sb.append("count(").append("\"").append(field).append("\"").append(")");
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        Map<String, Object> map = new TreeMap<>();
        map.put("device_id", "666");
        map.put("temp", 5.1);
        QueryModel model = new QueryModel();
        model.setMap(map);
        // model.setStart(LocalDateTime.now().plusHours(-10L));
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
