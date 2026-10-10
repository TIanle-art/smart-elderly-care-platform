package com.lcyl.code.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * 床位Mapper接口
 *
 * @author your_name
 * @date
 */
public interface BeddMapper
{



    /** 更新床位状态：0=未入住 1=已入住 */
    int updateBedStatus(@Param("bedId") Long bedId, @Param("status") Integer status);

    void freeBedByBedNo(@Param("bedNo") String bedNo);
}