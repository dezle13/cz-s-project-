package com.example.bms.main;

import com.example.bms.main.application.service.WarnService;
import com.example.bms.main.common.dto.SignalDto;
import com.example.bms.main.common.dto.WarnDto;
import com.example.bms.main.inter.model.converter.WarnVoConverter;
import com.example.bms.main.inter.model.request.WarnReq;
import com.example.bms.main.inter.model.response.ResponseMessage;
import com.example.bms.main.inter.model.response.WarnVo;
import com.example.bms.main.inter.web.BMSController;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@Slf4j
public class BMSControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WarnService warnService;

    @Mock
    private WarnVoConverter warnVoConverter;

    @InjectMocks
    private BMSController bmsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bmsController).build();
    }

    // =================== POST /api/warn 接口测试 ===================

    @Test
    void testGetWarnLevel_WithValidRequests() throws Exception {
        // 构造测试数据
        WarnReq req1 = new WarnReq(1L, 1, "{\"Mx\":12.0,\"Mi\":0.6}");
        WarnReq req2 = new WarnReq(2L, 2, "{\"Ix\":12.0,\"Ii\":11.7}");
        List<WarnReq> warnReqList = Arrays.asList(req1, req2);


        WarnDto warnDto1 = new WarnDto(1L, "三元电池", "电压差报警", 0);
        WarnDto warnDto2 = new WarnDto(2L, "铁锂电池", "电流差报警", 1);

        WarnVo warnVo1 = new WarnVo(1L, "三元电池", "电压差报警", "0");
        WarnVo warnVo2 = new WarnVo(2L, "铁锂电池", "电流差报警", "1");

        // 模拟依赖
        when(warnVoConverter.toVo(warnDto1)).thenThrow(new IllegalArgumentException("signal 字段格式错误"));
        when(warnVoConverter.toVo(warnDto2)).thenThrow(new IllegalArgumentException("signal 字段格式错误"));

        // 执行测试
        mockMvc.perform(post("/api/warn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(warnReqList)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].carId").value(1))
                .andExpect(jsonPath("$.data[0].batteryType").value("三元电池"))
                .andExpect(jsonPath("$.data[0].warnName").value("电压差报警"))
                .andExpect(jsonPath("$.data[0].warnLevel").value("0"))
                .andExpect(jsonPath("$.data[1].carId").value(2))
                .andExpect(jsonPath("$.data[1].batteryType").value("铁锂电池"))
                .andExpect(jsonPath("$.data[1].warnName").value("电流差报警"))
                .andExpect(jsonPath("$.data[1].warnLevel").value("2"));
    }

    @Test
    void testGetWarnLevel_WithInvalidRequest() throws Exception {
        // 构造非法请求（carId 为 null）
        WarnReq invalidReq = new WarnReq(null, 1, "{\"Mx\":12.0,\"Mi\":0.6}");

        List<WarnReq> warnReqList = Collections.singletonList(invalidReq);

        // 执行测试
        ResultActions result =  mockMvc.perform(post("/api/warn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(warnReqList)));
        log.info("result for  testGetWarnLevelfirst is {}",result.toString());
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("汽车id不能为空;"));
    }

    // =================== GET /api/warn/get/{carId} 接口测试 ===================

    @Test
    void testFindCar_WithValidCarId() throws Exception {
        // 构造测试数据
        Integer carId = 1;
        WarnDto warnDto = new WarnDto(1L, "三元电池", "电压差报警", 0);
        WarnVo warnVo = new WarnVo(1L, "三元电池", "电压差报警", "0");

        // 模拟依赖
        when(warnService.getByCarId(carId)).thenReturn(Collections.singletonList(warnDto));
        when(warnVoConverter.toVo(warnDto)).thenReturn(warnVo);

        // 执行测试
        ResultActions result =  mockMvc.perform(get("/api/warn/get/" + carId));
        log.info("result for  testFindCar_WithValidCarId is {}",result.toString());
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].carId").value(1))
                .andExpect(jsonPath("$.data[0].warnId").value("三元电池"))
                .andExpect(jsonPath("$.data[0].warnLevel").value("0"));

        // 验证方法调用
        verify(warnService, times(1)).getByCarId(eq(carId));
        verify(warnVoConverter, times(1)).toVo(any(WarnDto.class));
    }

    @Test
    void testFindCar_WithNonExistentCarId() throws Exception {
        // 构造测试数据
        Integer carId = 999;

        // 模拟依赖（假设服务返回空列表）
        when(warnService.getByCarId(carId)).thenReturn(Collections.emptyList());

        // 执行测试
        mockMvc.perform(get("/api/warn/get/" + carId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0));
    }

    // =================== 辅助方法 ===================

    private String objectToJson(Object obj) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("JSON 转换失败", e);
        }
    }
}