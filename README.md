# influxdb-spring-boot-starter


## 使用

#### 依赖
```


    <dependency>
        <groupId>plus.ojbk</groupId>
        <artifactId>influxdb-spring-boot-starter</artifactId>
        <version>1.0.0</version>
    </dependency>


```
#### 配置

```

# influxDB
influxdb:
  url: http://127.0.0.1:8086
  username: admin
  password: admin
  database: my_data


```


#### 代码

```
import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author wxm
 * @version 1.0
 * @since 2021/6/17 15:26
 *
 * 瞎编的设备模型
 *
 */
@Data
@Measurement(name = "device")
public class Device {
    /**
     * 设备编号
     */
    @Column(name="device_no", tag = true)  //tag 可以理解为influxdb的索引
    private String deviceNo;
    /**
     * 数据值
     */
    @Column(name="value")
    private BigDecimal value;
    /**
     * 电压
     */
    @Column(name="voltage")
    private Float voltage;
    /**
     * 状态
     */
    @Column(name="state")
    private Boolean state;
    /**
     * 上报时间
     */
    @Column(name="time")
    private LocalDateTime time;

}



```

```

    @Autowired
    private InfluxdbTemplate influxdbTemplate;

    
    /**
     * 批量插入
     */
    public void insert() {
        List<Device> deviceList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Device device = new Device();
            device.setDeviceNo("device-" + i);
            device.setValue(new BigDecimal(23.548));
            device.setState(true);
            device.setVoltage(3.5F);
          //  device.setTime(LocalDateTime.now()); 
            deviceList.add(device);
        }
        influxdbTemplate.insert(deviceList);
    }
    
    
    /**
     * 分页查询
     * 
     */
    public Object data(){
        QueryModel countModel = new QueryModel();
        countModel.setMeasurement(measurement);
        countModel.setStart(LocalDateTime.now().plusHours(-2L));
        countModel.setEnd(LocalDateTime.now());
        countModel.setSelect(Query.count("voltage"));
        countModel.setWhere(Op.where(countModel));
        
        //获得总条数
        long count = influxdbTemplate.count(Query.build(countModel));

        QueryModel model = new QueryModel();
        model.setCurrent(1L);  //当前页
        model.setSize(10L);   //每页数量
        model.setMeasurement(measurement);  //表  必须
        model.setUseTimeZone(true);  //时区 可选
        model.setOrder(Order.DESC);  //排序 可选
        //where 条件中额外参数可放入model.setMap();
        model.setStart(LocalDateTime.now().plusHours(-2L));  //可选
        model.setEnd(LocalDateTime.now());                   //可选
        model.setWhere(Op.where(model));                     //可选
        //分页数据
        List<Device> deviceList = influxdbTemplate.selectList(Query.build(model), Device.class);
        
        
        return //TODO 包装结果;
    }
    
    
    //请自行查看 InfluxdbTemplate
    //如果 还不满足的需求 可以使用 influxdbTemplate.execute(query);
    
```
