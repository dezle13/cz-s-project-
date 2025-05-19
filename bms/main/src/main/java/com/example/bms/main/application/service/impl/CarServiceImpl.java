package com.example.bms.main.application.service.impl;

import com.example.bms.main.application.service.CarService;
import com.example.bms.main.common.dto.CarDto;
import com.example.bms.main.domain.service.CarDomainService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 传进来的是CarDto
 * @author cz
 * @date 2025/5/18 12:31
 */
@Service
public class CarServiceImpl implements CarService {


    //调用DomainService
    @Resource
    private CarDomainService carDomainService;

    @Override
    public CarDto addCar(CarDto carDto) {
        return carDomainService.addCar(carDto);
    }

    @Override
    public void modifyCar(CarDto carDto) {
        carDomainService.modifyCar(carDto);
    }

    @Override
    public void removeCar(String vid) {
        carDomainService.removeCar(vid);
    }

    @Override
    public List<CarDto> getCar(String vid) {
        return carDomainService.findByVid(vid);
    }
    //使用动态SQL
    @Override
    public List<CarDto> listCars() {
        return carDomainService.findByVid(null);
    }
}
