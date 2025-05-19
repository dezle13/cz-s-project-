package com.example.bms.battery.inter.model.response;


import lombok.Data;

@Data
public class WarnReq {
    private Long carId;
    private Integer warnId;
    private String signal;

}
