package com.arno.tech.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


/**
 * 项目启动类
 *
 * @author xuxin14
 * @date 2023/02/02
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ArnoSpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArnoSpringApplication.class, args);
    }

}
