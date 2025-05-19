package com.example.bms.main.application.service;

import com.example.bms.main.common.dto.RuleDto;

import java.util.List;

/**
 * @author cz
 * @date 2025/5/18 16:03
 */
public interface RuleService {
    RuleDto addRule(RuleDto ruleDto);

    void removeRule(Long id);

    void modifyRule(RuleDto ruleDto);

    List<RuleDto> getRuleById(Long id);

    List<RuleDto> listRules();
}
