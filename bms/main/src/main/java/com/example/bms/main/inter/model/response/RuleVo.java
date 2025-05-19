package com.example.bms.main.inter.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;


@Data
public class RuleVo {
    private Long id;
    private Integer warnId;
    private String warnName;
    private String batteryType;
    private Map<Double, Integer> warnRule;

}
