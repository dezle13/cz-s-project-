package com.example.bms.main.domain.service;

import com.example.bms.main.common.dto.RuleDto;

import java.util.List;
import java.util.TreeMap;

/**
 * @author cz
 * @date 2025/5/18 16:10
 */
public interface RuleDomainService {
    RuleDto addRule(RuleDto ruleDto);

    void modifyRule(RuleDto ruleDto);

    void removeRule(Long id);

    List<RuleDto> findById(Long id);


    String getWarnNameByWarnId(Integer warnId);

    TreeMap<Double, Integer> getWarnRuleByWarnIdAndBatT(Integer warnId, String batteryType);
}
