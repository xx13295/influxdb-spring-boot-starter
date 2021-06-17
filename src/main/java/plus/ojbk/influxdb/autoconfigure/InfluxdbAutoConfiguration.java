package plus.ojbk.influxdb.autoconfigure;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import plus.ojbk.influxdb.autoconfigure.properties.InfluxdbProperties;
import plus.ojbk.influxdb.core.InfluxdbTemplate;


/**
 * @author wxm
 * @version 1.0
 * @since 2021/6/16 13:39
 */
@Configuration
@EnableConfigurationProperties({InfluxdbProperties.class})
@ConditionalOnProperty(prefix = "influxdb", value = "enable", matchIfMissing = true)
public class InfluxdbAutoConfiguration {

    public InfluxdbAutoConfiguration() {

    }


    @Bean
    @ConditionalOnMissingBean
    public InfluxDB influxdb(InfluxdbProperties influxdbProperties) {
        InfluxDB influxDB = InfluxDBFactory.connect(influxdbProperties.getUrl(), influxdbProperties.getUsername(), influxdbProperties.getPassword());
        influxDB.setDatabase(influxdbProperties.getDatabase());
        influxDB.setLogLevel(InfluxDB.LogLevel.BASIC);
        return influxDB;
    }


    @Bean
    public InfluxdbTemplate minioTemplate(InfluxdbProperties influxdbProperties) {
        return new InfluxdbTemplate(influxdbProperties);
    }


}
