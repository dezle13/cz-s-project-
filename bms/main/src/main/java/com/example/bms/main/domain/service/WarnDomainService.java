package com.example.bms.main.domain.service;

import com.example.bms.main.common.dto.WarnDto;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author cz
 * @date 2025/5/18 18:31
 */
public interface WarnDomainService {


    void saveWarn(List<WarnDto> warnDtoList);


    WarnDto processSignal(Long carId, String batteryType, String warnName, Map<String, Double> signal, TreeMap<Double, Integer> warnRule);

    List<WarnDto> getByCarId(Integer carId);
}
