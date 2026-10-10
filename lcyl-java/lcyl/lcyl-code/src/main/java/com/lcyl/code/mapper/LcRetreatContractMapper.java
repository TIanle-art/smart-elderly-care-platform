package com.lcyl.code.mapper;

import com.lcyl.code.domain.LcRetreatContract;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName LcRetreatContractMapper
 * @Description TODO
 * @Author ziluo
 * @Date 2026-03-26 10:32
 * @Version 1.0
 */
public interface LcRetreatContractMapper {
    int insert(LcRetreatContract record);

    /**
     * 根据退住ID查询合同列表，并关联用户表获取创建人姓名
     * @param retreatId 退住申请ID
     * @return 合同列表（包含创建人姓名）
     */
    List<LcRetreatContract> selectByRetreatId(@Param("retreatId") Long retreatId);


//    LcRetreatContract selectContractByRetreatId(Long id);


    LcRetreatContract selectContractByRetreatId(@Param("retreatId") Long retreatId);

}
