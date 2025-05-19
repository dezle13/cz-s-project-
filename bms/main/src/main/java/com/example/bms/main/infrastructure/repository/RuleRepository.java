package com.example.bms.main.infrastructure.repository;

import com.example.bms.main.infrastructure.pojo.RulePojo;
import org.apache.ibatis.annotations.*;
//import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author cz
 * @date 2025/5/18 16:24
 */
@Mapper
public interface RuleRepository {

    @Insert("INSERT INTO rule( warn_id,warn_name, battery_type, warn_rule)" +
            " values (#{warnId},#{warnName},#{batteryType},#{warnRule})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void add(RulePojo rulePojo);

    int update(RulePojo rulePojo);

    @Delete("DELETE from rule where id = #{id}")
    int remove(Long id);

    List<RulePojo> findById(Long id);



    @Select("SELECT warn_rule FROM rule WHERE warn_id = #{warnId} AND battery_type =#{batteryType}")
    String getWarnRuleByWarnIdAndBatT(@Param("warnId") Integer warnId,@Param("batteryType")  String batteryType);

    @Select("SELECT DISTINCT warn_name FROM rule WHERE warn_id = #{warnId}")
    String getWarnNameByWarnId( @Param("warnId") Integer warnId);
}
