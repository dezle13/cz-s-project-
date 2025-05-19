package com.example.bms.main.common.dto;

import lombok.Data;

@Data
public class CarDto {

    private String vid;
    private Long carId;
    private String batteryType;
    private Long totalMileage;
    private Integer batteryStatus;


}
