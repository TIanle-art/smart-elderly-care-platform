package com.lcyl.code.service.impl;

import java.util.Date;
import java.util.List;

import com.lcyl.code.domain.Nurse;
import com.lcyl.code.mapper.NursingPlanItemMapper;
import com.lcyl.code.utils.NurseUtils;
import com.lcyl.common.core.domain.entity.SysUser;
import com.lcyl.common.exception.ServiceException;
import com.lcyl.common.utils.DateUtils;
import com.lcyl.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

import com.lcyl.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.lcyl.code.domain.NursingPlanItem;
import com.lcyl.code.mapper.NursingPlanMapper;
import com.lcyl.code.domain.NursingPlan;
import com.lcyl.code.service.INursingPlanService;

/**
 * 护理计划Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
@Service
public class NursingPlanServiceImpl implements INursingPlanService 
{
    @Autowired
    private NursingPlanMapper nursingPlanMapper;
    @Autowired
    private NursingPlanItemMapper planItemMapper;


    /**
     * 查询护理计划
     * 
     * @param id 护理计划主键
     * @return 护理计划
     */
    @Override
    public NursingPlan selectNursingPlanById(Long id)
    {
        return nursingPlanMapper.selectNursingPlanById(id);
    }

    /**
     * 查询护理计划列表
     * 
     * @param nursingPlan 护理计划
     * @return 护理计划
     */
    @Override
    public List<NursingPlan> selectNursingPlanList(NursingPlan nursingPlan)
    {
        return nursingPlanMapper.selectNursingPlanList(nursingPlan);
    }

    /**
     * 新增护理计划
     * 
     * @param nursingPlan 护理计划
     * @return 结果
     */
    @Transactional
    @Override
    public int insertNursingPlan(NursingPlan nursingPlan)
    {
        // 1. 校验：护理项目不能重复
        List<NursingPlanItem> items = nursingPlan.getNursingPlanItemList();
        if (items != null && !items.isEmpty()) {
            Set<Long> itemIds = items.stream()
                    .map(NursingPlanItem::getItemId)
                    .filter(id -> id != null)
                    .collect(Collectors.toSet());
            if (itemIds.size() != items.size()) {
                throw new ServiceException("护理项目不能重复选择");
            }
        }

        // 2. 自动填充护理员创建人信息
        Nurse currentNurse = NurseUtils.getCurrentNurse();
        nursingPlan.setCreatorId(currentNurse.getId());
        nursingPlan.setCreatorName(currentNurse.getName());
        nursingPlan.setCreateTime(DateUtils.getNowDate());
        nursingPlan.setStatus(0L); // 默认启用

        // 3. 插入护理计划主表
        int rows = nursingPlanMapper.insertNursingPlan(nursingPlan);

        // 4. 批量插入护理计划-项目关联表
        if (items != null && !items.isEmpty()) {
            items.forEach(item -> {
                item.setPlanId(nursingPlan.getId()); // 关联计划ID
                planItemMapper.insertNursingPlanItem(item);
            });
        }

        return rows;
    }


    /**
     * 修改护理计划
     * 
     * @param nursingPlan 护理计划
     * @return 结果
     */
    @Transactional
    @Override
    public int updateNursingPlan(NursingPlan nursingPlan)
    {
        nursingPlan.setUpdateTime(DateUtils.getNowDate());
        nursingPlanMapper.deleteNursingPlanItemByPlanId(nursingPlan.getId());
        insertNursingPlanItem(nursingPlan);
        return nursingPlanMapper.updateNursingPlan(nursingPlan);
    }

    /**
     * 批量删除护理计划
     * 
     * @param ids 需要删除的护理计划主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteNursingPlanByIds(Long[] ids)
    {
        nursingPlanMapper.deleteNursingPlanItemByPlanIds(ids);
        return nursingPlanMapper.deleteNursingPlanByIds(ids);
    }

    /**
     * 删除护理计划信息
     * 
     * @param id 护理计划主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteNursingPlanById(Long id)
    {
        nursingPlanMapper.deleteNursingPlanItemByPlanId(id);
        return nursingPlanMapper.deleteNursingPlanById(id);
    }

    @Override
    public boolean checkPlanNameUnique(String planName) {
        return nursingPlanMapper.countPlanName(planName)==0;
    }

    /**
     * 新增护理计划-项目关联信息
     * 
     * @param nursingPlan 护理计划对象
     */
    public void insertNursingPlanItem(NursingPlan nursingPlan)
    {
        List<NursingPlanItem> nursingPlanItemList = nursingPlan.getNursingPlanItemList();
        Long id = nursingPlan.getId();
        if (StringUtils.isNotNull(nursingPlanItemList))
        {
            List<NursingPlanItem> list = new ArrayList<NursingPlanItem>();
            for (NursingPlanItem nursingPlanItem : nursingPlanItemList)
            {
                nursingPlanItem.setPlanId(id);
                list.add(nursingPlanItem);
            }
            if (list.size() > 0)
            {
                nursingPlanMapper.batchNursingPlanItem(list);
            }
        }
    }
}
