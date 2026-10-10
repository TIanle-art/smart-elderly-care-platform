package com.lcyl.code.service;

import java.util.List;
import com.lcyl.code.domain.LcCheckinAttachment;

/**
 * 资料上传Service接口
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
public interface ILcCheckinAttachmentService 
{
    /**
     * 查询资料上传
     * 
     * @param id 资料上传主键
     * @return 资料上传
     */
    public LcCheckinAttachment selectLcCheckinAttachmentById(Long id);

    /**
     * 查询资料上传列表
     * 
     * @param lcCheckinAttachment 资料上传
     * @return 资料上传集合
     */
    public List<LcCheckinAttachment> selectLcCheckinAttachmentList(LcCheckinAttachment lcCheckinAttachment);

    /**
     * 新增资料上传
     * 
     * @param lcCheckinAttachment 资料上传
     * @return 结果
     */
    public int insertLcCheckinAttachment(LcCheckinAttachment lcCheckinAttachment);

    /**
     * 修改资料上传
     * 
     * @param lcCheckinAttachment 资料上传
     * @return 结果
     */
    public int updateLcCheckinAttachment(LcCheckinAttachment lcCheckinAttachment);

    /**
     * 批量删除资料上传
     * 
     * @param ids 需要删除的资料上传主键集合
     * @return 结果
     */
    public int deleteLcCheckinAttachmentByIds(Long[] ids);

    /**
     * 删除资料上传信息
     * 
     * @param id 资料上传主键
     * @return 结果
     */
    public int deleteLcCheckinAttachmentById(Long id);
}
