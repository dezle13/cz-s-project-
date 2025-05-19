package com.example.bms.battery.common.dto;

import lombok.Data;

/**
 * @author cz
 * @date 2025/5/18 23:52
 */
@Data
public class BatteryDto {
    private Integer carId;
    private Integer warnId;
    private Double mx;
    private Double mi;
    private Double ix;
    private Double ii;
}
