package com.example.bms.main.domain.service.Impl;

import com.example.bms.main.common.dto.RuleDto;
import com.example.bms.main.common.exception.DomainException;
import com.example.bms.main.domain.service.RuleDomainService;
import com.example.bms.main.infrastructure.conventer.RulePojoConverter;
import com.example.bms.main.infrastructure.pojo.RulePojo;
import com.example.bms.main.infrastructure.repository.RuleRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author cz
 * @date 2025/5/18 16:10
 */
@Service
public class RuleDomainServiceImpl implements RuleDomainService {
    @Autowired
    private RuleRepository ruleRespository;

    @Autowired
    private RulePojoConverter rulePojoConverter;

    @Override
    @Transactional
    public RuleDto addRule(RuleDto ruleDto) {
        RulePojo rulePojo = rulePojoConverter.toRulePojo(ruleDto);
        ruleRespository.add(rulePojo);
        return ruleDto;
    }

    @Override
    @Transactional
    public void modifyRule(RuleDto ruleDto) {
        RulePojo rulePojo = rulePojoConverter.toRulePojo(ruleDto);
        if (ruleRespository.update(rulePojo) == 0){
            throw new DomainException("no rule");
        }
    }

    @Override
    @Transactional
    public void removeRule(Long id) {
        if (ruleRespository.remove(id) == 0){
            throw new DomainException("no rule");
        }
    }

    @Override
    @Transactional
    public List<RuleDto> findById(Long id) {
        List<RulePojo> rulePojoList = ruleRespository.findById(id);
        return rulePojoList.stream().map(rulePojoConverter::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String getWarnNameByWarnId(Integer warnId) {
        return ruleRespository.getWarnNameByWarnId(warnId);
    }


    @Override
    @Transactional
    public TreeMap<Double, Integer> getWarnRuleByWarnIdAndBatT(Integer warnId, String batteryType) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(ruleRespository.getWarnRuleByWarnIdAndBatT(warnId,batteryType), new TypeReference<TreeMap<Double, Integer>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert String to Map<String, Double>", e);
        }
    }







}
