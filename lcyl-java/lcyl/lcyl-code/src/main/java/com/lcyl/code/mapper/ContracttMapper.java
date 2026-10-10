package com.lcyl.code.mapper;

import com.lcyl.code.domain.Contractt;
import com.lcyl.system.domain.Contract;
import org.apache.ibatis.annotations.Param;

public interface ContracttMapper {
    public int insertContract(Contractt contract);

    /**
     * 根据老人ID查询合同
     * @param elderId 老人ID
     */
    Contract selectContractById(@Param("elderId") Long elderId);

    /**
     * 更新合同信息
     * @param contract 合同
     */
    void updateContract(Contract contract);
}
