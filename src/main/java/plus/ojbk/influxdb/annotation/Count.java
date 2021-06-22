package plus.ojbk.influxdb.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wxm
 * @version 1.0
 * @since 2021/6/21 17:22
 *
 * influxdb 分页查询 count 字段注解
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Count {

    String value() default "";
}
