package com.example.bms.main.domain.service;

import com.example.bms.main.common.dto.CarDto;

import java.util.List;

/**
 * @author cz
 * @date 2025/5/18 12:34
 */
public interface CarDomainService {
    CarDto addCar(CarDto carDto);

    void modifyCar(CarDto carDto);

    void removeCar(String vid);

    String generateVid();

    List<CarDto> findByVid(String vid);

    String getBatteryTypeByCarId(Long carId);


}
