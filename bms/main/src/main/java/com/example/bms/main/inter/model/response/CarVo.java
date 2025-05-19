package com.example.bms.main.inter.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class CarVo{

    private String vid;
    private Long carId;
    private String batteryType;
    private Long totalMileage;
    private Integer batteryStatus;


    public CarVo(String vid, Long carId, String batteryType, Long totalMileage, Integer batteryStatus) {
        this.vid = vid;
        this.carId = carId;
        this.batteryType = batteryType;
        this.totalMileage = totalMileage;
        this.batteryStatus = batteryStatus;
    }

    public CarVo() {

    }
}
