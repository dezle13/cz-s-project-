package com.example.bms.main.infrastructure.pojo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author cz
 * @date 2025/5/18 13:23
 */

@Data
@Entity
public class CarPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String vid;

    private Long carId;
    private String batteryType;
    private Long totalMileage;
    private Integer batteryStatus;
}
