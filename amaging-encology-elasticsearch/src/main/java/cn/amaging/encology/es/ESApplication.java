package cn.amaging.encology.es;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * Created by DuQiyu on 2018/11/13 10:30.
 */
@SpringBootApplication
@EnableElasticsearchRepositories
public class ESApplication {

    public static void main(String[] args) {
        SpringApplication.run(ESApplication.class, args);
    }

}
