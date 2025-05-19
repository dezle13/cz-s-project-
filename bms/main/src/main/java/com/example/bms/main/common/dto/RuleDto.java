package com.example.bms.main.common.dto;

import lombok.Data;

import java.util.Map;



@Data
public class RuleDto {
    private Long id;
    private Integer warnId;
    private String warnName;
    private String batteryType;
    private Map<Double, Integer> warnRule;

}
