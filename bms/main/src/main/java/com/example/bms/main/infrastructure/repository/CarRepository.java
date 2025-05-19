package com.example.bms.main.infrastructure.repository;

import com.example.bms.main.infrastructure.pojo.CarPojo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CarRepository {

    List<CarPojo> findByVid(String vid);

    @Insert("INSERT INTO car(vid, car_id, battery_type, total_mileage, battery_status)" +
            " values (#{vid},#{carId},#{batteryType},#{totalMileage},#{batteryStatus})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void add(CarPojo carPojo);


    int update(CarPojo carPojo);


    @Delete("DELETE from car where vid = #{vid}")
    int remove(String vid);

    @Select("SELECT battery_type FROM car WHERE car_id = #{cardId}")
    String getBatteryTypeByCarId(Long carId);
}
