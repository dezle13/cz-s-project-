package com.example.bms.main.infrastructure.repository;


import com.example.bms.main.infrastructure.pojo.WarnPojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author cz
 * @date 2025/5/18 22:09
 */
@Mapper
public interface WarnRepository{

    void add(List<WarnPojo> warnPojoList);

    @Select("SELECT * FROM warn WHERE car_id = #{carId}")
    List<WarnPojo> getByCarId(@Param("carId") Integer carId);
}
