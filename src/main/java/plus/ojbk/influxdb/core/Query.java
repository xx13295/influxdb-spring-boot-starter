package plus.ojbk.influxdb.core;

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
        if (!ObjectUtils.isEmpty(model.getGroup())) {
            query.append(" group by ").append(model.getGroup());
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
        String sql = query.toString();
        log.info(sql);
        return sql;
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


    /**
     * 聚合函数构建
     * @param tag
     * @param field
     * @return
     */
    public static StringBuilder funcAggregate(String tag, String field) {
        StringBuilder sb = new StringBuilder();
        sb.append(tag).append("(").append("\"").append(field).append("\"").append(")");
        sb.append(" as ").append("\"").append(field).append("\"");
        return sb;
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
        model.setWhere(Op.where(model));
        //model.setOrder(Order.ASC);

        System.err.println(Query.build(model));


    }

}
