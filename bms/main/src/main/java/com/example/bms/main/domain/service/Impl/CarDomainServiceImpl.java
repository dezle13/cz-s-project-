package com.example.bms.main.domain.service.Impl;

import com.example.bms.main.common.dto.CarDto;
import com.example.bms.main.common.exception.DomainException;
import com.example.bms.main.domain.service.CarDomainService;
import com.example.bms.main.infrastructure.conventer.CarPojoConverter;
import com.example.bms.main.infrastructure.pojo.CarPojo;
import com.example.bms.main.infrastructure.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author cz
 * @date 2025/5/18 12:36
 */
@Service
public class CarDomainServiceImpl implements CarDomainService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarPojoConverter carPojoConverter;


    @Transactional
    @Override
    public CarDto addCar(CarDto carDto) {
        String vid =  generateVid();
        carDto.setVid(vid);
        CarPojo carPojo = carPojoConverter.toPojo(carDto);
        carRepository.add(carPojo);
        return carDto;
    }

    @Override
    @Transactional
    public void modifyCar(CarDto carDto) {
        CarPojo carPojo = carPojoConverter.toPojo(carDto);
        if (carRepository.update(carPojo) == 0){
            throw new DomainException("no car");
        }
    }

    @Override
    @Transactional
    public void removeCar(String vid) {
        if (carRepository.remove(vid) == 0){
            throw new DomainException("no car");
        }
    }

    @Override
    public String generateVid() {
        long timeStamp =System.currentTimeMillis();
        //未来数十年都是13位
        String timeStampStr =Long.toString(timeStamp);;
        //取后8位，作为唯一标识
        String timeStampStrOnly8 = timeStampStr.substring(timeStampStr.length() -8);
        //加8个
        String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase().substring(0, 8);

        return uuid + timeStampStrOnly8;
    }

    @Override
    @Transactional
    public List<CarDto> findByVid(String vid) {
        List<CarPojo> carPojoList = carRepository.findByVid(vid);
        return carPojoList.stream().map(carPojoConverter::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String getBatteryTypeByCarId(Long carId) {
        return carRepository.getBatteryTypeByCarId(carId);
    }
}
