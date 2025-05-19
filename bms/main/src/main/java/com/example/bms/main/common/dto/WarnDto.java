package com.example.bms.main.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author bench
 * @Date 2024/06/16 21:44
 **/

@Data
public class WarnDto {

    private Long carId;

    private String batteryType;

    private String warnName;

    private Integer warnLevel;

    public WarnDto() {
    }

    public WarnDto(Long carId, String batteryType, String warnName, Integer warnLevel) {
        this.carId = carId;
        this.batteryType = batteryType;
        this.warnName = warnName;
        this.warnLevel = warnLevel;
    }
}
