package com.lcyl.code.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.lcyl.code.domain.Balance;
import com.lcyl.code.domain.Bill;
import com.lcyl.code.domain.BillItem;
import com.lcyl.code.domain.dto.BillCancelDTO;
import com.lcyl.code.domain.dto.BillGenerateDTO;
import com.lcyl.code.domain.dto.BillPayDTO;
import com.lcyl.code.domain.vo.BillGenerateElderVO;
import com.lcyl.code.mapper.BalanceMapper;
import com.lcyl.code.mapper.BillItemMapper;
import com.lcyl.code.mapper.BillMapper;
import com.lcyl.code.service.IBillService;
import com.lcyl.common.exception.ServiceException;
import com.lcyl.common.utils.DateUtils;
import com.lcyl.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BillServiceImpl implements IBillService
{
    @Autowired
    private BillMapper billMapper;

    @Autowired
    private BillItemMapper billItemMapper;

    @Autowired
    private BalanceMapper balanceMapper;

    @Override
    public Bill selectBillById(Long id)
    {
        return billMapper.selectBillById(id);
    }

    @Override
    public List<Bill> selectBillList(Bill bill)
    {
        return billMapper.selectBillList(bill);
    }

    @Override
    public int insertBill(Bill bill)
    {
        bill.setCreateTime(DateUtils.getNowDate());
        return billMapper.insertBill(bill);
    }

    @Override
    public int updateBill(Bill bill)
    {
        bill.setUpdateTime(DateUtils.getNowDate());
        return billMapper.updateBill(bill);
    }

    @Override
    public int deleteBillByIds(Long[] ids)
    {
        return billMapper.deleteBillByIds(ids);
    }

    @Override
    public int deleteBillById(Long id)
    {
        return billMapper.deleteBillById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int generateMonthlyBill(BillGenerateDTO dto)
    {
        if (dto == null || dto.getElderId() == null)
        {
            throw new ServiceException("老人ID不能为空");
        }
        if (dto.getBillMonth() == null || "".equals(dto.getBillMonth().trim()))
        {
            throw new ServiceException("账单月份不能为空");
        }

        String billMonth = dto.getBillMonth().trim();
        int count = billMapper.checkMonthlyBillExists(dto.getElderId(), billMonth);
        if (count > 0)
        {
            throw new ServiceException("该老人该月份月度账单已生成，请勿重复生成");
        }

        BillGenerateElderVO elderInfo = billMapper.selectGenerateElderInfo(dto.getElderId());
        if (elderInfo == null)
        {
            throw new ServiceException("老人信息不存在");
        }

        YearMonth yearMonth;
        try
        {
            yearMonth = YearMonth.parse(billMonth);
        }
        catch (Exception e)
        {
            throw new ServiceException("账单月份格式错误，应为yyyy-MM");
        }

        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();
        LocalDate deadline = yearMonth.plusMonths(1).atDay(7);

        BigDecimal roomTypePrice = billMapper.selectRoomTypePriceByElderId(dto.getElderId());
        if (roomTypePrice == null)
        {
            roomTypePrice = BigDecimal.ZERO;
        }

        BigDecimal nursingMonthlyFee = billMapper.selectNursingMonthlyFeeByElderId(dto.getElderId());
        if (nursingMonthlyFee == null)
        {
            nursingMonthlyFee = BigDecimal.ZERO;
        }

        BigDecimal billAmount = roomTypePrice.add(nursingMonthlyFee);
        Balance balance = balanceMapper.selectBalanceByElderId(dto.getElderId());
        BigDecimal availablePrepaid = balance != null && balance.getPrepaidBalance() != null
            ? balance.getPrepaidBalance()
            : BigDecimal.ZERO;
        BigDecimal prepaidDeduction = availablePrepaid.min(billAmount);
        BigDecimal payableAmount = billAmount.subtract(prepaidDeduction);

        Bill bill = new Bill();
        bill.setBillNo(generateBillNo());
        bill.setBillType("1");
        bill.setBillMonth(billMonth);
        bill.setElderId(elderInfo.getElderId());
        bill.setElderName(elderInfo.getElderName());
        bill.setElderIdCard(elderInfo.getElderIdCard());
        bill.setBedNo(elderInfo.getBedNo());
        bill.setBillAmount(billAmount);
        bill.setPayableAmount(payableAmount);
        bill.setTradeStatus("0");
        bill.setStartDate(Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        bill.setEndDate(Date.from(end.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        bill.setPayDeadline(Date.from(deadline.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        bill.setDaysCount((long) yearMonth.lengthOfMonth());
        bill.setCreatorId(SecurityUtils.getUserId());
        bill.setCreatorName(SecurityUtils.getUsername());
        bill.setDelFlag("0");
        bill.setCreateBy(SecurityUtils.getUsername());
        bill.setCreateTime(DateUtils.getNowDate());
        bill.setUpdateBy(SecurityUtils.getUsername());
        bill.setUpdateTime(DateUtils.getNowDate());
        bill.setRemark("系统生成月度账单");

        int rows = billMapper.insertBill(bill);
        if (rows <= 0 || bill.getId() == null)
        {
            throw new ServiceException("生成账单失败");
        }

        insertBillItem(bill, "1", "房位费", billMonth + "房位费用", roomTypePrice, 1);
        insertBillItem(bill, "1", "护理费", billMonth + "护理服务费用", nursingMonthlyFee, 2);
        if (prepaidDeduction.compareTo(BigDecimal.ZERO) > 0)
        {
            insertBillItem(bill, "2", "预缴款抵扣", "使用预缴账户余额抵扣本月账单", prepaidDeduction, 3);
            syncPrepaidBalanceOnGenerate(balance, bill, prepaidDeduction);
        }

        syncArrearsOnGenerate(bill);
        return rows;
    }

    private void insertBillItem(Bill bill, String itemType, String feeName, String serviceContent, BigDecimal amount, Integer sort)
    {
        BillItem item = new BillItem();
        item.setBillId(bill.getId());
        item.setBillNo(bill.getBillNo());
        item.setItemType(itemType);
        item.setFeeName(feeName);
        item.setServiceContent(serviceContent);
        item.setAmount(amount);
        item.setSort(sort);
        item.setRemark("系统生成账单明细");
        billItemMapper.insertBillItem(item);
    }

    private String generateBillNo()
    {
        return "ZD" + DateUtils.dateTimeNow("yyyyMMddHHmmss") + (new Random().nextInt(900) + 100);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cancelBill(BillCancelDTO dto)
    {
        if (dto == null || dto.getBillId() == null)
        {
            throw new ServiceException("账单ID不能为空");
        }
        if (dto.getCancelReason() == null || "".equals(dto.getCancelReason().trim()))
        {
            throw new ServiceException("取消原因不能为空");
        }

        Bill bill = billMapper.selectBillById(dto.getBillId());
        if (bill == null)
        {
            throw new ServiceException("账单不存在");
        }
        if (!"0".equals(bill.getTradeStatus()))
        {
            throw new ServiceException("当前账单不是待支付状态，不能取消");
        }

        bill.setTradeStatus("2");
        bill.setCancelReason(dto.getCancelReason().trim());
        bill.setCancelTime(DateUtils.getNowDate());
        bill.setUpdateBy(SecurityUtils.getUsername());
        bill.setUpdateTime(DateUtils.getNowDate());

        int rows = billMapper.updateBill(bill);
        if (rows > 0 && "1".equals(bill.getBillType()))
        {
            syncArrearsOnSettle(bill);
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int payBill(BillPayDTO dto)
    {
        if (dto == null || dto.getBillId() == null)
        {
            throw new ServiceException("账单ID不能为空");
        }
        if (dto.getPayMethod() == null || "".equals(dto.getPayMethod().trim()))
        {
            throw new ServiceException("支付方式不能为空");
        }
        if (dto.getVoucherUrl() == null || "".equals(dto.getVoucherUrl().trim()))
        {
            throw new ServiceException("支付凭证不能为空");
        }
        if (dto.getPayRemark() == null || "".equals(dto.getPayRemark().trim()))
        {
            throw new ServiceException("支付备注不能为空");
        }

        Bill bill = billMapper.selectBillById(dto.getBillId());
        if (bill == null)
        {
            throw new ServiceException("账单不存在");
        }
        if (!"0".equals(bill.getTradeStatus()))
        {
            throw new ServiceException("当前账单不是待支付状态，不能支付");
        }
        if (!"1".equals(bill.getBillType()))
        {
            throw new ServiceException("当前仅支持月度账单支付");
        }

        dto.setBillNo(bill.getBillNo());
        dto.setPaymentNo(generatePaymentNo());
        dto.setPayTime(DateUtils.getNowDate());
        dto.setPayChannel("2");
        dto.setPayAmount(bill.getPayableAmount());
        dto.setOperatorId(SecurityUtils.getUserId());
        dto.setOperatorName(SecurityUtils.getUsername());
        dto.setCreateBy(SecurityUtils.getUsername());
        dto.setCreateTime(DateUtils.getNowDate());

        billMapper.insertBillPaymentRecord(dto);

        bill.setTradeStatus("1");
        bill.setUpdateBy(SecurityUtils.getUsername());
        bill.setUpdateTime(DateUtils.getNowDate());

        int rows = billMapper.updateBill(bill);
        if (rows > 0)
        {
            syncArrearsOnSettle(bill);
        }
        return rows;
    }

    @Override
    public List<Bill> selectPendingBillsByElderId(Long elderId) {
        return billMapper.selectPendingBillsByElderId(elderId);
    }

    private String generatePaymentNo()
    {
        return "FK" + DateUtils.dateTimeNow("yyyyMMddHHmmss") + (new Random().nextInt(900) + 100);
    }

    private void syncArrearsOnGenerate(Bill bill)
    {
        if (bill.getElderId() == null || bill.getPayableAmount() == null)
        {
            return;
        }
        Balance balance = balanceMapper.selectBalanceByElderId(bill.getElderId());
        if (balance == null)
        {
            Balance newBalance = new Balance();
            newBalance.setPrepaidBalance(BigDecimal.ZERO);
            newBalance.setDepositAmount(BigDecimal.ZERO);
            newBalance.setArrearsAmount(bill.getPayableAmount());
            newBalance.setPaymentDeadline(bill.getPayDeadline());
            newBalance.setStatus(0L);
            newBalance.setElderId(bill.getElderId());
            newBalance.setElderName(bill.getElderName());
            newBalance.setBedNo(bill.getBedNo());
            newBalance.setCreateTime(DateUtils.getNowDate());
            newBalance.setUpdateTime(DateUtils.getNowDate());
            newBalance.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
            newBalance.setUpdateBy(String.valueOf(SecurityUtils.getUserId()));
            newBalance.setRemark("账单生成初始化账户");
            newBalance.setDelFlag("0");
            balanceMapper.insertBalanceForBill(newBalance);
        }
        else
        {
            balanceMapper.increaseArrearsAmount(
                bill.getElderId(),
                bill.getElderName(),
                bill.getBedNo(),
                bill.getPayableAmount(),
                bill.getPayDeadline(),
                SecurityUtils.getUserId()
            );
        }
    }

    private void syncArrearsOnSettle(Bill bill)
    {
        if (bill.getElderId() == null || bill.getPayableAmount() == null)
        {
            return;
        }
        balanceMapper.decreaseArrearsAmount(
            bill.getElderId(),
            bill.getPayableAmount(),
            SecurityUtils.getUserId()
        );
    }

    private void syncPrepaidBalanceOnGenerate(Balance balance, Bill bill, BigDecimal prepaidDeduction)
    {
        if (balance == null || balance.getId() == null || prepaidDeduction == null || prepaidDeduction.compareTo(BigDecimal.ZERO) <= 0)
        {
            return;
        }

        BigDecimal currentPrepaid = balance.getPrepaidBalance() == null ? BigDecimal.ZERO : balance.getPrepaidBalance();
        BigDecimal remainPrepaid = currentPrepaid.subtract(prepaidDeduction);
        if (remainPrepaid.compareTo(BigDecimal.ZERO) < 0)
        {
            remainPrepaid = BigDecimal.ZERO;
        }

        balance.setPrepaidBalance(remainPrepaid);
        balance.setElderName(bill.getElderName());
        balance.setBedNo(bill.getBedNo());
        balance.setUpdateBy(String.valueOf(SecurityUtils.getUserId()));
        balance.setUpdateTime(DateUtils.getNowDate());
        balanceMapper.updateBalance(balance);
    }

}
