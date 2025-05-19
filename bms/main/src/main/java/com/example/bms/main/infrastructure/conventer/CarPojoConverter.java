package com.example.bms.main.infrastructure.conventer;

import com.example.bms.main.common.dto.CarDto;
import com.example.bms.main.infrastructure.pojo.CarPojo;
import org.mapstruct.Mapper;

/**
 * @author cz
 * @date 2025/5/18 13:36
 */
@Mapper(componentModel = "spring")
public interface CarPojoConverter {

    CarDto toDto(CarPojo carPojo);

    CarPojo toPojo(CarDto carDto);

}
