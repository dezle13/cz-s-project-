package com.example.bms.main.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 *
 */
@Data
@NoArgsConstructor
public class SignalDto {
    private Long carId;
    private Integer warnId;
    private Map<String, Double> signal;

    public SignalDto(Long carId, int warnId, Map<String, Double> signal) {
        this.carId = carId;
        this.warnId = warnId;
        this.signal = signal;;
    }


}
