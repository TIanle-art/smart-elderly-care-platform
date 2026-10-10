package com.lcyl.code.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lcyl.code.mapper.LcCheckinApproveInitiatorMapper;
import com.lcyl.code.domain.LcCheckinApproveInitiator;
import com.lcyl.code.service.ILcCheckinApproveInitiatorService;

/**
 * 审批发起人Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
@Service
public class LcCheckinApproveInitiatorServiceImpl implements ILcCheckinApproveInitiatorService 
{
    @Autowired
    private LcCheckinApproveInitiatorMapper lcCheckinApproveInitiatorMapper;

    /**
     * 查询审批发起人
     * 
     * @param id 审批发起人主键
     * @return 审批发起人
     */
    @Override
    public LcCheckinApproveInitiator selectLcCheckinApproveInitiatorById(Long id)
    {
        return lcCheckinApproveInitiatorMapper.selectLcCheckinApproveInitiatorById(id);
    }

    /**
     * 查询审批发起人列表
     * 
     * @param lcCheckinApproveInitiator 审批发起人
     * @return 审批发起人
     */
    @Override
    public List<LcCheckinApproveInitiator> selectLcCheckinApproveInitiatorList(LcCheckinApproveInitiator lcCheckinApproveInitiator)
    {
        return lcCheckinApproveInitiatorMapper.selectLcCheckinApproveInitiatorList(lcCheckinApproveInitiator);
    }

    /**
     * 新增审批发起人
     * 
     * @param lcCheckinApproveInitiator 审批发起人
     * @return 结果
     */
    @Override
    public int insertLcCheckinApproveInitiator(LcCheckinApproveInitiator lcCheckinApproveInitiator)
    {
        return lcCheckinApproveInitiatorMapper.insertLcCheckinApproveInitiator(lcCheckinApproveInitiator);
    }

    /**
     * 修改审批发起人
     * 
     * @param lcCheckinApproveInitiator 审批发起人
     * @return 结果
     */
    @Override
    public int updateLcCheckinApproveInitiator(LcCheckinApproveInitiator lcCheckinApproveInitiator)
    {
        return lcCheckinApproveInitiatorMapper.updateLcCheckinApproveInitiator(lcCheckinApproveInitiator);
    }

    /**
     * 批量删除审批发起人
     * 
     * @param ids 需要删除的审批发起人主键
     * @return 结果
     */
    @Override
    public int deleteLcCheckinApproveInitiatorByIds(Long[] ids)
    {
        return lcCheckinApproveInitiatorMapper.deleteLcCheckinApproveInitiatorByIds(ids);
    }

    /**
     * 删除审批发起人信息
     * 
     * @param id 审批发起人主键
     * @return 结果
     */
    @Override
    public int deleteLcCheckinApproveInitiatorById(Long id)
    {
        return lcCheckinApproveInitiatorMapper.deleteLcCheckinApproveInitiatorById(id);
    }
}
