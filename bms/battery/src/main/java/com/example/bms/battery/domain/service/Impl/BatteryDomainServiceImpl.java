package com.example.bms.battery.domain.service.Impl;

import com.example.bms.battery.common.dto.BatteryDto;
import com.example.bms.battery.common.exception.DomainException;
import com.example.bms.battery.domain.service.BatteryDomainService;
import com.example.bms.battery.infrastructure.conventer.BatteryPojoConverter;
import com.example.bms.battery.infrastructure.pojo.BatteryPojo;
import com.example.bms.battery.infrastructure.repository.BatteryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author cz
 * @date 2025/5/19 00:36
 */
@Service
public class BatteryDomainServiceImpl implements BatteryDomainService {


    @Autowired
    private BatteryRepository batteryRepository;

    @Autowired
    private BatteryPojoConverter batteryPojoConverter;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BatteryDto addBattery(BatteryDto batteryDto) {
        BatteryPojo batteryPojo = batteryPojoConverter.toPojo(batteryDto);
        batteryRepository.add(batteryPojo);
        return batteryDto;
    }

    @Override
    public void modifyBattery(BatteryDto batteryDto) {
        BatteryPojo batteryPojo = batteryPojoConverter.toPojo(batteryDto);
        if (batteryRepository.update(batteryPojo) == 0){
            throw new DomainException("no car_id");
        }
    }

    @Override
    public void removeBattery(Integer carId) {
        if (batteryRepository.remove(carId) == 0){
            throw new DomainException("no battery");
        }
    }

    @Override
    public List<BatteryDto> findById(Integer carId) {
        List<BatteryPojo> batteryPojoList = batteryRepository.findById(carId);
        return batteryPojoList.stream().map(batteryPojoConverter::toDto).collect(Collectors.toList());
    }


}
