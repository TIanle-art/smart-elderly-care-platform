package com.lcyl.code.mapper;

import org.apache.ibatis.annotations.Param;

public interface ElderrMapper {
    int updateBedId(@Param("elderId") Long elderId, @Param("bedId") Long bedId);
    int updateBedNo(@Param("elderId") Long elderId, @Param("bedNo") String bedNo);

    void updateStatus(@Param("elderId") Long elderId, @Param("status") Integer status);
}
