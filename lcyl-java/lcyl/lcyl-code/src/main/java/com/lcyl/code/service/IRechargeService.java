package com.lcyl.code.service;

import java.util.List;
import com.lcyl.code.domain.BalanceRecharge;
import com.lcyl.code.domain.dto.BalanceRechargeDTO;
import com.lcyl.code.domain.vo.RechargeElderOptionVO;

/**
 * 预缴款充值记录Service接口
 * 
 * @author ruoyi
 * @date 2026-03-27
 */
public interface IRechargeService
{
    /**
     * 查询预缴款充值记录
     * 
     * @param id 预缴款充值记录主键
     * @return 预缴款充值记录
     */
    public BalanceRecharge selectBalanceRechargeById(Long id);

    /**
     * 查询预缴款充值记录列表
     * 
     * @param balanceRecharge 预缴款充值记录
     * @return 预缴款充值记录集合
     */
    public List<BalanceRecharge> selectBalanceRechargeList(BalanceRecharge balanceRecharge);

    /**
     * 新增预缴款充值记录
     * 
     * @param balanceRecharge 预缴款充值记录
     * @return 结果
     */
    public int insertBalanceRecharge(BalanceRecharge balanceRecharge);

    /**
     * 修改预缴款充值记录
     * 
     * @param balanceRecharge 预缴款充值记录
     * @return 结果
     */
    public int updateBalanceRecharge(BalanceRecharge balanceRecharge);

    /**
     * 批量删除预缴款充值记录
     * 
     * @param ids 需要删除的预缴款充值记录主键集合
     * @return 结果
     */
    public int deleteBalanceRechargeByIds(Long[] ids);

    /**
     * 删除预缴款充值记录信息
     * 
     * @param id 预缴款充值记录主键
     * @return 结果
     */
    public int deleteBalanceRechargeById(Long id);

    /**
     * 查询充值可选老人列表
     *
     * @return 老人选项
     */
    public List<RechargeElderOptionVO> selectRechargeElderOptions();

    /**
     * 预缴款充值
     *
     * @param dto 充值参数
     * @return 结果
     */
    public int rechargeBalance(BalanceRechargeDTO dto);
}
