package com.example.bms.main.inter.web;


import com.example.bms.main.application.service.CarService;
import com.example.bms.main.common.ValidationGroup.InsertGroup;
import com.example.bms.main.common.ValidationGroup.UpdateGroup;
import com.example.bms.main.common.dto.CarDto;
import com.example.bms.main.inter.model.converter.CarVoConverter;
import com.example.bms.main.inter.model.request.CarReq;
import com.example.bms.main.inter.model.response.CarVo;
import com.example.bms.main.inter.model.response.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/api")
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    private CarVoConverter carVoConverter;

    @PostMapping("/car/add")
    public ResponseMessage add(@Validated(InsertGroup.class) @RequestBody CarReq carReq){
        log.info("request add car: {}.", carReq);
        CarDto carDto = carVoConverter.toDto(carReq);
        carDto = carService.addCar(carDto);
        return ResponseMessage.success(carVoConverter.toVo(carDto));
    }

    @DeleteMapping("/car/delete/{vid}")
    public ResponseMessage delete(@PathVariable(value = "vid") String vid){
        log.info("request delete car by vid: {}.", vid);
        carService.removeCar(vid);
        return ResponseMessage.success();
    }

    @PutMapping("/car/modify")
    public ResponseMessage modify(@Validated(UpdateGroup.class) @RequestBody CarReq carReq){
        log.info("request modify car: {}", carReq);
        CarDto carDto = carVoConverter.toDto(carReq);
        carService.modifyCar(carDto);
        return ResponseMessage.success(carVoConverter.toVo(carDto));
    }

    @GetMapping("/car/find/{vid}")
    public ResponseMessage findCar(@PathVariable(value = "vid") String vid) {
        log.info("request find car:{}. ", vid);
        List<CarDto> carDtoList = carService.getCar(vid);
        log.info("request find user:{}. response", carDtoList);
        List<CarVo> carVoList = carDtoList.stream()
                .map(carVoConverter::toVo)
                .collect(Collectors.toList());
        return ResponseMessage.success(carVoList);
    }

    @GetMapping("/car")
    public ResponseMessage listAllCar() {
        log.info("request find car:{}. ");
        List<CarDto> carDtoList = carService.listCars();
        log.info("request find user:{}. response");
        List<CarVo> carVoList = carDtoList.stream()
                .map(carVoConverter::toVo)
                .collect(Collectors.toList());
        return ResponseMessage.success(carVoList);
    }
}
