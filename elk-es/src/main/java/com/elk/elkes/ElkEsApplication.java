package com.elk.elkes;

import com.qywk.cache.EnableQywkCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@EnableQywkCache
@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = {"com.elk.elkes"})
public class ElkEsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElkEsApplication.class, args);
    }

}
