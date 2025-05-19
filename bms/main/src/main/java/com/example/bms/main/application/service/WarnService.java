package com.example.bms.main.application.service;

import com.example.bms.main.common.dto.SignalDto;
import com.example.bms.main.common.dto.WarnDto;

import java.util.List;

/**
 * @author cz
 * @date 2025/5/18 17:46
 */
public interface WarnService {
    List<WarnDto> processSignalDtoList(List<SignalDto> signalDtoList);

    List<WarnDto> getByCarId(Integer carId);
}
