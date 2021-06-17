package plus.ojbk.influxdb.core;

import org.springframework.util.ObjectUtils;
import plus.ojbk.influxdb.core.model.DeleteModel;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * @author wxm
 * @version 1.0
 * @since 2021/6/17 8:45
 */
public class Delete extends Op {

    /**
     * 构造条件
     *
     * @param model
     * @return
     */
    public static String build(DeleteModel model) {
        Objects.requireNonNull(model.getMeasurement(), "DeleteModel.Measurement");
        StringBuilder delete = new StringBuilder();
        delete.append("delete from ").append(model.getMeasurement());
        if (!ObjectUtils.isEmpty(model.getWhere())) {
            delete.append(" where ").append(model.getWhere());
        }else {
            throw new RuntimeException("where 条件缺失");
        }
        String sql = delete.toString();
        log.info(sql);
        return sql;
    }



    public static void main(String[] args) throws Exception {
        Map<String, Object> map = new TreeMap<>();
        map.put("device_id", "666");
        map.put("temp", 5.1);
        DeleteModel model = new DeleteModel();
        model.setMap(map);
        model.setStart(LocalDateTime.now().plusHours(-10L));
        model.setEnd(LocalDateTime.now());
        model.setMeasurement("ojbk");
        model.setWhere(Op.where(model));

        System.err.println(Delete.build(model));
    }

}
