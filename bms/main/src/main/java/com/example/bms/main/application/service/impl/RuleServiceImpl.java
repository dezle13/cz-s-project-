package com.example.bms.main.application.service.impl;

import com.example.bms.main.application.service.RuleService;
import com.example.bms.main.common.dto.RuleDto;
import com.example.bms.main.domain.service.RuleDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cz
 * @date 2025/5/18 16:04
 */
@Service
public class RuleServiceImpl implements RuleService {


    @Autowired
    private RuleDomainService ruleDomainService;

    @Override
    public RuleDto addRule(RuleDto ruleDto) {
        return ruleDomainService.addRule(ruleDto);
    }

    @Override
    public void modifyRule(RuleDto ruleDto) {
        ruleDomainService.modifyRule(ruleDto);
    }

    @Override
    public void removeRule(Long id) {
        ruleDomainService.removeRule(id);
    }


    @Override
    public List<RuleDto> getRuleById(Long id) {
        return ruleDomainService.findById(id);
    }

    @Override
    public List<RuleDto> listRules() {
        return ruleDomainService.findById(null);
    }
}
