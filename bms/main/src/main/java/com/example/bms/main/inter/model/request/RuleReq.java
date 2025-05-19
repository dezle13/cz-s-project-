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
@NoArgsConstructor
public class RuleReq {
    @NotNull(message = "序号id不能为空", groups = {RemoveGroup.class, UpdateGroup.class, SearchGroup.class})
    private Long id;
    @NotNull(message = "规则编号id不能为空", groups = {InsertGroup.class, UpdateGroup.class})
    private Integer warnId;
    @NotBlank(message = "预警名称不能为空", groups = {InsertGroup.class, UpdateGroup.class})
    private String warnName;
    @NotBlank(message = "电池类型不能为空", groups = {InsertGroup.class, UpdateGroup.class})
    private String batteryType;
    @NotBlank(message = "预警规则不能为空", groups = {InsertGroup.class, UpdateGroup.class})
    private String warnRule;
}
