package com.example.bms.main.infrastructure.conventer;

import com.example.bms.main.common.dto.WarnDto;
import com.example.bms.main.infrastructure.pojo.WarnPojo;
import org.mapstruct.Mapper;

/**
 * @author cz
 * @date 2025/5/18 22:07
 */
@Mapper(componentModel = "spring")
public interface WarnPojoConverter {

    WarnDto toDto(WarnPojo warnPojo);

    WarnPojo toPojo(WarnDto warnDto);
}
