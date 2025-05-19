package com.example.bms.battery.application.service;

import com.example.bms.battery.common.dto.BatteryDto;

import java.util.List;

/**
 * @author cz
 * @date 2025/5/19 00:34
 */
public interface BatteryService {
    BatteryDto addBattery(BatteryDto batteryDto);

    void removeBattery(Integer vid);
    public void scanSignals();
    void modifyBattery(BatteryDto batteryDto);

    BatteryDto getBattery(Integer carId);

    List<BatteryDto> listBatterys();
}
