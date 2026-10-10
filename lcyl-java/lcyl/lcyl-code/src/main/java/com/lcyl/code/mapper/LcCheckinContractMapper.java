package com.lcyl.code.mapper;

import java.util.List;
import com.lcyl.code.domain.LcCheckinContract;

/**
 * 签约办理Mapper接口
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
public interface LcCheckinContractMapper 
{
    /**
     * 查询签约办理
     * 
     * @param id 签约办理主键
     * @return 签约办理
     */
    public LcCheckinContract selectLcCheckinContractById(Long id);

    /**
     * 查询签约办理列表
     * 
     * @param lcCheckinContract 签约办理
     * @return 签约办理集合
     */
    public List<LcCheckinContract> selectLcCheckinContractList(LcCheckinContract lcCheckinContract);

    /**
     * 新增签约办理
     * 
     * @param lcCheckinContract 签约办理
     * @return 结果
     */
    public int insertLcCheckinContract(LcCheckinContract lcCheckinContract);

    /**
     * 修改签约办理
     * 
     * @param lcCheckinContract 签约办理
     * @return 结果
     */
    public int updateLcCheckinContract(LcCheckinContract lcCheckinContract);

    /**
     * 删除签约办理
     * 
     * @param id 签约办理主键
     * @return 结果
     */
    public int deleteLcCheckinContractById(Long id);

    /**
     * 批量删除签约办理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLcCheckinContractByIds(Long[] ids);
}
