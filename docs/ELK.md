<font size=4.5>

**ELK分布式日志收集分析**

---

- **文章目录**

[toc]

# 1. 什么是ELK?

> ELK 是elastic公司提供的一套完整的日志收集以及展示的解决方案，是三个产品的首字母缩写，分别是ElasticSearch、Logstash 和 Kibana。
>
> [ElasticSearch](https://github.com/elastic/elasticsearch) 简称ES，它是一个实时的分布式搜索和分析引擎，它可以用于全文搜索，结构化搜索以及分析。它是一个建立在全文搜索引擎 Apache Lucene 基础上的搜索引擎，使用 Java 语言编写。
>
> [Logstash](https://github.com/elastic/logstash) 是一个具有实时传输能力的数据收集引擎，用来进行数据收集（如：读取文本文件）、解析，并将数据发送给ES。
>
> [Kibana](https://github.com/elastic/kibana) 为 Elasticsearch 提供了分析和可视化的 Web 平台。它可以在 Elasticsearch 的索引中查找，交互数据，并生成各种维度表格、图形。

# 2. ELK用途？

> 传统意义上，ELK是作为替代Splunk的一个开源解决方案。Splunk 是日志分析领域的领导者。日志分析并不仅仅包括系统产生的错误日志，异常，也包括业务逻辑，或者任何文本类的分析。而基于日志的分析，能够在其上产生非常多的解决方案，譬如：
>
> 1.问题排查。我们常说，运维和开发这一辈子无非就是和问题在战斗，所以这个说起来很朴实的四个字，其实是沉甸甸的。很多公司其实不缺钱，就要稳定，而要稳定，就要运维和开发能够快速的定位问题，甚至防微杜渐，把问题杀死在摇篮里。日志分析技术显然问题排查的基石。基于日志做问题排查，还有一个很帅的技术，叫全链路追踪，比如阿里的eagleeye
或者Google的dapper，也算是日志分析技术里的一种。
>
> 2.监控和预警。 日志，监控，预警是相辅相成的。基于日志的监控，预警使得运维有自己的机械战队，大大节省人力以及延长运维的寿命。
>
> 3.关联事件。多个数据源产生的日志进行联动分析，通过某种分析算法，就能够解决生活中各个问题。比如金融里的风险欺诈等。这个可以可以应用到无数领域了，取决于你的想象力。
>
> 4.数据分析。 这个对于数据分析师，还有算法工程师都是有所裨益的。

# 3. ELK架构设计

![](http://image.focusprogram.top/elk.png)

# 4. ELK搭建部署

## 4.1 环境准备

### 4.1.1 所需配置

| 服务名                 | Docker ip地址   | 宿主机ip地址           | 开放端口      | 功能                |
|---------------------|---------------|-------------------|-----------|-------------------|
| elasticsearch       | 172\.20\.0\.2 | 192\.168\.80\.130 | 9200、9300 | 搜索                |
| elasticsearch\-head | 172\.20\.0\.3 | 192\.168\.80\.130 | 9100      | elasticsearch界面管理 |
| logstash            | 172\.20\.0\.4 | 192\.168\.80\.130 | 5044      | 日志收集              |
| kibana              | 172\.20\.0\.5 | 192\.168\.80\.130 | 5061      | 展示、监控             |

### 4.1.2 创建网络
```
$ docker network create --driver=bridge --subnet=172.30.0.1/16 elk-network
```

## 4.2 elasticsearch

> Elasticsearch 是一个分布式可扩展的实时搜索和分析引擎,一个建立在全文搜索引擎 Apache Lucene(TM) 基础上的搜索引擎.当然 Elasticsearch 并不仅仅是 Lucene 那么简单，它不仅包括了全文搜索功能，还可以进行以下工作:
>
> 分布式实时文件存储，并将每一个字段都编入索引，使其可以被搜索。
实时分析的分布式搜索引擎。
可以扩展到上百台服务器，处理PB级别的结构化或非结构化数据。

### 4.2.1 elasticsearch配置文件

编辑配置文件es.yml

```
cluster.name: elasticsearch
node.name: es
network.bind_host: 0.0.0.0
network.publish_host: 172.30.0.2
http.port: 9200
transport.tcp.port: 9300
http.cors.enabled: true
http.cors.allow-origin: "*"
node.master: true
node.data: true
discovery.zen.ping.unicast.hosts: ["172.30.0.2:9300"]
discovery.zen.minimum_master_nodes: 1
```

### 4.2.2 docker-compose配置文件

编辑docker-compose配置文件elasticsearch.yml

```
version: '3'
services:
  elasticsearch:
    image: elasticsearch
    container_name: elasticsearch
    restart: always
    networks:
      default:
        ipv4_address: 172.30.0.2
    ports:
      - 9200:9200
      - 9300:9300
    volumes:
      - ./es.yml:/usr/share/elasticsearch/config/elasticsearch.yml
      # 指定Ik分词器和拼音分词器可以支持中文和拼音搜索
      - ./plugins:/usr/share/elasticsearch/plugins

  elasticsearch-head:
    image: mobz/elasticsearch-head:5
    container_name: elasticsearch-head
    restart: always
    networks:
      default:
        ipv4_address: 172.30.0.3
    ports:
      - 9100:9100
    links:
      - elasticsearch:elasticsearch

networks:
  default:
    external:
      name: elk-network
```

### 4.2.3 构建脚本

编辑构建脚本build.sh

```
docker-compose -f elasticsearch.yml stop

docker-compose -f elasticsearch.yml rm --force

docker-compose -f elasticsearch.yml up -d
```

执行构建脚本

```
$ chmod +x build.sh && ./build.sh
```

构建成功显示如下：

![](http://image.focusprogram.top/20200511112646.png)

测试是否成功启动

```
$ curl http://localhost:9200
```

发现无法访问，查看日志，报错信息如下：

```
max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]
```

解决方法如下：

1. 临时生效，重启服务器后实效

```
执行命令：

sysctl -w vm.max_map_count=262144

查看结果：

sysctl -a|grep vm.max_map_count

显示：

vm.max_map_count = 262144
```

2. 永久有效

```
$ vim /etc/sysctl.conf

vm.max_map_count=262144
```

修改重新编译后，再次访问，显示如下，则启动成功：

```
$ curl http://localhost:9200

{
  "name" : "es",
  "cluster_name" : "elasticsearch",
  "cluster_uuid" : "uDlzhbhgT-6B9Zv_KGD29Q",
  "version" : {
    "number" : "5.6.12",
    "build_hash" : "cfe3d9f",
    "build_date" : "2018-09-10T20:12:43.732Z",
    "build_snapshot" : false,
    "lucene_version" : "6.6.1"
  },
  "tagline" : "You Know, for Search"
}
```

访问 [http://192.168.80.130:9100/](http://192.168.80.130:9100/),显示如下，则说明elasticsearch-head启动成功

![](http://image.focusprogram.top/20200511113534.png)

**注意事项：**

- 1. 修改vm.max_map_count参数，否则无法正常启动
- 2. plugins中ik分词器插件版本要与elasticsearch版本保持一致，否则无法正常使用中文分词

### 4.3.4 elasticsearch常用命令

1. 查询全部

```
GET _search
{
  "query": {
    "match_all": {}
  }
}
```

2. 查询索引以及索引映射类型

```
GET user/_search

GET user/_mapping

GET product/_search

GET product/_mapping
```

3. 删除索引

```
DELETE user

DELETE product
```

4. 重新指定文档类型映射IK分词类型

```
POST /product/_mapping/product
{
      "product": {
        "properties": {
          "@timestamp": {
            "type": "date"
          },
          "@version": {
            "type": "text",
            "fields": {
              "keyword": {
                "type": "keyword",
                "ignore_above": 256
              }
            }
          },
          "attribute_list": {
            "type": "text",
            "fields": {
              "keyword": {
                "type": "keyword",
                "ignore_above": 256
              }
            }
          },
          "category_id": {
            "type": "long"
          },
          "created_time": {
            "type": "date"
          },
          "detail": {
            "type": "text",
             "analyzer":"ik_smart",
            "search_analyzer":"ik_smart"

          },
          "id": {
            "type": "long"
          },
          "main_image": {
            "type": "text",
            "fields": {
              "keyword": {
                "type": "keyword",
                "ignore_above": 256
              }
            }
          },
          "name": {
            "type": "text",
            "analyzer":"ik_smart",
            "search_analyzer":"ik_smart"

          },
          "revision": {
            "type": "long"
          },
          "status": {
            "type": "long"
          },
          "sub_images": {
            "type": "text",
            "fields": {
              "keyword": {
                "type": "keyword",
                "ignore_above": 256
              }
            }
          },
          "subtitle": {
            "type": "text",
          "analyzer":"ik_smart",
         "search_analyzer":"ik_smart"

          },
          "updated_time": {
            "type": "date"
          }
        }
      }
}
```

5. 添加自定义拼音和IK分词插件

```
PUT /product
{
   "settings": {
        "analysis": {
            "analyzer": {
                "ik_smart_pinyin": {
                    "type": "custom",
                    "tokenizer": "ik_smart",
                    "filter": ["my_pinyin", "word_delimiter"]
                },
                "ik_max_word_pinyin": {
                    "type": "custom",
                    "tokenizer": "ik_max_word",
                    "filter": ["my_pinyin", "word_delimiter"]
                }
            },
            "filter": {
                "my_pinyin": {
                    "type" : "pinyin",
                    "keep_separate_first_letter" : true,
                    "keep_full_pinyin" : true,
                    "keep_original" : true,
                    "limit_first_letter_length" : 16,
                    "lowercase" : true,
                    "remove_duplicated_term" : true 
                }
            }
        }
  }
  
}

# 重新指定文档类型映射自定义分词类型
POST /product/_mapping/product
{
  "product": {
	"properties": {
	  "@timestamp": {
		"type": "date"
	  },
	  "@version": {
		"type": "text",
		"fields": {
		  "keyword": {
			"type": "keyword",
			"ignore_above": 256
		  }
		}
	  },
	  "attribute_list": {
		"type": "text",
		"fields": {
		  "keyword": {
			"type": "keyword",
			"ignore_above": 256
		  }
		}
	  },
	  "category_id": {
		"type": "long"
	  },
	  "created_time": {
		"type": "date"
	  },
	  "detail": {
		"type": "text",
		 "analyzer":"ik_smart_pinyin",
		"search_analyzer":"ik_smart_pinyin"

	  },
	  "id": {
		"type": "long"
	  },
	  "main_image": {
		"type": "text",
		"fields": {
		  "keyword": {
			"type": "keyword",
			"ignore_above": 256
		  }
		}
	  },
	  "name": {
		"type": "text",
		"analyzer":"ik_smart_pinyin",
		"search_analyzer":"ik_smart_pinyin"

	  },
	  "revision": {
		"type": "long"
	  },
	  "status": {
		"type": "long"
	  },
	  "sub_images": {
		"type": "text",
		"fields": {
		  "keyword": {
			"type": "keyword",
			"ignore_above": 256
		  }
		}
	  },
	  "subtitle": {
		"type": "text",
	  "analyzer":"ik_smart",
	 "search_analyzer":"ik_smart"

	  },
	  "updated_time": {
		"type": "date"
	  }
	}
  }
}
```

## 4.3 logstash

### 4.3.1 处理过程

![](http://image.focusprogram.top/20200511114553.png)

如上图，Logstash的数据处理过程主要包括：Inputs, Filters, Outputs 三部分， 另外在Inputs和Outputs中可以使用Codecs对数据格式进行处理。这四个部分均以插件形式存在，用户通过定义pipeline配置文件，设置需要使用的input，filter，output, codec插件，以实现特定的数据采集，数据处理，数据输出等功能  

- **Inputs**：用于从数据源获取数据，常见的插件如file, syslog, redis, beats 等[详细参考](https://www.elastic.co/guide/en/logstash/5.6/input-plugins.html)
- **Filters**：用于处理数据如格式转换，数据派生等，常见的插件如grok, mutate, drop,  clone, geoip等[详细参考](https://www.elastic.co/guide/en/logstash/5.6/output-plugins.html)  
- **Outputs**：用于数据输出，常见的插件如elastcisearch，file, graphite, statsd等[详细参考](https://www.elastic.co/guide/en/logstash/5.6/filter-plugins.html)
- **Codecs**：Codecs不是一个单独的流程，而是在输入和输出等插件中用于数据转换的模块，用于对数据进行编码处理，常见的插件如json，multiline[详细参考](https://www.elastic.co/guide/en/logstash/5.6/codec-plugins.html)

### 4.3.2 执行模型

- 每个Input启动一个线程，从对应数据源获取数据  
- Input会将数据写入一个队列：默认为内存中的有界队列（意外停止会导致数据丢失）。为了防止数丢失Logstash提供了两个特性：
    - Persistent Queues：通过磁盘上的queue来防止数据丢失
    - Dead Letter Queues：保存无法处理的event（仅支持Elasticsearch作为输出源）
- Logstash会有多个pipeline worker, 每一个pipeline worker会从队列中取一批数据，然后执行filter和output（worker数目及每次处理的数据量均由配置确定）

### 4.3.3 收集数据来源

#### 4.3.3.1 kafka数据来源

编辑logstash_kafka.conf

```
input {
  kafka {
    bootstrap_servers => "192.168.80.130:9092"
    topics => ["my_log"]
  }
}
output {
    stdout { codec => rubydebug }
    elasticsearch {
       hosts => ["192.168.80.130:9200"]
       index => "my_log"
    }
}
```

#### 4.3.3.2 tomcat数据来源

编辑logstash_tomcat.conf

```
input {
    # 从文件读取日志信息 输送到控制台
    file {
        path => "/tomcat.logs"
        codec => "json" ## 以JSON格式读取日志
        type => "elasticsearch"
        start_position => "beginning"
    }
}

# filter {
#
# }

output {
    # 标准输出
    # stdout {}
    # 输出进行格式化，采用Ruby库来解析日志
     stdout { codec => rubydebug }
         elasticsearch {
        hosts => ["192.168.80.130:9200"]
        index => "tomcat-%{+YYYY.MM.dd}"
    }
}
```

#### 4.3.3.3 mysql数据来源

编辑mysql_tables.conf

```
input {
  jdbc {
    jdbc_driver_library => "/mysql-connector-java-5.1.46.jar"
    jdbc_driver_class => "com.mysql.jdbc.Driver"
    jdbc_connection_string => "jdbc:mysql://114.55.34.44:3306/elk"
    jdbc_user => "root"
    jdbc_password => "root"
    schedule => "* * * * *"
    statement => "SELECT * FROM user WHERE update_time >= :sql_last_value"
    use_column_value => true
    tracking_column_type => "timestamp"
    tracking_column => "update_time"
    last_run_metadata_path => "syncpoint_table"
    type => "user"
  }

  jdbc {
    jdbc_driver_library => "/mysql-connector-java-5.1.46.jar"
    jdbc_driver_class => "com.mysql.jdbc.Driver"
    jdbc_connection_string => "jdbc:mysql://114.55.34.44:3306/goods"
    jdbc_user => "root"
    jdbc_password => "root"
    schedule => "* * * * *"
    statement => "SELECT * FROM product WHERE updated_time >= :sql_last_value"
    use_column_value => true
    tracking_column_type => "timestamp"
    tracking_column => "update_time"
    last_run_metadata_path => "syncpoint_table"
    type => "product"
  }

}

output {
    
    if [type] == "user"{     	
       elasticsearch { 
        # ES的IP地址及端口
        hosts => ["192.168.80.130:9200"]
        # 索引名称 可自定义
        index => "user" 
        # 需要关联的数据库中有有一个id字段，对应类型中的id
        document_id => "%{id}"
        document_type => "user"
       }
    }

    if [type] == "product"{
       elasticsearch {  
        # ES的IP地址及端口
        hosts => ["192.168.80.130:9200"]
        # 索引名称 可自定义
        index => "product" 
        # 需要关联的数据库中有有一个id字段，对应类型中的id
        document_id => "%{id}"
        document_type => "product"
       }
    }
    
    stdout {
        # JSON格式输出
        codec => json_lines
    }
}
```

配置文件说明

```
jdbc_driver_library: jdbc mysql 驱动的路径
jdbc_driver_class: 驱动类的名字，mysql 填 com.mysql.jdbc.Driver 就好了
jdbc_connection_string: mysql 地址
jdbc_user: mysql 用户
jdbc_password: mysql 密码
schedule: 执行 sql 时机，类似 crontab 的调度
statement: 要执行的 sql，以 “:” 开头是定义的变量，可以通过 parameters 来设置变量，这里的 sql_last_value 是内置的变量，表示上一次 sql 执行中 update_time 的值，这里 update_time 条件是 >= 因为时间有可能相等，没有等号可能会漏掉一些增量
use_column_value: 使用递增列的值
tracking_column_type: 递增字段的类型，numeric 表示数值类型, timestamp 表示时间戳类型
tracking_column: 递增字段的名称，这里使用 update_time 这一列，这列的类型是 timestamp
last_run_metadata_path: 同步点文件，这个文件记录了上次的同步点，重启时会读取这个文件，这个文件可以手动修改
```

```
logstash-input-jdbc原理:

使用 logstash-input-jdbc 插件读取 mysql 的数据，这个插件的工作原理比较简单，就是定时执行一个 sql，然后将 sql 执行的结果写入到流中，增量获取的方式没有通过 binlog 方式同步，而是用一个递增字段作为条件去查询，每次都记录当前查询的位置，由于递增的特性，只需要查询比当前大的记录即可获取这段时间内的全部增量，一般的递增字段有两种，AUTO_INCREMENT 的主键 id 和 ON UPDATE CURRENT_TIMESTAMP 的 update_time 字段，id 字段只适用于那种只有插入没有更新的表，update_time 更加通用一些，建议在 mysql 表设计的时候都增加一个 update_time 字段
```

### 4.3.4 docker-compose配置文件

编辑docker-compose配置文件logstash.yml

```
version: '3'
services:
  logstash:
    image: logstash
    container_name: logstash
    restart: always
    networks:
      default:
        ipv4_address: 172.30.0.4
    ports:
      - 5044:5044
      - 4560:4560
      - 8080:8080
    volumes:
      - ./data:/data
      - ./config:/config
      - ./patterns:/opt/logstash/patterns
      - ./logs/tomcat.logs:/logs/tomcat.logs
      - ./java/mysql-connector-java-5.1.46.jar:/mysql-connector-java-5.1.46.jar
    external_links:
      - elasticsearch:elasticsearch
    command: bash -c "chmod +x /data && logstash -f /config/mysql_tables.conf --path.data=/data"

networks:
  default:
    external:
      name: elk-network
```

**注意事项：**

- logstash -f /config/ 会启动config文件夹下全部需要同步数据来源的配置文件，但是需要在input中指定type进行不同来源的区分，在output进行区分输出

- logstash -f /config/mysql_tables.conf 指定启动某个数据来源的配置文件

### 4.3.5 构建脚本

编辑构建脚本build.sh

```
docker-compose -f logstash.yml stop

docker-compose -f logstash.yml rm --force

docker-compose -f logstash.yml up -d
```

执行构建脚本

```
$ chmod +x build.sh && ./build.sh
```

查看启动日志，发现数据库两个表数据已经成功同步,每当数据库指定表中插入数据时，数据会在设定的同步时间内同步到elasticsearch中。目前设置的同步策略为一分钟同步一次

![](http://image.focusprogram.top/20200511125914.png)

## 4.4 kibana

> Kibana 是一款开源的数据分析和可视化平台，它是 Elastic Stack 成员之一，设计用于和 Elasticsearch 协作。您可以使用 Kibana 对 Elasticsearch 索引中的数据进行搜索、查看、交互操作。您可以很方便的利用图表、表格及地图对数据进行多元化的分析和呈现。
>
> Kibana 可以使大数据通俗易懂。它很简单，基于浏览器的界面便于您快速创建和分享动态数据仪表板来追踪 Elasticsearch 的实时数据变化。

### 4.4.1 docker-compose配置文件

编辑kibana.yml配置文件

```
version: '3'
services:
  kibana:
    image: kibana
    container_name: kibana
    restart: always
    networks:
      default:
        ipv4_address: 172.30.0.5
    environment:
      - ELASTICSEARCH_URL=http://172.30.0.2:9200
    ports:
      - 5601:5601
    external_links:
      - elasticsearch:elasticsearch

networks:
  default:
    external:
      name: elk-network
```

### 4.4.2 构建脚本

```
docker-compose -f kibana.yml stop

docker-compose -f kibana.yml rm --force

docker-compose -f kibana.yml up -d
```

执行构建脚本

```
$ chmod +x build.sh && ./build.sh
```

成功构建，显示如下：

![](http://image.focusprogram.top/20200511130308.png)

### 4.4.3 访问Kibana

[http://192.168.80.130:5601/status](http://192.168.80.130:5601/status)

![](http://image.focusprogram.top/20200511133846.png)

[http://192.168.80.130:5601/](http://192.168.80.130:5601/)

![](http://image.focusprogram.top/20200511130538.png)

创建索引user、product

![](http://image.focusprogram.top/20200511132114.png)

![](http://image.focusprogram.top/20200511132218.png)

</font>