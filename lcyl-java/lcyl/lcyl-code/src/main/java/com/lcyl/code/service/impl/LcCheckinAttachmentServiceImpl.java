package com.lcyl.code.service.impl;

import java.util.List;
import com.lcyl.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lcyl.code.mapper.LcCheckinAttachmentMapper;
import com.lcyl.code.domain.LcCheckinAttachment;
import com.lcyl.code.service.ILcCheckinAttachmentService;

/**
 * 资料上传Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
@Service
public class LcCheckinAttachmentServiceImpl implements ILcCheckinAttachmentService 
{
    @Autowired
    private LcCheckinAttachmentMapper lcCheckinAttachmentMapper;

    /**
     * 查询资料上传
     * 
     * @param id 资料上传主键
     * @return 资料上传
     */
    @Override
    public LcCheckinAttachment selectLcCheckinAttachmentById(Long id)
    {
        return lcCheckinAttachmentMapper.selectLcCheckinAttachmentById(id);
    }

    /**
     * 查询资料上传列表
     * 
     * @param lcCheckinAttachment 资料上传
     * @return 资料上传
     */
    @Override
    public List<LcCheckinAttachment> selectLcCheckinAttachmentList(LcCheckinAttachment lcCheckinAttachment)
    {
        return lcCheckinAttachmentMapper.selectLcCheckinAttachmentList(lcCheckinAttachment);
    }

    /**
     * 新增资料上传
     * 
     * @param lcCheckinAttachment 资料上传
     * @return 结果
     */
    @Override
    public int insertLcCheckinAttachment(LcCheckinAttachment lcCheckinAttachment)
    {
        lcCheckinAttachment.setCreateTime(DateUtils.getNowDate());
        return lcCheckinAttachmentMapper.insertLcCheckinAttachment(lcCheckinAttachment);
    }

    /**
     * 修改资料上传
     * 
     * @param lcCheckinAttachment 资料上传
     * @return 结果
     */
    @Override
    public int updateLcCheckinAttachment(LcCheckinAttachment lcCheckinAttachment)
    {
        return lcCheckinAttachmentMapper.updateLcCheckinAttachment(lcCheckinAttachment);
    }

    /**
     * 批量删除资料上传
     * 
     * @param ids 需要删除的资料上传主键
     * @return 结果
     */
    @Override
    public int deleteLcCheckinAttachmentByIds(Long[] ids)
    {
        return lcCheckinAttachmentMapper.deleteLcCheckinAttachmentByIds(ids);
    }

    /**
     * 删除资料上传信息
     * 
     * @param id 资料上传主键
     * @return 结果
     */
    @Override
    public int deleteLcCheckinAttachmentById(Long id)
    {
        return lcCheckinAttachmentMapper.deleteLcCheckinAttachmentById(id);
    }
}
