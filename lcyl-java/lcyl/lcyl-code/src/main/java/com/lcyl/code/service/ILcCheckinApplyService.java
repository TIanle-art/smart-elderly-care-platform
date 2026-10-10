package com.lcyl.code.service;

import java.util.List;
import com.lcyl.code.domain.LcCheckinApply;
import com.lcyl.code.vo.CheckinHomeVO;
import java.util.Date;

/**
 * 入住申请Service接口
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
public interface ILcCheckinApplyService 
{
    /**
     * 查询入住申请
     * 
     * @param applyId 入住申请主键
     * @return 入住申请
     */
    public LcCheckinApply selectLcCheckinApplyByApplyId(Long applyId);

    /**
     * 查询入住申请列表
     * 
     * @param lcCheckinApply 入住申请
     * @return 入住申请集合
     */
    public List<LcCheckinApply> selectLcCheckinApplyList(LcCheckinApply lcCheckinApply);

    /**
     * 新增入住申请
     * 
     * @param lcCheckinApply 入住申请
     * @return 结果
     */
    public int insertLcCheckinApply(LcCheckinApply lcCheckinApply);

    /**
     * 修改入住申请
     * 
     * @param lcCheckinApply 入住申请
     * @return 结果
     */
    public int updateLcCheckinApply(LcCheckinApply lcCheckinApply);

    /**
     * 批量删除入住申请
     * 
     * @param applyIds 需要删除的入住申请主键集合
     * @return 结果
     */
    public int deleteLcCheckinApplyByApplyIds(Long[] applyIds);

    /**
     * 删除入住申请信息
     * 
     * @param applyId 入住申请主键
     * @return 结果
     */
    public int deleteLcCheckinApplyByApplyId(Long applyId);

    public List<CheckinHomeVO> selectCheckinHomeList(LcCheckinApply apply, Date beginTime, Date endTime);

    public CheckinHomeVO selectCheckinDetail(Long applyId);

    public Long initiateCheckinApply();

    public int updateApplyStatus(Long applyId, String status);
}
