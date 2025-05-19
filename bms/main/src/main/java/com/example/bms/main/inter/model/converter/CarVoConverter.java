package com.example.bms.main.inter.model.converter;


import com.example.bms.main.common.dto.CarDto;
import com.example.bms.main.inter.model.request.CarReq;
import com.example.bms.main.inter.model.response.CarVo;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface CarVoConverter {

    CarDto toDto(CarReq carReq);

    CarVo toVo(CarDto carDto);


}
