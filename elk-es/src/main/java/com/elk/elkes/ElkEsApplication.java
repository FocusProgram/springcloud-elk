package com.elk.elkes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

//@EnableElk
//@EnableCache
@SpringBootApplication
@ComponentScan(basePackages = {"com.elk.elkes", "com.elk.elkkafka", "com.elk.elkcache"})
@EnableElasticsearchRepositories(basePackages = {"com.elk.elkes"})
public class ElkEsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElkEsApplication.class, args);
    }

}
