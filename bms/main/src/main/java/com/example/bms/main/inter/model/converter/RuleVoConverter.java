package com.example.bms.main.inter.model.converter;


import com.example.bms.main.common.dto.RuleDto;
import com.example.bms.main.inter.model.request.RuleReq;
import com.example.bms.main.inter.model.response.RuleVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;


@Mapper(componentModel = "spring")
public interface RuleVoConverter {

    @Mapping(source = "warnRule", target = "warnRule", qualifiedByName = "stringToSortedMap")
    RuleDto toDto(RuleReq ruleReq);

    @Mapping(source = "warnRule", target = "warnRule", qualifiedByName = "mapToString")
    RuleVo toVo(RuleDto ruleDto);

    @Named("stringToSortedMap")
    default Map<Double, Integer> stringToSortedMap(String value) {
        if (value == null || value.isEmpty()) {
            return Collections.emptyMap();
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(value, new TypeReference<TreeMap<Double, Integer>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert String to Map<String, Double>", e);
        }
    }

    @Named("mapToString")
    default String mapToString(Map<Double,Integer> map){
        if (map == null || map.isEmpty()) {
            return "{}"; // 返回空对象表示的 JSON 字符串
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert Map<Double, Integer> to String", e);
        }
    }

}
