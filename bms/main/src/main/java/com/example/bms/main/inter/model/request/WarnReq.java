package com.example.bms.main.inter.model.request;


import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;



@Data
@Validated
public class WarnReq {

    @NotNull(message = "汽车id不能为空")
    private Long carId;

    private Integer warnId;

    @NotBlank(message = "信号id不能为空")
    private String signal;

    public WarnReq(Long l, int i, String signal1) {
        this.carId = l;
        this.signal = signal1;
        this.warnId =   i;
    }
    public WarnReq() {
    }

}
