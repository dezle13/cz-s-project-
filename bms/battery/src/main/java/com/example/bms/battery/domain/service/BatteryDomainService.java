package com.example.bms.battery.domain.service;

import com.example.bms.battery.common.dto.BatteryDto;

import java.util.List;

/**
 * @author cz
 * @date 2025/5/19 00:36
 */
public interface BatteryDomainService {
    BatteryDto addBattery(BatteryDto batteryDto);

    void modifyBattery(BatteryDto batteryDto);

    void removeBattery(Integer vid);

    List<BatteryDto> findById(Integer carId);
}
