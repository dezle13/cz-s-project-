package com.example.bms.battery.inter.model.request;


import com.example.bms.battery.common.ValidationGroup.*;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class BatteryReq {
    @NotNull(message = "车架id不能为空", groups = {InsertGroup.class, UpdateGroup.class})
    private Integer carId;
    private Integer warnId;
    private Double mx;
    private Double mi;
    private Double ix;
    private Double ii;
}
