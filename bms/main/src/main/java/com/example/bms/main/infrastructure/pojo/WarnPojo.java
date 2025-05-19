package com.example.bms.main.infrastructure.pojo;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author cz
 * @date 2025/5/18 22:06
 */
@Data
@Entity
public class WarnPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Long carId;
    private String batteryType;
    private String warnName;
    private Integer warnLevel;
}
