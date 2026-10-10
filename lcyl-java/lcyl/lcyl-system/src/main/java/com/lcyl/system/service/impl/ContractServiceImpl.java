package com.lcyl.system.service.impl;

import com.lcyl.common.utils.DateUtils;
import com.lcyl.common.utils.StringUtils;
import com.lcyl.system.domain.Contract;
import com.lcyl.system.domain.Elder;
import com.lcyl.system.mapper.ContractMapper;
import com.lcyl.system.mapper.ElderMapper;
import com.lcyl.system.service.IContractService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 合同跟踪Service业务层处理
 * 
 * @author tyf
 * @date 2026-03-25
 */
@Service
public class ContractServiceImpl implements IContractService 
{
    @Autowired
    private ContractMapper contractMapper;

    /**
     * 查询合同跟踪
     * 
     * @param id 合同跟踪主键
     * @return 合同跟踪
     */
    @Override
    public Contract selectContractById(Long id)
    {
        return contractMapper.selectContractById(id);
    }

    /**
     * 查询合同跟踪列表
     * 
     * @param contract 合同跟踪
     * @return 合同跟踪
     */
    @Autowired
    private ElderMapper elderMapper;

    @Override
    public List<Contract> selectContractList(Contract contract) {
        // 处理时间范围参数
        Map<String, Object> params = contract.getParams();
        if (params != null) {
            String beginTimeStr = (String) params.get("beginTime");
            String endTimeStr = (String) params.get("endTime");

            if (beginTimeStr != null) {
                contract.setCreateTime(DateUtils.parseDate(beginTimeStr));
            }
            if (endTimeStr != null) {
                contract.setEndTime(DateUtils.parseDate(endTimeStr));
            }
        }

        // ===================== 【优化：根据老人姓名查询ID】 =====================
        String elderName = contract.getElderName();
        if (StringUtils.isNotBlank(elderName)) {
            Elder queryElder = new Elder();
            queryElder.setName(elderName); // 按姓名查询
            List<Elder> elderList = elderMapper.selectElderList(queryElder);

            // 如果查到老人，取第一个老人的ID
            if (CollectionUtils.isNotEmpty(elderList)) {
                Elder elder = elderList.get(0);
                contract.setElderId(elder.getId());
            } else {
                contract.setElderId(null); // 查不到则设为null
            }
        }
        return contractMapper.selectContractList(contract);
    }

    /**
     * 新增合同跟踪
     * 
     * @param contract 合同跟踪
     * @return 结果
     */
    @Override
    public int insertContract(Contract contract)
    {
        contract.setCreateTime(DateUtils.getNowDate());
        return contractMapper.insertContract(contract);
    }

    /**
     * 修改合同跟踪
     * 
     * @param contract 合同跟踪
     * @return 结果
     */
    @Override
    public int updateContract(Contract contract)
    {
        return contractMapper.updateContract(contract);
    }

    @Override
    public void batchUpdateContractStatus() {
        contractMapper.batchUpdateContractStatus();
    }

    /**
     * 批量删除合同跟踪
     * 
     * @param ids 需要删除的合同跟踪主键
     * @return 结果
     */
    @Override
    public int deleteContractByIds(Long[] ids)
    {
        return contractMapper.deleteContractByIds(ids);
    }

    /**
     * 删除合同跟踪信息
     * 
     * @param id 合同跟踪主键
     * @return 结果
     */
    @Override
    public int deleteContractById(Long id)
    {
        return contractMapper.deleteContractById(id);
    }
}
