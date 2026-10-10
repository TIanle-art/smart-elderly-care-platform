package com.lcyl.code.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lcyl.code.constant.CheckinConstants;
import com.lcyl.code.vo.CheckinHomeVO;
import com.lcyl.common.utils.DateUtils;
import com.lcyl.common.utils.uuid.IdUtils;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lcyl.code.mapper.LcCheckinApplyMapper;
import com.lcyl.code.domain.LcCheckinApply;
import com.lcyl.code.service.ILcCheckinApplyService;


/**
 * 入住申请Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
@Service
public class LcCheckinApplyServiceImpl implements ILcCheckinApplyService 
{
    @Autowired
    private LcCheckinApplyMapper lcCheckinApplyMapper;
    @Autowired
    private RuntimeService runtimeService;

    /**
     * 查询入住申请
     * 
     * @param applyId 入住申请主键
     * @return 入住申请
     */
    @Override
    public LcCheckinApply selectLcCheckinApplyByApplyId(Long applyId)
    {
        return lcCheckinApplyMapper.selectLcCheckinApplyByApplyId(applyId);
    }

    /**
     * 查询入住申请列表
     * 
     * @param lcCheckinApply 入住申请
     * @return 入住申请
     */
    @Override
    public List<LcCheckinApply> selectLcCheckinApplyList(LcCheckinApply lcCheckinApply)
    {
        return lcCheckinApplyMapper.selectLcCheckinApplyList(lcCheckinApply);
    }

    /**
     * 新增入住申请
     * 
     * @param lcCheckinApply 入住申请
     * @return 结果
     */
    @Override
    public int insertLcCheckinApply(LcCheckinApply lcCheckinApply)
    {
        lcCheckinApply.setCreateTime(DateUtils.getNowDate());
        return lcCheckinApplyMapper.insertLcCheckinApply(lcCheckinApply);
    }

    /**
     * 修改入住申请
     * 
     * @param lcCheckinApply 入住申请
     * @return 结果
     */
    @Override
    public int updateLcCheckinApply(LcCheckinApply lcCheckinApply)
    {
        lcCheckinApply.setUpdateTime(DateUtils.getNowDate());
        return lcCheckinApplyMapper.updateLcCheckinApply(lcCheckinApply);
    }

    /**
     * 批量删除入住申请
     * 
     * @param applyIds 需要删除的入住申请主键
     * @return 结果
     */
    @Override
    public int deleteLcCheckinApplyByApplyIds(Long[] applyIds)
    {
        return lcCheckinApplyMapper.deleteLcCheckinApplyByApplyIds(applyIds);
    }

    /**
     * 删除入住申请信息
     * 
     * @param applyId 入住申请主键
     * @return 结果
     */
    @Override
    public int deleteLcCheckinApplyByApplyId(Long applyId)
    {
        return lcCheckinApplyMapper.deleteLcCheckinApplyByApplyId(applyId);
    }

    @Override
    public List<CheckinHomeVO> selectCheckinHomeList(LcCheckinApply apply, Date beginTime, Date endTime) {
        return lcCheckinApplyMapper.selectCheckinHomeList(apply, beginTime, endTime);
    }
    @Override
    public CheckinHomeVO selectCheckinDetail(Long applyId) {
        return lcCheckinApplyMapper.selectCheckinDetail(applyId);
    }
    @Override
    public Long initiateCheckinApply() {
        // 1. 生成原型要求的单据编号：RZ+日期（yyyyMMddHHmmss）+2位随机数
        String applyNo = CheckinConstants.APPLY_NO_PREFIX
                + DateUtils.dateTimeNow("yyyyMMddHHmmss");


        // 2. 封装申请信息
        LcCheckinApply apply = new LcCheckinApply();
        apply.setApplyNo(applyNo);
        apply.setApplyStatus(CheckinConstants.APPLY_STATUS_INIT);
        apply.setDelFlag("0");
        apply.setCreateTime(DateUtils.getNowDate());

        // 3. 保存主表（初始生成器的save方法）
        this.insertLcCheckinApply(apply);
        Long applyId = apply.getApplyId();

        // 4. 启动Activiti流程（初始环境保留）
        Map<String, Object> variables = new HashMap<>();
        variables.put("applyId", applyId);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                CheckinConstants.CHECKIN_PROCESS_KEY,
                applyId.toString(),
                variables
        );

        // 5. 更新流程实例ID
        apply.setProcessInstanceId(processInstance.getId());
        this.updateLcCheckinApply(apply);

        return applyId;
    }

    @Override
    public int updateApplyStatus(Long applyId, String status) {
        LcCheckinApply apply = new LcCheckinApply();
        apply.setApplyId(applyId);
        apply.setApplyStatus(status);
        apply.setUpdateTime(DateUtils.getNowDate());
        return this.updateLcCheckinApply(apply) ;
    }
}
