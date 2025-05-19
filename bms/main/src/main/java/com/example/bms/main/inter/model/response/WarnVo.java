package com.example.bms.main.inter.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class WarnVo {

    private Long carId;

    private String batteryType;

    private String warnName;

    private String warnLevel;

    public WarnVo(Long carId, String batteryType, String warnName, String warnLevel) {
        this.carId = carId;
        this.batteryType = batteryType;
        this.warnName = warnName;
        this.warnLevel = warnLevel;
    }

    public WarnVo() {
    }
}
