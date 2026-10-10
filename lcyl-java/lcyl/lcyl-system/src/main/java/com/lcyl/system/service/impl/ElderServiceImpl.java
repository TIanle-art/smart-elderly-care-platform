package com.lcyl.system.service.impl;

import com.lcyl.common.utils.DateUtils;
import com.lcyl.common.utils.UserThreadLocal;
import com.lcyl.system.domain.Elder;
import com.lcyl.system.domain.dto.AddInfo;
import com.lcyl.system.domain.vo.BedVO;
import com.lcyl.system.mapper.ElderMapper;
import com.lcyl.system.service.IElderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;
import java.util.List;

/**
 * 老人信息Service业务层处理
 * 
 * @author tyf
 * @date 2026-03-24
 */
@Service
public class ElderServiceImpl implements IElderService 
{
    @Autowired
    private ElderMapper elderMapper;

    /**
     * 查询老人信息
     * 
     * @param id 老人信息主键
     * @return 老人信息
     */
    @Override
    public Elder selectElderById(Long id)
    {
        return elderMapper.selectElderById(id);
    }

    /**
     * 查询老人信息列表
     * 
     * @param elder 老人信息
     * @return 老人信息
     */
    @Override
    public List<Elder> selectElderList(Elder elder)
    {
        return elderMapper.selectElderList(elder);
    }

    /**
     * 新增老人信息
     * 
     * @param elder 老人信息
     * @return 结果
     */
    @Override
    public int insertElder(Elder elder)
    {
        elder.setCreateTime(DateUtils.getNowDate());
        return elderMapper.insertElder(elder);
    }

    /**
     * 修改老人信息
     * 
     * @param elder 老人信息
     * @return 结果
     */
    @Override
    public int updateElder(Elder elder)
    {
        elder.setUpdateTime(DateUtils.getNowDate());
        return elderMapper.updateElder(elder);
    }

    /**
     * 批量删除老人信息
     * 
     * @param ids 需要删除的老人信息主键
     * @return 结果
     */
    @Override
    public int deleteElderByIds(Long[] ids)
    {
        return elderMapper.deleteElderByIds(ids);
    }

    /**
     * 删除老人信息信息
     * 
     * @param id 老人信息主键
     * @return 结果
     */
    @Override
    public int deleteElderById(Long id)
    {
        return elderMapper.deleteElderById(id);
    }

    @Override
    public List<Elder> selectElderByUser(Long memberId) {

        List<Elder> elderList= elderMapper.selectElderByMember(memberId);
        return elderList;
    }

    @Override
    public List<Elder> selectAllElder() {
        List<Elder> elderList= elderMapper.selectAllElderList();
        return elderList;
    }

    @Override
    public String insertInfo(AddInfo addInfo) {
        Long memberId = UserThreadLocal.getUserId();
        List<Long> info=elderMapper.selectElderInfo(memberId);

        if(info.contains(addInfo.getElderId())){

            return "该老人已添加";
        }

        Integer add=elderMapper.insertInfo(addInfo,memberId);
        if(add==1){
            return "添加成功";
        }else {
            return "添加失败";
        }
    }


    @Override
    public List<BedVO> getElderBedList() {
        Long memberId = UserThreadLocal.getUserId();
        List<BedVO> bedVO =  elderMapper.selectElderBedList(memberId);
        return bedVO;
    }

    @Override
    public Integer deleteElderInfoById(Long id) {
        return elderMapper.delElderInfoById(id);
    }
}
