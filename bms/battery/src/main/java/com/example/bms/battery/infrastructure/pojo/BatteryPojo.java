package com.example.bms.battery.infrastructure.pojo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author cz
 * @date 2025/5/19 00:37
 */
@Entity
@Data
public class BatteryPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private Integer carId;
    private Integer warnId;
    private Double mx;
    private Double mi;
    private Double ix;
    private Double ii;
}
