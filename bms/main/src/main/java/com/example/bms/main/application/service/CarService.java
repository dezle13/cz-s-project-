package com.example.bms.main.application.service;

import com.example.bms.main.common.dto.CarDto;

import java.util.List;

public interface CarService {

    CarDto addCar(CarDto carDto);


    void modifyCar(CarDto carDto);

    void removeCar(String vid);

    List<CarDto> getCar(String vid);

    List<CarDto> listCars();
}
