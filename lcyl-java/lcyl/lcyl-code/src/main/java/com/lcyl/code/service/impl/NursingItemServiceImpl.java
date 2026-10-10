package com.lcyl.code.service.impl;

import java.util.List;

import com.lcyl.code.domain.Nurse;
import com.lcyl.code.utils.NurseUtils;
import com.lcyl.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lcyl.code.mapper.NursingItemMapper;
import com.lcyl.code.domain.NursingItem;
import com.lcyl.code.service.INursingItemService;

/**
 * 护理项目Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
@Service
public class NursingItemServiceImpl implements INursingItemService 
{
    @Autowired
    private NursingItemMapper nursingItemMapper;

    /**
     * 查询护理项目
     * 
     * @param id 护理项目主键
     * @return 护理项目
     */
    @Override
    public NursingItem selectNursingItemById(Long id)
    {
        return nursingItemMapper.selectNursingItemById(id);
    }

    /**
     * 查询护理项目列表
     * 
     * @param nursingItem 护理项目
     * @return 护理项目
     */
    @Override
    public List<NursingItem> selectNursingItemList(NursingItem nursingItem)
    {
        return nursingItemMapper.selectNursingItemList(nursingItem);
    }

    /**
     * 新增护理项目
     * 
     * @param nursingItem 护理项目
     * @return 结果
     */
    @Override
    public int insertNursingItem(NursingItem nursingItem)
    {
        // 1. 获取当前登录的护理员
        Nurse currentNurse = NurseUtils.getCurrentNurse();

        // 2. 自动填充创建人ID和姓名
        nursingItem.setCreatorId(currentNurse.getId());
        nursingItem.setCreatorName(currentNurse.getName());
        nursingItem.setCreateTime(DateUtils.getNowDate());
        return nursingItemMapper.insertNursingItem(nursingItem);
    }

    /**
     * 修改护理项目
     * 
     * @param nursingItem 护理项目
     * @return 结果
     */
    @Override
    public int updateNursingItem(NursingItem nursingItem)
    {
        nursingItem.setCreatorId(null);
        nursingItem.setCreatorName(null);
        nursingItem.setUpdateTime(DateUtils.getNowDate());
        return nursingItemMapper.updateNursingItem(nursingItem);
    }

    /**
     * 批量删除护理项目
     * 
     * @param ids 需要删除的护理项目主键
     * @return 结果
     */
    @Override
    public int deleteNursingItemByIds(Long[] ids)
    {
        return nursingItemMapper.deleteNursingItemByIds(ids);
    }

    /**
     * 删除护理项目信息
     * 
     * @param id 护理项目主键
     * @return 结果
     */
    @Override
    public int deleteNursingItemById(Long id)
    {
        return nursingItemMapper.deleteNursingItemById(id);
    }

    @Override
    public boolean checkItemNameUnique(String itemName) {
        return nursingItemMapper.countItemName(itemName)==0;
    }
}
