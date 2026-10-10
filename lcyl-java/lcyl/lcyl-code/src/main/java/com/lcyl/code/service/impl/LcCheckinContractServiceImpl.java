package com.lcyl.code.service.impl;

import java.util.List;
import com.lcyl.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lcyl.code.mapper.LcCheckinContractMapper;
import com.lcyl.code.domain.LcCheckinContract;
import com.lcyl.code.service.ILcCheckinContractService;

/**
 * 签约办理Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
@Service
public class LcCheckinContractServiceImpl implements ILcCheckinContractService 
{
    @Autowired
    private LcCheckinContractMapper lcCheckinContractMapper;

    /**
     * 查询签约办理
     * 
     * @param id 签约办理主键
     * @return 签约办理
     */
    @Override
    public LcCheckinContract selectLcCheckinContractById(Long id)
    {
        return lcCheckinContractMapper.selectLcCheckinContractById(id);
    }

    /**
     * 查询签约办理列表
     * 
     * @param lcCheckinContract 签约办理
     * @return 签约办理
     */
    @Override
    public List<LcCheckinContract> selectLcCheckinContractList(LcCheckinContract lcCheckinContract)
    {
        return lcCheckinContractMapper.selectLcCheckinContractList(lcCheckinContract);
    }

    /**
     * 新增签约办理
     * 
     * @param lcCheckinContract 签约办理
     * @return 结果
     */
    @Override
    public int insertLcCheckinContract(LcCheckinContract lcCheckinContract)
    {
        lcCheckinContract.setCreateTime(DateUtils.getNowDate());
        return lcCheckinContractMapper.insertLcCheckinContract(lcCheckinContract);
    }

    /**
     * 修改签约办理
     * 
     * @param lcCheckinContract 签约办理
     * @return 结果
     */
    @Override
    public int updateLcCheckinContract(LcCheckinContract lcCheckinContract)
    {
        return lcCheckinContractMapper.updateLcCheckinContract(lcCheckinContract);
    }

    /**
     * 批量删除签约办理
     * 
     * @param ids 需要删除的签约办理主键
     * @return 结果
     */
    @Override
    public int deleteLcCheckinContractByIds(Long[] ids)
    {
        return lcCheckinContractMapper.deleteLcCheckinContractByIds(ids);
    }

    /**
     * 删除签约办理信息
     * 
     * @param id 签约办理主键
     * @return 结果
     */
    @Override
    public int deleteLcCheckinContractById(Long id)
    {
        return lcCheckinContractMapper.deleteLcCheckinContractById(id);
    }
}
