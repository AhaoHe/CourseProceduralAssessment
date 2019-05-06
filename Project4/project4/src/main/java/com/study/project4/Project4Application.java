package com.study.project4;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(value = "com.study.project4.com.dao")
@SpringBootApplication
public class Project4Application {

    public static void main(String[] args) {
        SpringApplication.run(Project4Application.class, args);
    }

}
