package com.example.bms.main.inter.model.request;


import com.example.bms.main.common.ValidationGroup.InsertGroup;
import com.example.bms.main.common.ValidationGroup.RemoveGroup;
import com.example.bms.main.common.ValidationGroup.SearchGroup;
import com.example.bms.main.common.ValidationGroup.UpdateGroup;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;



@Data
public class CarReq {

    @NotBlank(message = "汽车vid不能为空", groups = {RemoveGroup.class, UpdateGroup.class, SearchGroup.class})
    private String vid;

    @NotNull(message = "车架编号不能为空", groups = {InsertGroup.class, UpdateGroup.class})
    private Long carId;

    @NotBlank(message = "电池类型不能为空", groups = {InsertGroup.class, UpdateGroup.class})
    private String batteryType;

    @NotNull(message = "汽车总里程不能为空", groups = {InsertGroup.class, UpdateGroup.class})
    private Long totalMileage;

    @NotNull(message = "电池状态不能为空", groups = {InsertGroup.class, UpdateGroup.class})
    private Integer batteryStatus;

    public CarReq() {
    }
}
