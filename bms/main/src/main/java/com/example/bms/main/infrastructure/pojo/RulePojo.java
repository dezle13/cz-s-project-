package com.example.bms.main.infrastructure.pojo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author cz
 * @date 2025/5/18 16:15
 */
@Data
@Entity
public class RulePojo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Integer warnId;
    private String warnName;
    private String batteryType;
    private String warnRule;
}
