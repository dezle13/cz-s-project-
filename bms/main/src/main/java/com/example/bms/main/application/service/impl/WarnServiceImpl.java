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

    @Override
    public List<WarnDto> processSignalDtoList(List<SignalDto> signalRequestDtoList) {

        List<SignalDto> signalDtoList = new ArrayList<>();
        List<WarnDto> warnDtoList = new ArrayList<>();

        for(SignalDto signalDto : signalRequestDtoList){
            //处理为warnId为空值的Signal
            if(signalDto.getWarnId() == null){
                List<Map<String,Double>> subSignals = splitMapByTwo(signalDto.getSignal());
                for(int warnNameInex = 1;warnNameInex <=2 ;warnNameInex++){
                    SignalDto signalDtoSetWarnName = new SignalDto(signalDto.getCarId(),
                            warnNameInex,
                            subSignals.get(warnNameInex-1));
                    signalDtoList.add(signalDtoSetWarnName);
                }
            }else{
                signalDtoList.add(signalDto);
            }}
        ObjectMapper objectMapper = new ObjectMapper();
        //获取规则，1：warnId + BatteryType -> WarnRule 这一部分可以使用缓存
        for(SignalDto signalDto: signalDtoList){

            String UUID2 = String.valueOf("CarId:"+ signalDto.getCarId());
            String UUID3 = String.valueOf("WarnId:"+signalDto.getWarnId());
            String batteryType;
            if(redisTemplate.hasKey(UUID2)){
                batteryType = (String) redisTemplate.opsForValue().get(UUID2);
            }else {
                batteryType = carDomainService.getBatteryTypeByCarId(signalDto.getCarId());
                redisTemplate.opsForValue().set(UUID2, batteryType);
            }
            String UUID1 = String.valueOf("batteryTypeAndWarnId:"+ batteryType+signalDto.getWarnId());
            TreeMap<Double,Integer>  warnRule = new TreeMap<>();
            if(redisTemplate.hasKey(UUID1)){
                String jsonStr = (String) redisTemplate.opsForValue().get(UUID1);
                try {
                    warnRule = objectMapper.readValue(jsonStr, new TypeReference<TreeMap<Double, Integer>>() {});
                } catch (Exception e) {
                    throw new RuntimeException("Failed to parse JSON", e);
                }
            }else {
                warnRule = ruleDomainService
                        .getWarnRuleByWarnIdAndBatT(signalDto.getWarnId(),
                                batteryType
                        );
                // 存储数据到 Redis
                try {
                    String jsonStr = objectMapper.writeValueAsString(warnRule);
                    redisTemplate.opsForValue().set(UUID1, jsonStr);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }


            String WarnName;
            if(redisTemplate.hasKey(UUID3)){
                WarnName = (String) redisTemplate.opsForValue().get(UUID3);
            }else {
                WarnName = ruleDomainService.getWarnNameByWarnId(signalDto.getWarnId());
                redisTemplate.opsForValue().set(UUID3 ,WarnName);
            }


            warnDtoList.add(warnDomainService.processSignal(signalDto.getCarId(),
                    batteryType,WarnName,
                    signalDto.getSignal(),warnRule));
        }

//        warnDomainService.saveWarn(warnDtoList);

        return warnDtoList;
    }

//    public static TreeMap<Double,Integer> getTreeFromString(String value){try {
//        ObjectMapper objectMapper = new ObjectMapper();
//        return objectMapper.readValue(value, new TypeReference<TreeMap<Double, Integer>>() {});
//    } catch (
//            IOException e) {
//        throw new RuntimeException("Failed to convert String to Map<String, Double>", e);
//    }}


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
