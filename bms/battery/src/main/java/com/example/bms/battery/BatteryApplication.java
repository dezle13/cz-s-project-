package com.example.bms.battery;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BatteryApplication {

    public static void main(String[] args) {
        SpringApplication.run(BatteryApplication.class, args);
    }

}
