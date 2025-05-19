package com.example.bms.battery;

import com.example.bms.battery.application.service.BatteryService;
import com.example.bms.battery.common.ValidationGroup.InsertGroup;
import com.example.bms.battery.common.ValidationGroup.UpdateGroup;
import com.example.bms.battery.common.dto.BatteryDto;
import com.example.bms.battery.inter.model.converter.BatteryVoConverter;
import com.example.bms.battery.inter.model.request.BatteryReq;
import com.example.bms.battery.inter.model.response.BatteryVo;
import com.example.bms.battery.inter.model.response.ResponseMessage;
import com.example.bms.battery.inter.web.BatteryController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BatteryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BatteryService batteryService;

    @Mock
    private BatteryVoConverter batteryVoConverter;

    @InjectMocks
    private BatteryController batteryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(batteryController).build();
    }

    // 1. 测试添加电池（POST /api/battery/add）
    @Test
    void testAddBattery_Success() throws Exception {
        // 构造测试数据
        BatteryReq batteryReq = new BatteryReq();
        batteryReq.setCarId(101);
        batteryReq.setWarnId(201);
        batteryReq.setMx(90.0);
        batteryReq.setMi(70.0);
        batteryReq.setIx(150.0);
        batteryReq.setIi(120.0);

        BatteryDto batteryDto = new BatteryDto();
        batteryDto.setCarId(101);
        batteryDto.setWarnId(201);
        batteryDto.setMx(90.0);
        batteryDto.setMi(70.0);
        batteryDto.setIx(150.0);
        batteryDto.setIi(120.0);

        BatteryVo batteryVo = new BatteryVo();
        batteryVo.setCarId(101);
        batteryVo.setWarnId(201);
        batteryVo.setSignal("Temperature: 90°C, Current: 150A");

        // 模拟依赖行为
        when(batteryVoConverter.toDto(batteryReq)).thenReturn(batteryDto);
        when(batteryService.addBattery(batteryDto)).thenReturn(batteryDto);
        when(batteryVoConverter.toVo(batteryDto)).thenReturn(batteryVo);

        // 发起请求并验证结果
        mockMvc.perform(post("/api/battery/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"carId\":101,\"warnId\":201,\"mx\":90.0,\"mi\":70.0,\"ix\":150.0,\"ii\":120.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.carId").value(101))
                .andExpect(jsonPath("$.data.signal").value("Temperature: 90°C, Current: 150A"));
    }

    // 2. 测试删除电池（DELETE /api/battery/delete/{id}）
    @Test
    void testDeleteBattery_Success() throws Exception {
        // 模拟依赖行为
        doNothing().when(batteryService).removeBattery(101);

        // 发起请求并验证结果
        mockMvc.perform(delete("/api/battery/delete/101"))
                .andExpect(status().isOk());
    }

    // 3. 测试修改电池（PUT /api/battery/modify）
    @Test
    void testModifyBattery_Success() throws Exception {
        // 构造测试数据
        BatteryReq batteryReq = new BatteryReq();
        batteryReq.setCarId(101);
        batteryReq.setWarnId(201);
        batteryReq.setMx(90.0);
        batteryReq.setMi(70.0);
        batteryReq.setIx(150.0);
        batteryReq.setIi(120.0);

        BatteryDto batteryDto = new BatteryDto();
        batteryDto.setCarId(101);
        batteryDto.setWarnId(201);
        batteryDto.setMx(90.0);
        batteryDto.setMi(70.0);
        batteryDto.setIx(150.0);
        batteryDto.setIi(120.0);

        BatteryVo batteryVo = new BatteryVo();
        batteryVo.setCarId(101);
        batteryVo.setWarnId(201);
        batteryVo.setSignal("Temperature: 90°C, Current: 150A");

        // 模拟依赖行为
        when(batteryVoConverter.toDto(batteryReq)).thenReturn(batteryDto);
        when(batteryVoConverter.toVo(batteryDto)).thenReturn(batteryVo);

        // 发起请求并验证结果
        mockMvc.perform(put("/api/battery/modify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"carId\":101,\"warnId\":201,\"mx\":90.0,\"mi\":70.0,\"ix\":150.0,\"ii\":120.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.carId").value(101));
    }

    // 4. 测试根据carId查询电池（GET /api/battery/find/{carId}）
    @Test
    void testFindBatteryByCarId_Success() throws Exception {
        // 构造测试数据
        BatteryDto batteryDto = new BatteryDto();
        batteryDto.setCarId(101);
        batteryDto.setWarnId(201);
        batteryDto.setMx(90.0);
        batteryDto.setMi(70.0);
        batteryDto.setIx(150.0);
        batteryDto.setIi(120.0);

        BatteryVo batteryVo = new BatteryVo();
        batteryVo.setCarId(101);
        batteryVo.setWarnId(201);
        batteryVo.setSignal("Temperature: 90°C, Current: 150A");

        // 模拟依赖行为
        when(batteryService.getBattery(101)).thenReturn(batteryDto);
        when(batteryVoConverter.toVo(batteryDto)).thenReturn(batteryVo);

        // 发起请求并验证结果
        mockMvc.perform(get("/api/battery/find/101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.carId").value(101));
    }

    // 5. 测试列出所有电池（GET /api/battery）
    @Test
    void testListAllBatteries_Success() throws Exception {
        // 构造测试数据
        BatteryDto batteryDto = new BatteryDto();
        batteryDto.setCarId(101);
        batteryDto.setWarnId(201);
        batteryDto.setMx(90.0);
        batteryDto.setMi(70.0);
        batteryDto.setIx(150.0);
        batteryDto.setIi(120.0);

        BatteryVo batteryVo = new BatteryVo();
        batteryVo.setCarId(101);
        batteryVo.setWarnId(201);
        batteryVo.setSignal("Temperature: 90°C, Current: 150A");

        // 模拟依赖行为
        when(batteryService.listBatterys()).thenReturn(Collections.singletonList(batteryDto));
        when(batteryVoConverter.toVo(batteryDto)).thenReturn(batteryVo);

        // 发起请求并验证结果
        mockMvc.perform(get("/api/battery"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].carId").value(101));
    }

    // 6. 测试触发扫描信号（GET /api/battery/submit）
    @Test
    void testSubmitScanSignals_Success() throws Exception {
        // 模拟依赖行为
        doNothing().when(batteryService).scanSignals();

        // 发起请求并验证结果
        mockMvc.perform(get("/api/battery/submit"))
                .andExpect(status().isOk());
    }

    // 7. 测试添加电池时参数校验失败（缺少 carId）
    @Test
    void testAddBattery_ValidationFailed() throws Exception {
        // 构造无效请求体（缺少 carId）
        String invalidRequest = "{\"warnId\":201,\"mx\":90.0,\"mi\":70.0,\"ix\":150.0,\"ii\":120.0}";

        // 发起请求并验证校验错误
        mockMvc.perform(post("/api/battery/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }
}