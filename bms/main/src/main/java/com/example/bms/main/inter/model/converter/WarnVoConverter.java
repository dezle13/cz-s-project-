package com.example.bms.main.inter.model.converter;


import com.example.bms.main.common.dto.SignalDto;
import com.example.bms.main.common.dto.WarnDto;
import com.example.bms.main.inter.model.request.WarnReq;
import com.example.bms.main.inter.model.response.WarnVo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.io.IOException;
import java.util.Map;


@Mapper(componentModel = "spring")
public interface WarnVoConverter {

    @Mapping(source = "signal", target = "signal", qualifiedByName = "stringToMap")
    SignalDto toDto(WarnReq warnReq);

    @Mapping(source = "warnLevel", target = "warnLevel", qualifiedByName = "mapToWarnLevel")
    WarnVo toVo(WarnDto warnDto);

    /**
     * 保证递增排序
     * @param value
     * @return
     */
    @Named("stringToMap")
    default Map<String, Double> stringToMap(String value) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(value, new TypeReference<Map<String, Double>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert String to Map<String, Double>", e);
        }
    }

    @Named("mapToWarnLevel")
    default String mapToWarnLevel(Integer warnLevel) {
        if (warnLevel == -1) {
            return "不报警";
        } else {
            return warnLevel.toString();
        }
    }
}
