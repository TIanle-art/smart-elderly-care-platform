package com.lcyl.code.mapper;

import com.lcyl.code.domain.Elderr;
import org.apache.ibatis.annotations.Param;

public interface ElderCheckInMapper {
    Elderr selectByIdCardNo(@Param("idCardNo") String idCardNo);

    int insertElder(Elderr elder);
}
