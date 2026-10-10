package com.lcyl.code.mapper;

import java.util.List;
import com.lcyl.code.domain.LcCheckinApproveInitiator;

/**
 * 审批发起人Mapper接口
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
public interface LcCheckinApproveInitiatorMapper 
{
    /**
     * 查询审批发起人
     * 
     * @param id 审批发起人主键
     * @return 审批发起人
     */
    public LcCheckinApproveInitiator selectLcCheckinApproveInitiatorById(Long id);

    /**
     * 查询审批发起人列表
     * 
     * @param lcCheckinApproveInitiator 审批发起人
     * @return 审批发起人集合
     */
    public List<LcCheckinApproveInitiator> selectLcCheckinApproveInitiatorList(LcCheckinApproveInitiator lcCheckinApproveInitiator);

    /**
     * 新增审批发起人
     * 
     * @param lcCheckinApproveInitiator 审批发起人
     * @return 结果
     */
    public int insertLcCheckinApproveInitiator(LcCheckinApproveInitiator lcCheckinApproveInitiator);

    /**
     * 修改审批发起人
     * 
     * @param lcCheckinApproveInitiator 审批发起人
     * @return 结果
     */
    public int updateLcCheckinApproveInitiator(LcCheckinApproveInitiator lcCheckinApproveInitiator);

    /**
     * 删除审批发起人
     * 
     * @param id 审批发起人主键
     * @return 结果
     */
    public int deleteLcCheckinApproveInitiatorById(Long id);

    /**
     * 批量删除审批发起人
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLcCheckinApproveInitiatorByIds(Long[] ids);
}
