package com.wzx;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * File Description.
 *
 * @author Jesse
 * @version 1.0
 * @since <pre>2020/8/13</pre>
 */
@EnableScheduling
@EnableKnife4j
@EnableDiscoveryClient
@SpringBootApplication
public class VueblogApplication {

    public static void main(String[] args) {
        SpringApplication.run(VueblogApplication.class, args);
    }

}
