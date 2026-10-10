package com.lcyl.code.service;

import java.util.List;
import com.lcyl.code.domain.Balance;

/**
 * 余额Service接口
 * 
 * @author ruoyi
 * @date 2026-03-27
 */
public interface IBalanceService 
{
    /**
     * 查询余额
     * 
     * @param id 余额主键
     * @return 余额
     */
    public Balance selectBalanceById(Long id);

    /**
     * 查询余额列表
     * 
     * @param balance 余额
     * @return 余额集合
     */
    public List<Balance> selectBalanceList(Balance balance);

    /**
     * 新增余额
     * 
     * @param balance 余额
     * @return 结果
     */
    public int insertBalance(Balance balance);

    /**
     * 修改余额
     * 
     * @param balance 余额
     * @return 结果
     */
    public int updateBalance(Balance balance);

    /**
     * 批量删除余额
     * 
     * @param ids 需要删除的余额主键集合
     * @return 结果
     */
    public int deleteBalanceByIds(Long[] ids);

    /**
     * 删除余额信息
     * 
     * @param id 余额主键
     * @return 结果
     */
    public int deleteBalanceById(Long id);

    Balance selectBalanceByElderId(Long elderId);
}
