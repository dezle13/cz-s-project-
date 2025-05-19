package com.example.bms.battery.application.service.impl;

import com.example.bms.battery.application.service.BatteryService;
import com.example.bms.battery.common.dto.BatteryDto;
import com.example.bms.battery.domain.service.BatteryDomainService;
import com.example.bms.battery.inter.model.request.BatteryReq;
import com.example.bms.battery.inter.model.response.BatteryVo;
import com.example.bms.battery.inter.model.response.WarnReq;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 在此处使用换粗
 * @author cz
 * @date 2025/5/19 00:34
 */
@Service
@Slf4j
@EnableScheduling
public class BatteryServiceImpl implements BatteryService {
    private static final String CACHE_NAME = "BatteryCache::";

    private final ObjectMapper objectMapper = new ObjectMapper();
    //调用DomainService
    @Resource
    private BatteryDomainService batteryDomainService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public BatteryDto addBattery(BatteryDto batteryDto) {
        validateFieldsByWarnId(batteryDto);
        return batteryDomainService.addBattery(batteryDto);
    }

    @Override
    public void modifyBattery(BatteryDto batteryDto) {
        //在Domain中处理了未能修改的情况，此处能进行下去就是可以成功
        batteryDomainService.modifyBattery(batteryDto);
        //手动实现Redis的修改工作
        String cacheKey = CACHE_NAME + batteryDto.getCarId();
        if (redisTemplate.hasKey(cacheKey)) {
            redisTemplate.opsForValue().set(cacheKey, batteryDto);
        }
    }

    @Override
    public void removeBattery(Integer carId) {
        batteryDomainService.removeBattery(carId);
        // 生成缓存 Key
        String cacheKey = CACHE_NAME + carId;
        // 从 Redis 中删除该 Key  失败就返回0
        redisTemplate.delete(cacheKey);
    }

    @Override
    @Cacheable(value = "BatteryCache:",key = "#carId")
    public BatteryDto getBattery(Integer carId) {
        List<BatteryDto> entities = batteryDomainService.findById(carId);

        // 检查是否为空
        if (entities == null || entities.isEmpty()) {
            throw new RuntimeException("No battery found for carId: " + carId);
        }

        // 检查是否返回多个记录
        if (entities.size() > 1) {
            throw new RuntimeException("Multiple batteries found for carId: " + carId);
        }

        // 转换为 DTO 并返回
        return  entities.get(0);
    }


//    @Scheduled(cron = "0 */1 * * * *")
    @Override
    public void scanSignals() {
        final int  BATCH_SIZE   = 1000;
        final String TOPIC        = "batteryTopic";
        final String TAG          = "signal";
        final String DESTINATION  = TOPIC + ":" + TAG;

        log.info("Batch send start: total 10000 msgs, batchSize={}", BATCH_SIZE);
        long start = System.currentTimeMillis();

        List<WarnReq> buffer = new ArrayList<>();
        ThreadLocalRandom rnd = ThreadLocalRandom.current();

        for (int i = 0; i < 1000; i++) {
            // 1) 随机 carId 1-3
            int carId = rnd.nextInt(1, 4);

            // 2) 随机 warnId ∈ {1,2,null}
            int pick = rnd.nextInt(0, 3);
            Integer warnId;
            if (pick == 0) {
                warnId = 1;
            } else if (pick == 1) {
                warnId = 2;
            } else {
                warnId = null;
            }

            // 3) 根据 warnId 生成 signal JSON
            Map<String, Double> sig = new LinkedHashMap<>();
            if (warnId == null || warnId == 1) {
                double mx = rnd.nextDouble(1.0, 20.0);
                double mi = rnd.nextDouble(0.0, mx);
                sig.put("Mx", Math.round(mx * 100.0) / 100.0);
                sig.put("Mi", Math.round(mi * 100.0) / 100.0);
            }
            if (warnId == null || warnId == 2) {
                double ii = rnd.nextDouble(0.0, 10.0);
                double ix = rnd.nextDouble(ii, ii + 10.0);
                sig.put("Ii", Math.round(ii * 100.0) / 100.0);
                sig.put("Ix", Math.round(ix * 100.0) / 100.0);
            }

            String signalJson;
            try {
                signalJson = objectMapper.writeValueAsString(sig);
            } catch (JsonProcessingException e) {
                // 如果序列化失败，跳过这条
                continue;
            }

            // 4) 构造并发送 VO
            WarnReq vo = new WarnReq();
            vo.setCarId((long) carId);
            vo.setWarnId(warnId);
            vo.setSignal(signalJson);

            buffer.add(vo);

            if (buffer.size() == BATCH_SIZE) {
                rocketMQTemplate.convertAndSend(DESTINATION, buffer); // 发送 List<WarnReq>
                buffer.clear();
            }
    }}

    //使用动态SQL
    @Override
    public List<BatteryDto> listBatterys() {
        return batteryDomainService.findById(null);
    }

    private static void validateFieldsByWarnId(BatteryDto req) {
        Integer warnId = req.getWarnId();
        if (warnId == null) {
            throw new IllegalArgumentException("warnId 不能为空");
        }

        switch (warnId) {
            case 1:
                if (req.getMx() == null || req.getMi() == null) {
                    throw new IllegalArgumentException("warnId=1 时，mx 和 mi 必须存在");
                }
                if (req.getIx() != null || req.getIi() != null) {
                    throw new IllegalArgumentException("warnId=1 时，ix 和 ii 不允许存在");
                }
                break;
            case 2:
                if (req.getIx() == null || req.getIi() == null) {
                    throw new IllegalArgumentException("warnId=2 时，ix 和 ii 必须存在");
                }
                if (req.getMx() != null || req.getMi() != null) {
                    throw new IllegalArgumentException("warnId=2 时，mx 和 mi 不允许存在");
                }
                break;
            default:
                throw new IllegalArgumentException("不支持的 warnId: " + warnId);
        }
    }
}
