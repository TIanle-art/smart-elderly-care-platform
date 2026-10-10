package com.lcyl.code.mapper;

import java.util.List;
import com.lcyl.code.domain.LcCheckinEvaluateReport;

/**
 * 评估报告Mapper接口
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
public interface LcCheckinEvaluateReportMapper 
{
    /**
     * 查询评估报告
     * 
     * @param id 评估报告主键
     * @return 评估报告
     */
    public LcCheckinEvaluateReport selectLcCheckinEvaluateReportById(Long id);

    /**
     * 查询评估报告列表
     * 
     * @param lcCheckinEvaluateReport 评估报告
     * @return 评估报告集合
     */
    public List<LcCheckinEvaluateReport> selectLcCheckinEvaluateReportList(LcCheckinEvaluateReport lcCheckinEvaluateReport);

    /**
     * 新增评估报告
     * 
     * @param lcCheckinEvaluateReport 评估报告
     * @return 结果
     */
    public int insertLcCheckinEvaluateReport(LcCheckinEvaluateReport lcCheckinEvaluateReport);

    /**
     * 修改评估报告
     * 
     * @param lcCheckinEvaluateReport 评估报告
     * @return 结果
     */
    public int updateLcCheckinEvaluateReport(LcCheckinEvaluateReport lcCheckinEvaluateReport);

    /**
     * 删除评估报告
     * 
     * @param id 评估报告主键
     * @return 结果
     */
    public int deleteLcCheckinEvaluateReportById(Long id);

    /**
     * 批量删除评估报告
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLcCheckinEvaluateReportByIds(Long[] ids);
}
