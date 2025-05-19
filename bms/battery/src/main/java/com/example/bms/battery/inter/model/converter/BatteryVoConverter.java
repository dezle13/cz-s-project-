package com.example.bms.battery.inter.model.converter;



import com.example.bms.battery.common.dto.BatteryDto;
import com.example.bms.battery.inter.model.request.BatteryReq;
import com.example.bms.battery.inter.model.response.BatteryVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.*;

import java.util.HashMap;
import java.util.Map;


@Mapper(componentModel = "spring")
public interface BatteryVoConverter {

    BatteryDto toDto(BatteryReq carReq);

    @Mappings({
            @Mapping(target = "carId", source = "carId"),
            @Mapping(target = "warnId", source = "warnId"),
            @Mapping(target = "signal", ignore = true)
    })
    BatteryVo toVo(BatteryDto carDto);
    @AfterMapping
    default void afterMapping(BatteryDto dto, @MappingTarget BatteryVo vo) {
        Map<String, Double> signalMap = new HashMap<>();
        if(dto.getIx() != null && dto.getIi() != null ) {
            signalMap.put("Ix", dto.getIx());
            signalMap.put("Ii", dto.getIi());
        }
        if(dto.getMx() != null && dto.getMi() != null ) {
            signalMap.put("Mx", dto.getMx());
            signalMap.put("Mi", dto.getMi());
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            vo.setSignal(objectMapper.writeValueAsString(signalMap));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 序列化失败", e);
        }
    }

}
