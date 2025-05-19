package com.example.bms.main;

import com.example.bms.main.application.service.RuleService;
import com.example.bms.main.common.ValidationGroup.UpdateGroup;
import com.example.bms.main.common.dto.RuleDto;
import com.example.bms.main.inter.model.converter.RuleVoConverter;
import com.example.bms.main.inter.model.request.RuleReq;
import com.example.bms.main.inter.model.response.ResponseMessage;
import com.example.bms.main.inter.model.response.RuleVo;
import com.example.bms.main.inter.web.RuleController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RuleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RuleService ruleService;

    @Mock
    private RuleVoConverter ruleVoConverter;

    @InjectMocks
    private RuleController ruleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(ruleController).build();
    }

    // 1. 测试添加规则（POST /api/rule/add）
    @Test
    void testAddRule_Success() throws Exception {
        // 构造测试数据
        RuleReq ruleReq = new RuleReq();
        ruleReq.setId(1L);
        ruleReq.setWarnId(1001);
        ruleReq.setWarnName("Overheat");
        ruleReq.setBatteryType("Lithium");
        ruleReq.setWarnRule("{\\\"0.0\\\":-1}");

        RuleDto ruleDto = new RuleDto();
        ruleDto.setId(1L);
        ruleDto.setWarnId(1001);
        ruleDto.setWarnName("Overheat");
        ruleDto.setBatteryType("Lithium");
        Map<Double, Integer> warnRule = new HashMap<>();
        warnRule.put(0.0, -1);
        ruleDto.setWarnRule(warnRule);

        RuleVo ruleVo = new RuleVo();
        ruleVo.setId(1L);
        ruleVo.setWarnId(1001);
        ruleVo.setWarnName("Overheat");
        ruleVo.setBatteryType("Lithium");
        ruleVo.setWarnRule(warnRule);

        // 模拟依赖行为
        when(ruleVoConverter.toDto(ruleReq)).thenReturn(ruleDto);
        when(ruleService.addRule(ruleDto)).thenReturn(ruleDto);
        when(ruleVoConverter.toVo(ruleDto)).thenReturn(ruleVo);

        // 发起请求并验证结果
        mockMvc.perform(post("/api/rule/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"warnId\":1001,\"warnName\":\"Overheat\",\"batteryType\":\"Lithium\",\"warnRule\":{\"80.0\":1}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.warnName").value("Overheat"));
    }

    // 2. 测试删除规则（DELETE /api/rule/delete/{id}）
    @Test
    void testDeleteRule_Success() throws Exception {
        // 模拟依赖行为
        doNothing().when(ruleService).removeRule(1L);

        // 发起请求并验证结果
        mockMvc.perform(delete("/api/rule/delete/1"))
                .andExpect(status().isOk());
    }

    // 3. 测试修改规则（PUT /api/rule/modify）
    @Test
    void testModifyRule_Success() throws Exception {
        // 构造测试数据
        Map<Double, Integer> warnRule = new HashMap<>();
        warnRule.put(0.0, -1);
        RuleReq ruleReq = new RuleReq();
        ruleReq.setId(1L);
        ruleReq.setWarnId(1001);
        ruleReq.setWarnName("Overheat");
        ruleReq.setBatteryType("Lithium");
        ruleReq.setWarnRule("{\\\"0.0\\\":-1}");

        RuleDto ruleDto = new RuleDto();
        ruleDto.setId(1L);
        ruleDto.setWarnId(1001);
        ruleDto.setWarnName("Overheat");
        ruleDto.setBatteryType("Lithium");
        ruleDto.setWarnRule(warnRule);

        RuleVo ruleVo = new RuleVo();
        ruleVo.setId(1L);
        ruleVo.setWarnId(1001);
        ruleVo.setWarnName("Overheat");
        ruleVo.setBatteryType("Lithium");
        ruleVo.setWarnRule(warnRule);

        // 模拟依赖行为
        when(ruleVoConverter.toDto(ruleReq)).thenReturn(ruleDto);
        when(ruleVoConverter.toVo(ruleDto)).thenReturn(ruleVo);

        // 发起请求并验证结果
        mockMvc.perform(put("/api/rule/modify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"warnId\":1001,\"warnName\":\"Overheat\",\"batteryType\":\"Lithium\",\"warnRule\":{\"80.0\":1}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L));
    }

    // 4. 测试根据ID查询规则（GET /api/rule/find/{id}）
    @Test
    void testFindRuleById_Success() throws Exception {
        // 构造测试数据
        Map<Double, Integer> warnRule = new HashMap<>();
        warnRule.put(0.0, -1);
        RuleDto ruleDto = new RuleDto();
        ruleDto.setId(1L);
        ruleDto.setWarnId(1001);
        ruleDto.setWarnName("Overheat");
        ruleDto.setBatteryType("Lithium");
        ruleDto.setWarnRule(warnRule);

        RuleVo ruleVo = new RuleVo();
        ruleVo.setId(1L);
        ruleVo.setWarnId(1001);
        ruleVo.setWarnName("Overheat");
        ruleVo.setBatteryType("Lithium");
        ruleVo.setWarnRule(warnRule);

        // 模拟依赖行为
        when(ruleVoConverter.toVo(ruleDto)).thenReturn(ruleVo);

        // 发起请求并验证结果
        mockMvc.perform(get("/api/rule/find/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1L));
    }

    // 5. 测试列出所有规则（GET /api/rule）
    @Test
    void testListAllRules_Success() throws Exception {
        // 构造测试数据
        Map<Double, Integer> warnRule = new HashMap<>();
        warnRule.put(0.0, -1);
        RuleDto ruleDto = new RuleDto();
        ruleDto.setId(1L);
        ruleDto.setWarnId(1001);
        ruleDto.setWarnName("Overheat");
        ruleDto.setBatteryType("Lithium");
        ruleDto.setWarnRule(warnRule);

        RuleVo ruleVo = new RuleVo();
        ruleVo.setId(1L);
        ruleVo.setWarnId(1001);
        ruleVo.setWarnName("Overheat");
        ruleVo.setBatteryType("Lithium");
        ruleVo.setWarnRule(warnRule);

        // 模拟依赖行为
        when(ruleVoConverter.toVo(ruleDto)).thenReturn(ruleVo);

        // 发起请求并验证结果
        mockMvc.perform(get("/api/rule"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1L));
    }

    // 6. 测试添加规则时参数校验失败（缺少 warnName）
    @Test
    void testAddRule_ValidationFailed() throws Exception {
        // 构造无效请求体（缺少 warnName）
        String invalidRequest = "{\"id\":1,\"warnId\":1001,\"batteryType\":\"Lithium\",\"warnRule\":{\"80.0\":1}}";

        // 发起请求并验证校验错误
        mockMvc.perform(post("/api/rule/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("预警名称不能为空"));
    }
}