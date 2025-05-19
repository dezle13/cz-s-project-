package com.example.bms.main.inter.web;


import com.example.bms.main.application.service.RuleService;
import com.example.bms.main.common.ValidationGroup.UpdateGroup;
import com.example.bms.main.common.dto.RuleDto;
import com.example.bms.main.inter.model.converter.RuleVoConverter;
import com.example.bms.main.inter.model.request.RuleReq;
import com.example.bms.main.inter.model.response.ResponseMessage;
import com.example.bms.main.inter.model.response.RuleVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**t
 * -1 -> 不报警
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class RuleController {

    @Autowired
    private RuleService RuleService;

    @Autowired
    private RuleVoConverter ruleVoConverter;

    @PostMapping("/rule/add")
    public ResponseMessage add(@RequestBody RuleReq ruleReq){
        log.info("request add rule: {}.", ruleReq);
        RuleDto ruleDto = ruleVoConverter.toDto(ruleReq);
        ruleDto = RuleService.addRule(ruleDto);
        return ResponseMessage.success(ruleVoConverter.toVo(ruleDto));
    }

    @DeleteMapping("/rule/delete/{id}")
    public ResponseMessage delete(@PathVariable(value = "id") Long id){
        log.info("request delete rule by id: {}.", id);
        RuleService.removeRule(id);
        return ResponseMessage.success();
    }

    @PutMapping("/rule/modify")
    public ResponseMessage modify(@Validated(UpdateGroup.class) @RequestBody RuleReq ruleReq){
        log.info("request modify rule: {}", ruleReq);
        RuleDto ruleDto = ruleVoConverter.toDto(ruleReq);
        RuleService.modifyRule(ruleDto);
        return ResponseMessage.success(ruleVoConverter.toVo(ruleDto));
    }

    @GetMapping("/rule/find/{id}")
    public ResponseMessage findCar(@PathVariable(value = "id") Long id) {
        log.info("request find rule:{}. ", id);
        List<RuleDto> ruleDtoList = RuleService.getRuleById(id);
        log.info("request find user:{}. response", ruleDtoList);
        List<RuleVo> ruleVoList = ruleDtoList.stream()
                .map(ruleVoConverter::toVo)
                .collect(Collectors.toList());
        return ResponseMessage.success(ruleVoList);
    }

    @GetMapping("/rule")
    public ResponseMessage listAllCar() {
        log.info("request find car:{}. ");
        List<RuleDto> ruleDtoList = RuleService.listRules();
        log.info("request find user:{}. response");
        List<RuleVo> ruleVoList = ruleDtoList.stream()
                .map(ruleVoConverter::toVo)
                .collect(Collectors.toList());
        return ResponseMessage.success(ruleVoList);
    }
}
