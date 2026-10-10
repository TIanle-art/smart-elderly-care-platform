package com.lcyl.code.mapper;

import com.lcyl.code.domain.LcRetreatBill;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName LcRetreatBillMapper
 * @Description TODO
 * @Author ziluo
 * @Date 2026-03-26 10:32
 * @Version 1.0
 */
public interface LcRetreatBillMapper {
    int insert (LcRetreatBill record);
    int updateById(LcRetreatBill record);
    LcRetreatBill selectLatestByRetreatId(@Param("retreatId") Long retreatId);
}
