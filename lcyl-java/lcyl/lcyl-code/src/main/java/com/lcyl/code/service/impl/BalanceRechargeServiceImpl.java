package com.lcyl.code.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import com.lcyl.code.domain.dto.BalanceRechargeDTO;
import com.lcyl.code.domain.vo.RechargeElderOptionVO;
import com.lcyl.common.exception.ServiceException;
import com.lcyl.common.utils.DateUtils;
import com.lcyl.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lcyl.code.mapper.BalanceRechargeMapper;
import com.lcyl.code.domain.BalanceRecharge;
import com.lcyl.code.service.IRechargeService;

/**
 * 预缴款充值记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-27
 */
@Service
public class BalanceRechargeServiceImpl implements IRechargeService
{
    @Autowired
    private BalanceRechargeMapper balanceRechargeMapper;

    /**
     * 查询预缴款充值记录
     * 
     * @param id 预缴款充值记录主键
     * @return 预缴款充值记录
     */
    @Override
    public BalanceRecharge selectBalanceRechargeById(Long id)
    {
        return balanceRechargeMapper.selectBalanceRechargeById(id);
    }

    /**
     * 查询预缴款充值记录列表
     * 
     * @param balanceRecharge 预缴款充值记录
     * @return 预缴款充值记录
     */
    @Override
    public List<BalanceRecharge> selectBalanceRechargeList(BalanceRecharge balanceRecharge)
    {
        return balanceRechargeMapper.selectBalanceRechargeList(balanceRecharge);
    }

    /**
     * 新增预缴款充值记录
     * 
     * @param balanceRecharge 预缴款充值记录
     * @return 结果
     */
    @Override
    public int insertBalanceRecharge(BalanceRecharge balanceRecharge)
    {
        balanceRecharge.setCreateTime(DateUtils.getNowDate());
        return balanceRechargeMapper.insertBalanceRecharge(balanceRecharge);
    }

    /**
     * 修改预缴款充值记录
     * 
     * @param balanceRecharge 预缴款充值记录
     * @return 结果
     */
    @Override
    public int updateBalanceRecharge(BalanceRecharge balanceRecharge)
    {
        balanceRecharge.setUpdateTime(DateUtils.getNowDate());
        return balanceRechargeMapper.updateBalanceRecharge(balanceRecharge);
    }

    /**
     * 批量删除预缴款充值记录
     * 
     * @param ids 需要删除的预缴款充值记录主键
     * @return 结果
     */
    @Override
    public int deleteBalanceRechargeByIds(Long[] ids)
    {
        return balanceRechargeMapper.deleteBalanceRechargeByIds(ids);
    }

    /**
     * 删除预缴款充值记录信息
     * 
     * @param id 预缴款充值记录主键
     * @return 结果
     */
    @Override
    public int deleteBalanceRechargeById(Long id)
    {
        return balanceRechargeMapper.deleteBalanceRechargeById(id);
    }

    @Override
    public List<RechargeElderOptionVO> selectRechargeElderOptions()
    {
        return balanceRechargeMapper.selectRechargeElderOptions();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int rechargeBalance(BalanceRechargeDTO dto)
    {
        if (dto == null)
        {
            throw new ServiceException("充值参数不能为空");
        }
        if (dto.getElderId() == null && dto.getElderName() != null && !"".equals(dto.getElderName().trim()))
        {
            dto.setElderId(balanceRechargeMapper.selectElderIdByName(dto.getElderName().trim()));
        }
        if (dto.getElderId() == null)
        {
            throw new ServiceException("老人ID不能为空");
        }
        if (dto.getElderName() == null || "".equals(dto.getElderName().trim()))
        {
            throw new ServiceException("老人姓名不能为空");
        }
        if (dto.getRechargeMethod() == null || "".equals(dto.getRechargeMethod().trim()))
        {
            throw new ServiceException("充值方式不能为空");
        }
        if (dto.getRechargeAmount() == null || dto.getRechargeAmount().compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new ServiceException("充值金额必须大于0");
        }

        dto.setRechargeNo(generateRechargeNo());
        dto.setStatus("0");
        dto.setCreateBy(SecurityUtils.getUsername());
        dto.setCreateTime(DateUtils.getNowDate());
        dto.setUpdateBy(SecurityUtils.getUsername());
        dto.setUpdateTime(DateUtils.getNowDate());
        dto.setDelFlag("0");
        dto.setRemark("预缴款充值");

        int rows = balanceRechargeMapper.insertRechargeRecord(dto);
        if (rows <= 0)
        {
            throw new ServiceException("充值记录保存失败");
        }

        int count = balanceRechargeMapper.countBalanceByElderId(dto.getElderId());
        if (count > 0)
        {
            balanceRechargeMapper.updateBalancePrepaid(
                dto.getElderId(),
                dto.getRechargeAmount(),
                dto.getElderName(),
                dto.getBedNo(),
                SecurityUtils.getUserId()
            );
        }
        else
        {
            balanceRechargeMapper.insertBalanceAccountByRecharge(dto, SecurityUtils.getUserId());
        }

        return rows;
    }

    private String generateRechargeNo()
    {
        return "CZ" + DateUtils.dateTimeNow("yyyyMMddHHmmss") + (new Random().nextInt(900) + 100);
    }
}
