package com.example.bms.main.application.service.impl;

import com.example.bms.main.application.service.WarnService;
import com.example.bms.main.common.dto.SignalDto;
import com.example.bms.main.common.dto.WarnDto;
import com.example.bms.main.domain.service.CarDomainService;
import com.example.bms.main.domain.service.RuleDomainService;
import com.example.bms.main.domain.service.WarnDomainService;
import com.example.bms.main.inter.model.converter.WarnVoConverter;
import com.example.bms.main.inter.model.request.WarnReq;
import com.example.bms.main.inter.model.response.WarnVo;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @author cz
 * @date 2025/5/19 04:25
 */

@Component
@RocketMQMessageListener(
        topic = "batteryTopic",         // 你的 topic 名
        selectorExpression = "signal",  // tag，和生产者保持一致
        consumerGroup = "battery-group" // 消费者组名，必须唯一
)
public class SignalConsumerListener implements RocketMQListener<List<WarnReq>> {
    @Autowired
    private WarnVoConverter warnVoConverter;
    @Resource
    private WarnService warnService;


    @Override
    public void onMessage(List<WarnReq> message) {
        // 处理接收到的消息
        long start = System.currentTimeMillis();

//        List<SignalDto> signalDtoList = message.parallelStream().map(warnVoConverter::toDto).collect(Collectors.toList());
//
//        List<WarnDto> warnDtoList =  warnService.processSignalDtoList(signalDtoList);
        // 将 signalDtoList 拆分为并行流处理
//        List<WarnDto> warnDtoList = signalDtoList.parallelStream()
//                .map(warnService::processSignalDto)
//                .collect(Collectors.toList());
//        List<WarnVo> warnVoList = warnDtoList.parallelStream().map(warnVoConverter::toVo).collect(Collectors.toList());
        List<WarnDto> warnDtoList = message.parallelStream().map(warnVoConverter::toDto)
                .flatMap(x -> warnService.processSignalDtoList(x).stream()).collect(Collectors.toList());
        long end = System.currentTimeMillis();
        long duration = end - start;

        System.out.println("耗时：" + duration + " ms");

    }


}
