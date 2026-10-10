package com.lcyl.code.service.impl;

import java.util.List;
import com.lcyl.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lcyl.code.mapper.BalanceMapper;
import com.lcyl.code.domain.Balance;
import com.lcyl.code.service.IBalanceService;

/**
 * 余额Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-27
 */
@Service
public class BalanceServiceImpl implements IBalanceService 
{
    @Autowired
    private BalanceMapper balanceMapper;

    /**
     * 查询余额
     * 
     * @param id 余额主键
     * @return 余额
     */
    @Override
    public Balance selectBalanceById(Long id)
    {
        return balanceMapper.selectBalanceById(id);
    }

    /**
     * 查询余额列表
     * 
     * @param balance 余额
     * @return 余额
     */
    @Override
    public List<Balance> selectBalanceList(Balance balance)
    {
        return balanceMapper.selectBalanceList(balance);
    }

    /**
     * 新增余额
     * 
     * @param balance 余额
     * @return 结果
     */
    @Override
    public int insertBalance(Balance balance)
    {
        balance.setCreateTime(DateUtils.getNowDate());
        return balanceMapper.insertBalance(balance);
    }

    /**
     * 修改余额
     * 
     * @param balance 余额
     * @return 结果
     */
    @Override
    public int updateBalance(Balance balance)
    {
        balance.setUpdateTime(DateUtils.getNowDate());
        return balanceMapper.updateBalance(balance);
    }

    /**
     * 批量删除余额
     * 
     * @param ids 需要删除的余额主键
     * @return 结果
     */
    @Override
    public int deleteBalanceByIds(Long[] ids)
    {
        return balanceMapper.deleteBalanceByIds(ids);
    }

    /**
     * 删除余额信息
     * 
     * @param id 余额主键
     * @return 结果
     */
    @Override
    public int deleteBalanceById(Long id)
    {
        return balanceMapper.deleteBalanceById(id);
    }

    @Override
    public Balance selectBalanceByElderId(Long elderId) {
        return balanceMapper.selectBalanceByElderId(elderId);
    }
}
