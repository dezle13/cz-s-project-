package com.example.bms.battery.infrastructure.conventer;


import com.example.bms.battery.common.dto.BatteryDto;
import com.example.bms.battery.infrastructure.pojo.BatteryPojo;
import org.mapstruct.Mapper;

/**
 * @author cz
 * @date 2025/5/18 13:36
 */
@Mapper(componentModel = "spring")
public interface BatteryPojoConverter {

    BatteryDto toDto(BatteryPojo batteryPojo);

    BatteryPojo toPojo(BatteryDto batteryDto);

}
