package com.lcyl.code.service.impl;

import java.util.List;
import com.lcyl.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lcyl.code.mapper.LcCheckinEvaluateReportMapper;
import com.lcyl.code.domain.LcCheckinEvaluateReport;
import com.lcyl.code.service.ILcCheckinEvaluateReportService;

/**
 * 评估报告Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
@Service
public class LcCheckinEvaluateReportServiceImpl implements ILcCheckinEvaluateReportService 
{
    @Autowired
    private LcCheckinEvaluateReportMapper lcCheckinEvaluateReportMapper;

    /**
     * 查询评估报告
     * 
     * @param id 评估报告主键
     * @return 评估报告
     */
    @Override
    public LcCheckinEvaluateReport selectLcCheckinEvaluateReportById(Long id)
    {
        return lcCheckinEvaluateReportMapper.selectLcCheckinEvaluateReportById(id);
    }

    /**
     * 查询评估报告列表
     * 
     * @param lcCheckinEvaluateReport 评估报告
     * @return 评估报告
     */
    @Override
    public List<LcCheckinEvaluateReport> selectLcCheckinEvaluateReportList(LcCheckinEvaluateReport lcCheckinEvaluateReport)
    {
        return lcCheckinEvaluateReportMapper.selectLcCheckinEvaluateReportList(lcCheckinEvaluateReport);
    }

    /**
     * 新增评估报告
     * 
     * @param lcCheckinEvaluateReport 评估报告
     * @return 结果
     */
    @Override
    public int insertLcCheckinEvaluateReport(LcCheckinEvaluateReport lcCheckinEvaluateReport)
    {
        lcCheckinEvaluateReport.setCreateTime(DateUtils.getNowDate());
        return lcCheckinEvaluateReportMapper.insertLcCheckinEvaluateReport(lcCheckinEvaluateReport);
    }

    /**
     * 修改评估报告
     * 
     * @param lcCheckinEvaluateReport 评估报告
     * @return 结果
     */
    @Override
    public int updateLcCheckinEvaluateReport(LcCheckinEvaluateReport lcCheckinEvaluateReport)
    {
        return lcCheckinEvaluateReportMapper.updateLcCheckinEvaluateReport(lcCheckinEvaluateReport);
    }

    /**
     * 批量删除评估报告
     * 
     * @param ids 需要删除的评估报告主键
     * @return 结果
     */
    @Override
    public int deleteLcCheckinEvaluateReportByIds(Long[] ids)
    {
        return lcCheckinEvaluateReportMapper.deleteLcCheckinEvaluateReportByIds(ids);
    }

    /**
     * 删除评估报告信息
     * 
     * @param id 评估报告主键
     * @return 结果
     */
    @Override
    public int deleteLcCheckinEvaluateReportById(Long id)
    {
        return lcCheckinEvaluateReportMapper.deleteLcCheckinEvaluateReportById(id);
    }
}
