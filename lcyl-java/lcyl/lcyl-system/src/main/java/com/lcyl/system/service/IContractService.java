package com.lcyl.system.service;

import com.lcyl.system.domain.Contract;

import java.util.List;

/**
 * 合同跟踪Service接口
 * 
 * @author tyf
 * @date 2026-03-25
 */
public interface IContractService 
{
    /**
     * 查询合同跟踪
     * 
     * @param id 合同跟踪主键
     * @return 合同跟踪
     */
    public Contract selectContractById(Long id);

    /**
     * 查询合同跟踪列表
     * 
     * @param contract 合同跟踪
     * @return 合同跟踪集合
     */
    public List<Contract> selectContractList(Contract contract);

    /**
     * 新增合同跟踪
     * 
     * @param contract 合同跟踪
     * @return 结果
     */
    public int insertContract(Contract contract);

    /**
     * 修改合同跟踪
     * 
     * @param contract 合同跟踪
     * @return 结果
     */
    public int updateContract(Contract contract);
    void batchUpdateContractStatus();

    /**
     * 批量删除合同跟踪
     * 
     * @param ids 需要删除的合同跟踪主键集合
     * @return 结果
     */
    public int deleteContractByIds(Long[] ids);

    /**
     * 删除合同跟踪信息
     * 
     * @param id 合同跟踪主键
     * @return 结果
     */
    public int deleteContractById(Long id);
}
