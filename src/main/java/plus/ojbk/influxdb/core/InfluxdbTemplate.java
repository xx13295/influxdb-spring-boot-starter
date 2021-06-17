package plus.ojbk.influxdb.core;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import plus.ojbk.influxdb.autoconfigure.properties.InfluxdbProperties;
import plus.ojbk.influxdb.util.InfluxdbUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wxm
 * @version 1.0
 * @since 2021/6/16 16:42
 * <p>
 * 基础语句
 * select <field_key>[,<field_key>,<tag_key>] from <measurement_name>[,<measurement_name>]
 * insert into <retention policy> measurement,tagKey=tagValue fieldKey=fieldValue timestamp
 * delete from <measurement_name> where [<tag_key>='<tag_value>'] | [<time interval>]
 */
public class InfluxdbTemplate {

    @Autowired
    private InfluxDB influxDB;

    private String database;
    private InfluxdbProperties properties;

    public InfluxdbTemplate(InfluxdbProperties properties) {
        this.properties = properties;
        this.database = properties.getDatabase();
    }


    /***
     * 默认执行方法
     * @param query  sql语句
     * @return
     */
    public QueryResult execute(String query) {
        return influxDB.query(new Query(query, database));
    }

    /**
     * 查询 返回对应实体 List
     *
     * @param query sql语句
     * @param clazz 实体
     * @param <T>
     * @return
     */
    public <T> List<T> selectList(String query, Class<T> clazz) {
        return InfluxdbUtils.toPOJO(execute(query), clazz);
    }

    /**
     * 获取 count
     * 仅支持 Field 字段
     *
     * @param query sql语句
     * @return
     */
    public long count(String query) {
        return InfluxdbUtils.count(execute(query));
    }

    /**
     * 批量插入
     *
     * @param entity 实体
     */
    public void insert(List<?> entity) {
        List<String> data = new ArrayList<>();
        for (Object object : entity) {
            data.add(InfluxdbUtils.save(object).lineProtocol());
        }
        influxDB.write(data);
    }

    /**
     * 插入
     *
     * @param entity 实体
     */
    public void insert(Object entity) {
        influxDB.write(InfluxdbUtils.save(entity));
    }

    /**
     * 插入
     *
     * @param tags 标签索引字段map
     * @param fields 普通字段map
     * @param measurement 表
     */
    public void insert(Map<String, String> tags, Map<String, Object> fields, String measurement) {
        Point.Builder builder = Point.measurement(measurement);
        builder.tag(tags);
        builder.fields(fields);
        influxDB.write(database, "", builder.build());
    }

    /**
     * 删除
     * 只允许根据tag和时间来进行删除操作
     * field字段删除无效
     * 不推荐使用
     *
     * @param query sql语句
     * @return
     */
    public long delete(String query) {
        return InfluxdbUtils.delete(execute(query));
    }

}
