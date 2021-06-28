package plus.ojbk.influxdb.annotation;


import plus.ojbk.influxdb.core.enums.Function;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wxm
 * @version 1.0
 * @since 2021/6/24 11:22
 *
 * influxdb 常用聚合函数字段注解
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Aggregate {
    /**
     * 字段名
     * @return
     */
    String value();

    /**
     * 字段使用的聚合函数
     * @return
     */
    Function tag();
}
