package com.ww;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@MapperScan(basePackages = {"com.ww.db.mapper"})
@SpringBootApplication
public class SpringbootJavaStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootJavaStudyApplication.class, args);
    }

}
