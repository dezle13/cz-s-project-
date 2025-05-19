package com.example.bms.battery.inter.web;


import com.example.bms.battery.application.service.BatteryService;
import com.example.bms.battery.common.ValidationGroup.*;
import com.example.bms.battery.common.dto.BatteryDto;
import com.example.bms.battery.inter.model.converter.BatteryVoConverter;
import com.example.bms.battery.inter.model.request.BatteryReq;
import com.example.bms.battery.inter.model.response.BatteryVo;
import com.example.bms.battery.inter.model.response.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/api")
public class BatteryController {

    @Autowired
    private BatteryService batteryService;
    @Autowired
    private BatteryVoConverter batteryVoConverter;

    @PostMapping("/battery/add")
    public ResponseMessage add(@Validated(InsertGroup.class) @RequestBody BatteryReq batteryReq){
        log.info("request add battery: {}.", batteryReq);
        BatteryDto batteryDto = batteryVoConverter.toDto(batteryReq);
        batteryDto = batteryService.addBattery(batteryDto);
        return ResponseMessage.success(batteryVoConverter.toVo(batteryDto));
    }

    @DeleteMapping("/battery/delete/{id}")
    public ResponseMessage delete(@PathVariable(value = "id") Integer id){
        log.info("request delete battery by vid: {}.", id);
        batteryService.removeBattery(id);
        return ResponseMessage.success();
    }

    @PutMapping("/battery/modify")
    public ResponseMessage modify(@Validated(UpdateGroup.class) @RequestBody BatteryReq batteryReq){
        log.info("request modify battery: {}", batteryReq);
        BatteryDto batteryDto = batteryVoConverter.toDto(batteryReq);
        batteryService.modifyBattery(batteryDto);
        return ResponseMessage.success(batteryVoConverter.toVo(batteryDto));
    }

    @GetMapping("/battery/find/{carId}")
    public ResponseMessage findBattery(@PathVariable("carId") Integer carId) {
        log.info("request find battery:{}，{} ", carId);
        BatteryDto batteryDto = batteryService.getBattery(carId);
        log.info("request find user:{}. response", batteryDto);
        return ResponseMessage.success(batteryVoConverter.toVo(batteryDto));
    }

    @GetMapping("/battery")
    public ResponseMessage listAllBattery() {
        log.info("request find battery:{}. ");
        List<BatteryDto> batteryDtoList = batteryService.listBatterys();
        log.info("request find user:{}. response");
        List<BatteryVo> batteryVoList = batteryDtoList.stream()
                .map(batteryVoConverter::toVo)
                .collect(Collectors.toList());
        return ResponseMessage.success(batteryVoList);
    }

    @GetMapping("/battery/submit")
    public ResponseMessage findBattery() {
        batteryService.scanSignals();
        return ResponseMessage.success();
    }
}
