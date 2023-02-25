package me.sibyl.mq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Classname Application
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/02/19 21:44
 */

@SpringBootApplication
@EnableAsync
@EnableCaching
public class MqConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MqConsumerApplication.class, args);
    }
}
