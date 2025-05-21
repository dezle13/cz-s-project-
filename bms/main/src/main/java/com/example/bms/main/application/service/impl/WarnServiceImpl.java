package com.example.bms.main.application.service.impl;

import com.example.bms.main.application.service.WarnService;
import com.example.bms.main.common.dto.SignalDto;
import com.example.bms.main.common.dto.WarnDto;
import com.example.bms.main.domain.service.CarDomainService;
import com.example.bms.main.domain.service.RuleDomainService;
import com.example.bms.main.domain.service.WarnDomainService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * signalDtoList包含待处理的数据，这时候应该将之匹配到map中，以Key分类处理
 * @author cz
 * @date 2025/5/18 17:46
 */
@Service
public class WarnServiceImpl implements WarnService {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private CarDomainService carDomainService;

    @Resource
    private RuleDomainService ruleDomainService;

    @Resource
    private WarnDomainService warnDomainService;

//    @Override
//    public List<WarnDto> processSignalDtoList(SignalDto signalRequestDto) {
//
//        List<SignalDto> signalDtoList = new ArrayList<>();
//        List<WarnDto> warnDtoList = new ArrayList<>();
//
//            //处理为warnId为空值的Signal
//        if(signalRequestDto.getWarnId() == null){
//            List<Map<String,Double>> subSignals = splitMapByTwo(signalRequestDto.getSignal());
//            for(int warnNameInex = 1;warnNameInex <=2 ;warnNameInex++){
//                SignalDto signalDtoSetWarnName = new SignalDto(signalRequestDto.getCarId(),
//                        warnNameInex,
//                        subSignals.get(warnNameInex-1));
//                signalDtoList.add(signalDtoSetWarnName);
//            }
//        }else{
//            signalDtoList.add(signalRequestDto);
//        }
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        //获取规则，1：warnId + BatteryType -> WarnRule 这一部分可以使用缓存
//        for(SignalDto signalDto: signalDtoList){
//            //使用UUID保存所需要的东西
//            String UUID = String.valueOf("CarIdAndWarnId:"+ signalDto.getCarId() +signalDto.getWarnId());
//
//            String batteryType;
//            TreeMap<Double,Integer>  warnRule = new TreeMap<>();
//            String warnName;
//            if(Boolean.TRUE.equals(redisTemplate.hasKey(UUID))){
//                batteryType = (String) redisTemplate.opsForHash().get(UUID,"batteryType");
//                String jsonStr = (String) redisTemplate.opsForHash().get(UUID,"warnRule");
//                try {
//                    warnRule = objectMapper.readValue(jsonStr, new TypeReference<TreeMap<Double, Integer>>() {});
//                } catch (Exception e) {
//                    throw new RuntimeException("Failed to parse JSON", e);
//                }
//                warnName = (String) redisTemplate.opsForHash().get(UUID,"warnName");
//
//            }else {
//                HashMap<String, Object> userMap = new HashMap<>();
//                //电池名称
//                batteryType = carDomainService.getBatteryTypeByCarId(signalDto.getCarId());
////                redisTemplate.opsForZSet().add(UUID,batteryType,1);
//                userMap.put("batteryType",batteryType);
//                //规则
//                warnRule = ruleDomainService
//                        .getWarnRuleByWarnIdAndBatT(signalDto.getWarnId(),
//                                batteryType
//                        );
//                try {
//                    String jsonStr = objectMapper.writeValueAsString(warnRule);
//                    userMap.put("warnRule",jsonStr);
////                    redisTemplate.opsForZSet().add(UUID,jsonStr,2);
//                } catch (JsonProcessingException e) {
//                    throw new RuntimeException(e);
//                }
//                //warn名称
//                warnName = ruleDomainService.getWarnNameByWarnId(signalDto.getWarnId());
////                redisTemplate.opsForZSet().add(UUID,warnName,3);
//
//                userMap.put("warnName",warnName);
//
//                redisTemplate.opsForHash().putAll(UUID,userMap);
//            }
//
//
//            warnDtoList.add(warnDomainService.processSignal(signalDto.getCarId(),
//                    batteryType,warnName,
//                    signalDto.getSignal(),warnRule));
//        }
//
////        warnDomainService.saveWarn(warnDtoList);
//        return warnDtoList;
//    }
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final TypeReference<TreeMap<Double, Integer>> WARN_RULE_TYPE =
            new TypeReference<TreeMap<Double, Integer>>() {};
    public List<WarnDto> processSignalDtoList(SignalDto signalRequestDto) {
        List<SignalDto> signalDtoList = new ArrayList<>();
//        long start1 = System.currentTimeMillis();
        if (signalRequestDto.getWarnId() == null) {
            List<Map<String, Double>> subSignals = splitMapByTwo(signalRequestDto.getSignal());
            for (int warnNameIndex = 1; warnNameIndex <= 2; warnNameIndex++) {
                SignalDto signalDtoSetWarnName = new SignalDto(signalRequestDto.getCarId(),
                        warnNameIndex,
                        subSignals.get(warnNameIndex - 1));
                signalDtoList.add(signalDtoSetWarnName);
            }
        } else {
            signalDtoList.add(signalRequestDto);
        }
//        long end1 = System.currentTimeMillis();
//        long duration = end1 - start1;
//        System.out.println("处理warnId =null耗时：" + duration + " ms");

        List<WarnDto> result =  signalDtoList.parallelStream()
                .map(signalDto -> processSignalDto(signalDto, objectMapper, redisTemplate, carDomainService, ruleDomainService, warnDomainService))
                .collect(Collectors.toList());
//        warnDomainService.saveWarn(result);
        // 并行处理 signalDtoList
        return result;
    }

    private WarnDto processSignalDto(SignalDto signalDto, ObjectMapper objectMapper, RedisTemplate redisTemplate,
                                     CarDomainService carDomainService, RuleDomainService ruleDomainService,
                                     WarnDomainService warnDomainService) {
        String UUID = "CarIdAndWarnId:" + signalDto.getCarId() + signalDto.getWarnId();
        String batteryType;
        TreeMap<Double, Integer> warnRule = new TreeMap<>();
        String warnName;
//        long start2 = System.currentTimeMillis();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(UUID))) {
            // 一次性获取所有需要的字段
            List values = redisTemplate.opsForHash().multiGet(
                    UUID,
                    Arrays.asList("batteryType", "warnRule", "warnName")
            );

            batteryType = (String) values.get(0);
            String jsonStr = (String) values.get(1);

            try {
                warnRule = objectMapper.readValue(jsonStr, new TypeReference<TreeMap<Double, Integer>>() {});
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse JSON", e);
            }

            warnName = (String) values.get(2);
        } else {
            HashMap<String, Object> userMap = new HashMap<>();
            batteryType = carDomainService.getBatteryTypeByCarId(signalDto.getCarId());
            userMap.put("batteryType", batteryType);
            warnRule = ruleDomainService.getWarnRuleByWarnIdAndBatT(signalDto.getWarnId(), batteryType);
            try {
                String jsonStr = objectMapper.writeValueAsString(warnRule);
                userMap.put("warnRule", jsonStr);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            warnName = ruleDomainService.getWarnNameByWarnId(signalDto.getWarnId());
            userMap.put("warnName", warnName);
            redisTemplate.opsForHash().putAll(UUID, userMap);
        }
//        long end2 = System.currentTimeMillis();
//        long duration = end2 - start2;
//        System.out.println("读取数据：" + duration + " ms");
        return warnDomainService.processSignal(signalDto.getCarId(), batteryType, warnName, signalDto.getSignal(), warnRule);
    }


    @Override
    public List<WarnDto> getByCarId(Integer carId) {

        return warnDomainService.getByCarId(carId);
    }

    /**
     * 将输入的 Map 按每两个键值对一组分割成多个子 Map。
     * @param originalMap 原始 Map（大小必须为偶数）
     * @return 子 Map 列表
     */
    public static <K, V> List<Map<K, V>> splitMapByTwo(Map<K, V> originalMap) {
        List<Map<K, V>> result = new ArrayList<>();
        if (originalMap == null || originalMap.isEmpty()) {
            return result;
        }

        // 将原始 Map 的 entrySet 转换为 List，保持顺序（如 LinkedHashMap 的顺序）
        List<Map.Entry<K, V>> entryList = new ArrayList<>(originalMap.entrySet());

        // 按顺序两两分组
        for (int i = 0; i < entryList.size(); i += 2) {
            Map<K, V> subMap = new LinkedHashMap<>(); // 保持顺序
            // 添加第一个元素
            Map.Entry<K, V> entry1 = entryList.get(i);
            subMap.put(entry1.getKey(), entry1.getValue());
            // 添加第二个元素（如果存在）
            if (i + 1 < entryList.size()) {
                Map.Entry<K, V> entry2 = entryList.get(i + 1);
                subMap.put(entry2.getKey(), entry2.getValue());
            }
            result.add(subMap);
        }

        return result;
    }


}
