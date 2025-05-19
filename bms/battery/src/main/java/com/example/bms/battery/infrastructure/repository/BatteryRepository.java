package com.example.bms.battery.infrastructure.repository;


import com.example.bms.battery.infrastructure.pojo.BatteryPojo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BatteryRepository {

    List<BatteryPojo> findById(@Param("carId") Integer carId);

    @Insert("INSERT INTO battery(car_id, warn_id, mx, mi, ix, ii)" +
            " values (#{carId},#{warnId},#{mx},#{mx},#{ix},#{ii})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void add(BatteryPojo batteryPojo);


    int update(BatteryPojo batteryPojo);


    @Delete("DELETE from battery where car_id = #{carId}")
    int remove(Integer carId);


}
