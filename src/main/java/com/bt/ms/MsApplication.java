package com.bt.ms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.bt.ms.mapper")
public class MsApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsApplication.class, args);
        System.out.println("my job is starting");
    }
}
