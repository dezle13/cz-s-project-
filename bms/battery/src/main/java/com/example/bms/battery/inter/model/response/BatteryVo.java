package com.example.bms.battery.inter.model.response;

import lombok.Data;


@Data
public class BatteryVo {
    private Integer carId;
    private Integer warnId;
    private String signal;
}
