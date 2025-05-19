package com.example.bms.main.domain.service.Impl;

import com.example.bms.main.common.dto.WarnDto;
import com.example.bms.main.domain.service.WarnDomainService;
import com.example.bms.main.infrastructure.conventer.WarnPojoConverter;
import com.example.bms.main.infrastructure.pojo.WarnPojo;
import com.example.bms.main.infrastructure.repository.WarnRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author cz
 * @date 2025/5/18 18:31
 */
@Service
public class WarnDomainServiceImpl implements WarnDomainService {

    @Resource
    private WarnRepository warnRepository;
    @Resource
    private WarnPojoConverter warnPojoConverter;

    @Override
    @Transactional
    public void saveWarn(List<WarnDto> warnDtoList) {
        List<WarnPojo> warnPojoList = warnDtoList.stream().
                map(warnPojoConverter::toPojo).collect(Collectors.toList());
        warnRepository.add(warnPojoList);
    }

    @Override
    public WarnDto processSignal(Long carId, String batteryType, String warnName, Map<String, Double> signal, TreeMap<Double, Integer> warnRule) {
        WarnDto warnDto = new WarnDto();
        warnDto.setCarId(carId);
        warnDto.setBatteryType(batteryType);
        warnDto.setWarnName(warnName);
        //处理预警信号和预警等级之间的关系
        double diffValue = 0d;
        //计算绝对差
        if(signal.containsKey("Mx")){
            diffValue =  signal.get("Mx") - signal.get("Mi");
        }else {
            diffValue = signal.get("Ix") - signal.get("Ii");
        }
        warnDto.setWarnLevel(getWarnLevel(diffValue,warnRule));

        return warnDto;
    }

    @Override
    @Transactional
    public List<WarnDto> getByCarId(Integer carId) {
        List<WarnPojo> warnPojoList = warnRepository.getByCarId(carId);
        List<WarnDto> result = warnPojoList.stream().map(warnPojoConverter::toDto).collect(Collectors.toList());
        return result;
    }


    public static int getWarnLevel(double diffValue, TreeMap<Double, Integer> warnRule){
        // 从末尾往前遍历键
        for (Double key : warnRule.descendingKeySet()) {
            if(diffValue >= key){
                return warnRule.get(key);
            }
        }
        return -1;
    }



}
