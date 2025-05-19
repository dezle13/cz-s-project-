package com.example.bms.main;

import com.example.bms.main.application.service.CarService;
import com.example.bms.main.common.ValidationGroup.*;
import com.example.bms.main.common.dto.CarDto;
import com.example.bms.main.inter.model.converter.CarVoConverter;
import com.example.bms.main.inter.model.request.CarReq;
import com.example.bms.main.inter.model.response.CarVo;

import com.example.bms.main.inter.web.CarController;
import com.fasterxml.jackson.databind.ObjectMapper;
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

public class CarControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CarService carService;

    @Mock
    private CarVoConverter carVoConverter;

    @InjectMocks
    private CarController carController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(carController).build();
    }

    // 测试添加汽车（正常场景）
    @Test
    void testAddCar_Success() throws Exception {
        // 准备测试数据
        CarReq carReq = new CarReq();
        carReq.setVid("V123");
        carReq.setCarId(1001L);
        carReq.setBatteryType("Lithium");
        carReq.setTotalMileage(50000L);
        carReq.setBatteryStatus(1);

        CarDto carDto = new CarDto();
        carDto.setVid("V123");
        carDto.setCarId(1001L);
        carDto.setBatteryType("Lithium");
        carDto.setTotalMileage(50000L);
        carDto.setBatteryStatus(1);

        CarVo carVo = new CarVo();
        carVo.setVid("V123");
        carVo.setCarId(1001L);
        carVo.setBatteryType("Lithium");
        carVo.setTotalMileage(50000L);
        carVo.setBatteryStatus(1);

        // 模拟依赖行为
        when(carVoConverter.toDto(carReq)).thenReturn(carDto);
        when(carService.addCar(carDto)).thenReturn(carDto);
        when(carVoConverter.toVo(carDto)).thenReturn(carVo);

        // 执行测试
        mockMvc.perform(post("/api/car/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(carReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.vid").value("V123"))
                .andExpect(jsonPath("$.data.carId").value(1001))
                .andExpect(jsonPath("$.data.batteryType").value("Lithium"));
    }

    // 测试添加汽车（校验失败：vid 为空）
    @Test
    void testAddCar_ValidationFail_VidIsNull() throws Exception {
        CarReq carReq = new CarReq();
        carReq.setCarId(1001L);
        carReq.setBatteryType("Lithium");
        carReq.setTotalMileage(50000L);
        carReq.setBatteryStatus(1);

        mockMvc.perform(post("/api/car/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(carReq)))
                .andExpect(status().isBadRequest());
    }

    // 测试删除汽车
    @Test
    void testDeleteCar_Success() throws Exception {
        String vid = "V123";
        mockMvc.perform(delete("/api/car/delete/{vid}", vid))
                .andExpect(status().isOk());
    }

    // 测试修改汽车（正常场景）
    @Test
    void testModifyCar_Success() throws Exception {
        CarReq carReq = new CarReq();
        carReq.setVid("V123");
        carReq.setCarId(1001L);
        carReq.setBatteryType("Lithium");
        carReq.setTotalMileage(60000L);
        carReq.setBatteryStatus(2);

        CarDto carDto = new CarDto();
        carDto.setVid("V123");
        carDto.setCarId(1001L);
        carDto.setBatteryType("Lithium");
        carDto.setTotalMileage(60000L);
        carDto.setBatteryStatus(2);

        CarVo carVo = new CarVo();
        carVo.setVid("V123");
        carVo.setCarId(1001L);
        carVo.setBatteryType("Lithium");
        carVo.setTotalMileage(60000L);
        carVo.setBatteryStatus(2);

        // 模拟依赖行为
        when(carVoConverter.toDto(carReq)).thenReturn(carDto);
        when(carVoConverter.toVo(carDto)).thenReturn(carVo);

        mockMvc.perform(put("/api/car/modify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(carReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalMileage").value(60000));
    }

    // 测试修改汽车（校验失败：carId 为 null）
    @Test
    void testModifyCar_ValidationFail_CarIdIsNull() throws Exception {
        CarReq carReq = new CarReq();
        carReq.setVid("V123");
        carReq.setBatteryType("Lithium");
        carReq.setTotalMileage(60000L);
        carReq.setBatteryStatus(2);

        mockMvc.perform(put("/api/car/modify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(carReq)))
                .andExpect(status().isBadRequest());
    }

    // 测试查询单个汽车
    @Test
    void testFindCar_Success() throws Exception {
        String vid = "V123";
        CarDto carDto = new CarDto();
        carDto.setVid("V123");
        carDto.setCarId(1001L);
        carDto.setBatteryType("Lithium");
        carDto.setTotalMileage(50000L);
        carDto.setBatteryStatus(1);

        CarVo carVo = new CarVo();
        carVo.setVid("V123");
        carVo.setCarId(1001L);
        carVo.setBatteryType("Lithium");
        carVo.setTotalMileage(50000L);
        carVo.setBatteryStatus(1);

        // 模拟依赖行为
        when(carService.getCar(vid)).thenReturn(Collections.singletonList(carDto));
        when(carVoConverter.toVo(carDto)).thenReturn(carVo);

        mockMvc.perform(get("/api/car/find/{vid}", vid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].batteryType").value("Lithium"));
    }

    // 测试查询所有汽车
    @Test
    void testListAllCar_Success() throws Exception {
        CarDto carDto = new CarDto();
        carDto.setVid("V123");
        carDto.setCarId(1001L);
        carDto.setBatteryType("Lithium");
        carDto.setTotalMileage(50000L);
        carDto.setBatteryStatus(1);

        CarVo carVo = new CarVo();
        carVo.setVid("V123");
        carVo.setCarId(1001L);
        carVo.setBatteryType("Lithium");
        carVo.setTotalMileage(50000L);
        carVo.setBatteryStatus(1);

        // 模拟依赖行为
        when(carService.listCars()).thenReturn(Collections.singletonList(carDto));
        when(carVoConverter.toVo(carDto)).thenReturn(carVo);

        mockMvc.perform(get("/api/car"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].totalMileage").value(50000));
    }

    // 辅助方法：对象转 JSON
    private String objectToJson(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("JSON 转换失败", e);
        }
    }
}