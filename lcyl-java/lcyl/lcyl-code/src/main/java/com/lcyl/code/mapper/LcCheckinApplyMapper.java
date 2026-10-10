package com.lcyl.code.mapper;

import java.util.Date;
import java.util.List;
import com.lcyl.code.domain.LcCheckinApply;
import com.lcyl.code.vo.CheckinHomeVO;
import org.apache.ibatis.annotations.Param;

/**
 * 入住申请Mapper接口
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
public interface LcCheckinApplyMapper 
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
     * 删除入住申请
     * 
     * @param applyId 入住申请主键
     * @return 结果
     */
    public int deleteLcCheckinApplyByApplyId(Long applyId);

    /**
     * 批量删除入住申请
     * 
     * @param applyIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLcCheckinApplyByApplyIds(Long[] applyIds);

    public List<CheckinHomeVO> selectCheckinHomeList(
            @Param("apply") LcCheckinApply apply,
            @Param("beginTime") Date beginTime,
            @Param("endTime") Date endTime
    );

    public CheckinHomeVO selectCheckinDetail(@Param("applyId") Long applyId);
}
