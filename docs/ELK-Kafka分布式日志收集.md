<font size=4.5>

**ELK-Kafka分布式日志收集**

---

- **文章目录**

* [1\. ELK搭建详细教程参考](#1-elk%E6%90%AD%E5%BB%BA%E8%AF%A6%E7%BB%86%E6%95%99%E7%A8%8B%E5%8F%82%E8%80%83)
* [2\. ELK\-Kafka分布式日志收集架构设计](#2-elk-kafka%E5%88%86%E5%B8%83%E5%BC%8F%E6%97%A5%E5%BF%97%E6%94%B6%E9%9B%86%E6%9E%B6%E6%9E%84%E8%AE%BE%E8%AE%A1)
* [3\. 环境搭建部署](#3-%E7%8E%AF%E5%A2%83%E6%90%AD%E5%BB%BA%E9%83%A8%E7%BD%B2)
  * [3\.1 环境准备](#31-%E7%8E%AF%E5%A2%83%E5%87%86%E5%A4%87)
  * [3\.2 创建网络](#32-%E5%88%9B%E5%BB%BA%E7%BD%91%E7%BB%9C)
  * [3\.3 elasticsearch](#33-elasticsearch)
    * [3\.3\.1 elasticsearch配置文件](#331-elasticsearch%E9%85%8D%E7%BD%AE%E6%96%87%E4%BB%B6)
    * [3\.3\.2 docker\-compose配置文件](#332-docker-compose%E9%85%8D%E7%BD%AE%E6%96%87%E4%BB%B6)
    * [3\.3\.3 构建脚本](#333-%E6%9E%84%E5%BB%BA%E8%84%9A%E6%9C%AC)
  * [3\.4 logstash](#34-logstash)
    * [3\.4\.1 订阅kafka数据来源配置文件](#341-%E8%AE%A2%E9%98%85kafka%E6%95%B0%E6%8D%AE%E6%9D%A5%E6%BA%90%E9%85%8D%E7%BD%AE%E6%96%87%E4%BB%B6)
    * [3\.4\.2 docker\-compose配置文件](#342-docker-compose%E9%85%8D%E7%BD%AE%E6%96%87%E4%BB%B6)
    * [3\.4\.3 构建脚本](#343-%E6%9E%84%E5%BB%BA%E8%84%9A%E6%9C%AC)
  * [3\.5 kibana](#35-kibana)
    * [3\.5\.1 docker\-compose配置文件](#351-docker-compose%E9%85%8D%E7%BD%AE%E6%96%87%E4%BB%B6)
    * [3\.5\.2 构建脚本](#352-%E6%9E%84%E5%BB%BA%E8%84%9A%E6%9C%AC)
  * [3\.6 zookeeper](#36-zookeeper)
    * [3\.6\.1 docker\-compose配置文件](#361-docker-compose%E9%85%8D%E7%BD%AE%E6%96%87%E4%BB%B6)
    * [3\.6\.2 构建脚本](#362-%E6%9E%84%E5%BB%BA%E8%84%9A%E6%9C%AC)
  * [3\.7 kafka](#37-kafka)
    * [3\.7\.1 docker\-compose配置文件](#371-docker-compose%E9%85%8D%E7%BD%AE%E6%96%87%E4%BB%B6)
    * [3\.7\.2 构建脚本](#372-%E6%9E%84%E5%BB%BA%E8%84%9A%E6%9C%AC)
  * [3\.8 kafka\-manager](#38-kafka-manager)
    * [3\.8\.1 docker\-compose配置文件](#381-docker-compose%E9%85%8D%E7%BD%AE%E6%96%87%E4%BB%B6)
    * [3\.8\.2 构建脚本](#382-%E6%9E%84%E5%BB%BA%E8%84%9A%E6%9C%AC)
  * [3\.9 搭建部署完成](#39-%E6%90%AD%E5%BB%BA%E9%83%A8%E7%BD%B2%E5%AE%8C%E6%88%90)
* [4\. springboot整合elk\+kafka](#4-springboot%E6%95%B4%E5%90%88elkkafka)
  * [4\.1 elasticsearch查询模块](#41-elasticsearch%E6%9F%A5%E8%AF%A2%E6%A8%A1%E5%9D%97)
  * [4\.2 kafka日志收集模块](#42-kafka%E6%97%A5%E5%BF%97%E6%94%B6%E9%9B%86%E6%A8%A1%E5%9D%97)
  * [4\.3 测试日志收集](#43-%E6%B5%8B%E8%AF%95%E6%97%A5%E5%BF%97%E6%94%B6%E9%9B%86)

# 1. ELK搭建详细教程参考

[ELK详解文档](https://github.com/FocusProgram/springcloud-elk/blob/master/docs/ELK详解.md)

# 2. ELK-Kafka分布式日志收集架构设计

> 使用SpringAop进行日志收集，然后通过kafka将日志发送给logstash，logstash再将日志写入elasticsearch，这样elasticsearch就有了日志数据了，最后，则使用kibana将存放在elasticsearch中的日志数据显示出来，并且可以做实时的数据图表分析等等。

![](http://image.focusprogram.top/elk-kafka.png)

# 3. 环境搭建部署

## 3.1 环境准备

| 服务名                | Docker ip地址   | 宿主机ip地址           | 开放端口      | 功能             |
|--------------------|---------------|-------------------|-----------|----------------|
| elasticsearch\-one | 172\.20\.0\.2 | 192\.168\.80\.130 | 9200、9300 | 搜索             |
| elasticsearch\-two | 172\.20\.0\.3 | 192\.168\.80\.130 | 9202、9303 | 搜索             |
| logstash           | 172\.20\.0\.4 | 192\.168\.80\.130 | 5044      | 日志收集           |
| kibana             | 172\.20\.0\.5 | 192\.168\.80\.130 | 5061      | 展示、监控          |
| zookeeper          | 172\.20\.0\.6 | 192\.168\.80\.130 | 2181      | 注册配置中心         |
| kafka              | 172\.20\.0\.7 | 192\.168\.80\.130 | 9092      | 消息中间件，提供发布订阅功能 |
| kafka\-manage      | 172\.20\.0\.8 | 192\.168\.80\.130 | 9000      | kafka界面化管理     |

**备注：** 由于需要整合springboot程序，所以elasticsearch必须是集群形式，至少需要两台，否则程序启动会报错

## 3.2 创建网络

```
$ docker network create --driver=bridge --subnet=172.20.0.1/16 elk-kafka-network
```

## 3.3 elasticsearch

### 3.3.1 elasticsearch配置文件

```
$ vim es-one.yml

cluster.name: elasticsearch-cluster
node.name: es-node-one
network.bind_host: 0.0.0.0
network.publish_host: 172.20.0.2
http.port: 9200
transport.tcp.port: 9300
http.cors.enabled: true
http.cors.allow-origin: "*"
node.master: true
node.data: true
discovery.zen.ping.unicast.hosts: ["172.20.0.2:9300","172.20.0.3:9303"]
discovery.zen.minimum_master_nodes: 1
```

```
$ vim es-two.yml


cluster.name: elasticsearch-cluster
node.name: es-node-two
network.bind_host: 0.0.0.0
network.publish_host: 172.20.0.3
http.port: 9202
transport.tcp.port: 9303
http.cors.enabled: true
http.cors.allow-origin: "*"
node.master: true
node.data: true
discovery.zen.ping.unicast.hosts: ["172.20.0.2:9300","172.20.0.3:9303"]
discovery.zen.minimum_master_nodes: 1
```

### 3.3.2 docker-compose配置文件

```
$ vim elasticsearch-one.yml

version: '3'
services:
  elasticsearch-one:
    image: elasticsearch
    container_name: elasticsearch-one
    restart: always
    networks:
      default:
        ipv4_address: 172.20.0.2
    ports:
      - 9200:9200
      - 9300:9300
    volumes:
      - ./es-one.yml:/usr/share/elasticsearch/config/elasticsearch.yml
      - ./plugins-one:/usr/share/elasticsearch/plugins

networks:
  default:
    external:
      name: elk-kafka-network
```

```
$ vim elasticsearch-one.yml

version: '3'
services:
  elasticsearch-two:
    image: elasticsearch
    container_name: elasticsearch-two
    restart: always
    networks:
      default:
        ipv4_address: 172.20.0.3
    ports:
      - 9202:9202
      - 9303:9303
    volumes:
      - ./es-two.yml:/usr/share/elasticsearch/config/elasticsearch.yml
      - ./plugins-two:/usr/share/elasticsearch/plugins

networks:
  default:
    external:
      name: elk-kafka-network
```

### 3.3.3 构建脚本

```
$ vim build-one.sh

docker-compose -f elasticsearch-one.yml stop

docker-compose -f elasticsearch-one.yml rm --force

docker-compose -f elasticsearch-one.yml up -d
```

```
$ vim build-two.sh

docker-compose -f elasticsearch-two.yml stop

docker-compose -f elasticsearch-two.yml rm --force

docker-compose -f elasticsearch-two.yml up -d
```

执行构建脚本

```
$ chmod +x build-one.sh build-two.sh && ./build-one.sh && ./build-two.sh
```

## 3.4 logstash

### 3.4.1 订阅kafka数据来源配置文件

编辑logstash_kafka.conf

```
input {
  kafka {
    bootstrap_servers => "192.168.80.130:9092"
    # topics为kafka订阅主题名
    topics => ["my_log"]
  }
}
output {
    stdout { codec => rubydebug }
    elasticsearch {
       hosts => ["192.168.80.130:9200","192.168.80.130:9202"]
       index => "my_log"
    }
}
```

### 3.4.2 docker-compose配置文件

编辑logstash.yml

```
version: '3'
services:
  logstash:
    image: logstash
    container_name: logstash
    restart: always
    networks:
      default:
        ipv4_address: 172.20.0.4
    ports:
      - 5044:5044
      - 4560:4560
      - 8080:8080
    volumes:
      - ./data:/data
      - ./config:/config
      - ./logs/tomcat.logs:/tomcat.logs
      - ./patterns:/opt/logstash/patterns
      - ./mysql/mysql-connector-java-5.1.46.jar:/mysql-connector-java-5.1.46.jar
    external_links:
      - elasticsearch:elasticsearch
    command: bash -c "chmod +x /data && logstash -f logstash_kafka.conf --path.data=/data"

networks:
  default:
    external:
      name: elk-kafka-network
```

### 3.4.3 构建脚本

编辑build.sh

```
docker-compose -f logstash.yml stop

docker-compose -f logstash.yml rm --force

docker-compose -f logstash.yml up -d
```

执行构建脚本

```
$ chmod +x build.sh && ./build.sh
```

## 3.5 kibana

### 3.5.1 docker-compose配置文件

编辑kibana.yml

```
version: '3'
services:
  kibana:
    image: kibana
    container_name: kibana
    restart: always
    networks:
      default:
        ipv4_address: 172.20.0.5
    environment:
      - ELASTICSEARCH_URL=http://172.20.0.2:9200
    ports:
      - 5601:5601
    external_links:
      - elasticsearch-one:elasticsearch-one
      - elasticsearch-two:elasticsearch-two

networks:
  default:
    external:
      name: elk-kafka-network
```

### 3.5.2 构建脚本

编辑build.sh

```
docker-compose -f kibana.yml stop

docker-compose -f kibana.yml rm --force

docker-compose -f kibana.yml up -d
```

执行构建脚本

```
$ chmod +x build.sh && ./build.sh
```

## 3.6 zookeeper

### 3.6.1 docker-compose配置文件

编辑zookeeper.yml

```
version: '3'
services:
  zookeeper:
    image: wurstmeister/zookeeper
    container_name: zookeeper
    restart: always
    networks:
      default:
        ipv4_address: 172.20.0.6
    ports:
      - 2181:2181

networks:
  default:
    external:
      name: elk-kafka-network
```

### 3.6.2 构建脚本

编辑build.sh

```
docker-compose -f zookeeper.yml stop

docker-compose -f zookeeper.yml rm --force

docker-compose -f zookeeper.yml up -d
```

执行构建脚本

```
$ chmod +x build.sh && ./build.sh
```

## 3.7 kafka

### 3.7.1 docker-compose配置文件

编辑kafka.yml

```
version: '3'
services:
  kafka:
    image: wurstmeister/kafka
    container_name: kafka
    restart: always
    environment:
      #- KAFKA_BROKER_ID=0
      #- KAFKA_ZOOKEEPER_CONNECT=192.168.80.130:2181
      #- KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://192.168.80.130:9092
      #- KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://192.168.80.130:9092
      KAFKA_ZOOKEEPER_CONNECT: 192.168.80.130:2181
      KAFKA_LISTENERS: PLAINTEXT://0.0.0.0:9092
      KAFKA_DELETE_TOPIC_ENABLE: "true"
      KAFKA_BROKER_ID: 0
    networks:
      default:
        ipv4_address: 172.20.0.7
    ports:
      - 9092:9092
    external_links:
      - zookeeper:zookeeper

networks:
  default:
    external:
      name: elk-kafka-network
```

### 3.7.2 构建脚本

编辑build.sh

```
docker-compose -f kafka.yml stop

docker-compose -f kafka.yml rm --force

docker-compose -f kafka.yml up -d
```

执行构建脚本

```
$ chmod +x build.sh && ./build.sh
```

## 3.8 kafka-manager

### 3.8.1 docker-compose配置文件

编辑kafka-manager.yml

```
version: '3'
services:
  kafka-manager:
    image: sheepkiller/kafka-manager
    container_name: kafka-manager
    restart: always
    environment:
      #- ZK_HOSTS=172.20.0.6:2181
      ZK_HOSTS: 172.20.0.6:2181
    networks:
      default:
        ipv4_address: 172.20.0.8
    ports:
      - 9000:9000

networks:
  default:
    external:
      name: elk-kafka-network

```

### 3.8.2 构建脚本

编辑build-manager.sh

```
docker-compose -f kafka-manager.yml stop

docker-compose -f kafka-manager.yml rm --force

docker-compose -f kafka-manager.yml up -d
```

执行构建脚本

```
$ chmod +x build-manager.sh && ./build-manager.sh
```

## 3.9 搭建部署完成

1. 查看docker容器状态是否正常

![](http://image.focusprogram.top/20200511143815.png)

2. 查看elasticsearch集群状态是否正常

```
$ curl http://localhost:9200 && curl http://localhost:9202
```

![](http://image.focusprogram.top/20200511142251.png)

测试集群效果：

[http://192.168.80.130:9200/_cat/nodes?pretty](http://192.168.80.130:9200/_cat/nodes?pretty)

![](http://image.focusprogram.top/20200511142457.png)

3. kafka需要添加指定主题my_log

```
$ docker exec -it kafka /bin/bash

# 创建my_log topic
/opt/kafka/bin/kafka-topics.sh --create --zookeeper 192.168.80.130:2181 --replication-factor 1 --partitions 1 --topic my_log

# 查询创建的主题
/opt/kafka/bin/kafka-topics.sh --list --zookeeper 192.168.80.130:2181
```

# 4. springboot整合elk+kafka

## 4.1 elasticsearch查询模块

引入maven依赖

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
    <version>2.2.6.RELEASE</version>
</dependency>
<dependency>
    <groupId>com.querydsl</groupId>
    <artifactId>querydsl-apt</artifactId>
</dependency>
<dependency>
    <groupId>com.querydsl</groupId>
    <artifactId>querydsl-jpa</artifactId>
</dependency>
```

添加实体类ProductEntity

```
@Document(indexName = "product", type = "product")
@Data
public class ProductEntity {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 类型ID
     */
    private Integer categoryId;
    /**
     * 名称
     */
    private String name;
    /**
     * 小标题
     */
    private String subtitle;
    /**
     * 主图像
     */
    private String mainImage;
    /**
     * 小标题图像
     */
    private String subImages;
    /**
     * 描述
     */
    private String detail;
    /**
     * 商品规格
     */
    private String attributeList;
    /**
     * 价格
     */
    private Double price;
    /**
     * 库存
     */
    private Integer stock;
    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新时间
     */
    private MappingMetaData.Timestamp updatedTime;
}
```

添加es查询接口ProductReposiory

```
public interface ProductReposiory extends ElasticsearchRepository<ProductEntity, Long> {
}
```

添加查询接口以及实现

```
public interface ProductSearchService {

    List<ProductEntity> search(String name, @PageableDefault(page = 0, value = 10) Pageable pageable);

}

@Service
public class ProductSearchServiceImpl implements ProductSearchService {

    @Autowired
    private ProductReposiory productReposiory;

    @Override
    public List<ProductEntity> search(String name, @PageableDefault(page = 0, value = 10) Pageable pageable) {
//        String user = null;
//        System.out.println(user.getBytes());
        // 1.拼接查询条件
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        // 2.模糊查询 name、 subtitle、detail含有 搜索关键字
        builder.must(QueryBuilders.multiMatchQuery(name, "name", "subtitle", "detail"));
        // 3.调用ES接口查询
        Page<ProductEntity> page = productReposiory.search(builder, pageable);
        // 4.获取集合数据
        List<ProductEntity> content = page.getContent();
        return content;
    }

}
```

添加访问层ProductController

```
@RestController
@RequestMapping("elk")
public class ProductController {

    @Autowired
    private ProductSearchService productSearchService;

    @Cache(key = "product")
    @PostMapping("search")
    public List<ProductEntity> search(@RequestParam("name") String name) {
        Pageable pageable = PageRequest.of(0, 10);
        return productSearchService.search(name, pageable);
    }
}
```

添加配置文件application.yml

```
server:
  port: 9000

spring:
  application:
    name: elk-es
  data:
    elasticsearch:
      # 指定elasticsearch集群地址
      cluster-name: elasticsearch-cluster
      cluster-nodes: 192.168.80.130:9300
  kafka:
    bootstrap-servers: 192.168.80.130:9092

  redis:
    host: 114.55.34.44
    port: 6379
    password: root
    timeout: 2000
    jedis:
      pool:
        maxActive: 300
        maxIdle: 100
        maxWait: 1000
    # 服务或应用名
    sysName: admin
    enable: true
    database: 0

kafka:
  topic: my_log
```

## 4.2 kafka日志收集模块

引入maven依赖

```
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
    <version>2.1.5.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjrt</artifactId>
    <version>1.8.6</version>
</dependency>
```

添加kafka推送消息类KafkaSender

```
@Component
@Slf4j
public class KafkaSender<T> {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic}")
    public String kafkaTopic;

    /**
     * kafka 发送消息
     *
     * @param obj 消息对象
     */
    public void send(T obj) {
        String jsonObj = JSON.toJSONString(obj);
        log.info("------------ message = {}", jsonObj);

        // 发送消息
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(kafkaTopic, jsonObj);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.info("Produce: The message failed to be sent:" + throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                // TODO 业务处理
                log.info("Produce: The message was sent successfully:");
                log.info("Produce: _+_+_+_+_+_+_+ result: " + stringObjectSendResult.toString());
            }
        });
    }

}
```

添加AOP拦截方法请求日志收集类AopLogAspect和全局错误日志收集类GlobalExceptionHandler

```
@Aspect
@Slf4j
@Component
public class AopLogAspect {

    @Autowired
    private KafkaSender<JSONObject> kafkaSender;

    // 申明一个切点 里面是 execution表达式
    @Pointcut("execution(* com.elk.*.controller.*.*(..))")
    private void serviceAspect() {
    }

    /**
     * 基于注解形式拦截API请求
     *
     * @param joinPoint
     */
    @Before(value = "@annotation(com.elk.elkkafka.annotation.MonitorRequest)")
    public void doBefore(JoinPoint joinPoint) {
        //获取到请求的属性
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //获取到请求对象
        HttpServletRequest request = attributes.getRequest();
        //URL：根据请求对象拿到访问的地址
        log.info("url=" + request.getRequestURL());
        //获取请求的方法，是Get还是Post请求
        log.info("method=" + request.getMethod());
        //ip：获取到访问
        log.info("ip=" + request.getRemoteAddr());
        //获取被拦截的类名和方法名
        log.info("class=" + joinPoint.getSignature().getDeclaringTypeName() +
                "and method name=" + joinPoint.getSignature().getName());
        //参数
        log.info("参数=" + joinPoint.getArgs().toString());
    }

    /**
     * 匹配API拦截
     *
     * @param joinPoint
     */
    @Before(value = "serviceAspect()")
    public void methodBefore(JoinPoint joinPoint) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        // 打印请求内容
        log.info("===============请求内容===============");
        log.info("请求地址:" + request.getRequestURL().toString());
        log.info("请求方式:" + request.getMethod());
        log.info("请求类方法:" + joinPoint.getSignature());
        log.info("请求类方法参数:" + Arrays.toString(joinPoint.getArgs()));
        log.info("===============请求内容===============");

        JSONObject jsonObject = new JSONObject();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
        jsonObject.put("request_time", df.format(new Date()));
        jsonObject.put("request_url", request.getRequestURL().toString());
        jsonObject.put("request_method", request.getMethod());
        jsonObject.put("signature", joinPoint.getSignature());
        jsonObject.put("request_args", Arrays.toString(joinPoint.getArgs()));
        try {
            jsonObject.put("request_ip", WebToolUtils.getLocalIP());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        JSONObject requestJsonObject = new JSONObject();
        requestJsonObject.put("request", jsonObject);
        kafkaSender.send(requestJsonObject);
    }

    // 在方法执行完结后打印返回内容
    @AfterReturning(returning = "o", pointcut = "serviceAspect()")
    public void methodAfterReturing(Object o) {

        log.info("--------------返回内容----------------");
        log.info("Response内容:" + o.toString());
        log.info("--------------返回内容----------------");
        JSONObject respJSONObject = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
        jsonObject.put("response_time", df.format(new Date()));
        jsonObject.put("response_content", JSONObject.toJSONString(o));
        respJSONObject.put("response", jsonObject);
        kafkaSender.send(respJSONObject);
    }

}
```

```
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Autowired
    private KafkaSender<JSONObject> kafkaSender;

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public JSONObject exceptionHandler(Exception e) {
        log.info("###全局捕获异常###,error:{}", e);

        // 1.封装异常日志信息
        JSONObject errorJson = new JSONObject();
        JSONObject logJson = new JSONObject();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
        logJson.put("request_time", df.format(new Date()));
        logJson.put("error_info", e);

        errorJson.put("request_error", logJson);
        kafkaSender.send(errorJson);
        // 2. 返回错误信息
        JSONObject result = new JSONObject();
        result.put("code", 500);
        result.put("msg", "系统错误");

        // 参数中记录下，IP和端口号
        return result;
    }

}
```

## 4.3 测试日志收集

```
$ curl http://localhost:9000/elk/search?name=苹果

[
    {
        "id": 1,
        "categoryId": null,
        "name": "Pad平板电脑无线局域网",
        "subtitle": "Pad平板电脑",
        "mainImage": null,
        "subImages": null,
        "detail": "官方授权Pad苹果电脑",
        "attributeList": null,
        "price": null,
        "stock": null,
        "status": 0,
        "createdBy": null,
        "createdTime": null,
        "updatedTime": null
    }
]
```

查询kibana，发现数据已经同步：

![](http://image.focusprogram.top/20200511154946.png)

![](http://image.focusprogram.top/20200511155151.png)








</font>