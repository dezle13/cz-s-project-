package com.example.bms.main.inter.web;


import com.example.bms.main.application.service.WarnService;
import com.example.bms.main.common.dto.SignalDto;
import com.example.bms.main.common.dto.WarnDto;
import com.example.bms.main.inter.model.converter.WarnVoConverter;
import com.example.bms.main.inter.model.request.WarnReq;
import com.example.bms.main.inter.model.response.ResponseMessage;
import com.example.bms.main.inter.model.response.WarnVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController
@Validated
@RequestMapping("/api")
public class BMSController {


    @Autowired
    private WarnVoConverter warnVoConverter;

    @Autowired
    private WarnService warnService;

    @PostMapping("/warn")
    public ResponseMessage getWarnLevel(@Valid  @RequestBody List<WarnReq> warnReqList) {
        List<SignalDto> signalDtoList = warnReqList.parallelStream().map(warnVoConverter::toDto).collect(Collectors.toList());

        List<WarnDto> warnDtoList =  warnService.processSignalDtoList(signalDtoList);

        List<WarnVo> warnVoList = warnDtoList.parallelStream().map(warnVoConverter::toVo).collect(Collectors.toList());

        return ResponseMessage.success(warnVoList);
    }

    @GetMapping("/warn/get/{carId}")
    public ResponseMessage findCar(@PathVariable(value = "carId") Integer carId) {
        log.info("request find warn:{}. ", carId);
        List<WarnDto> warnDtoList = warnService.getByCarId(carId);
        log.info("request find user:{}. response", warnDtoList);
        List<WarnVo> WarnVoList = warnDtoList.stream()
                .map(warnVoConverter::toVo)
                .collect(Collectors.toList());
        return ResponseMessage.success(WarnVoList);
    }

}
