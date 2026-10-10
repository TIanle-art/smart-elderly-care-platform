package com.lcyl.code.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lcyl.code.domain.Bill;
import com.lcyl.code.domain.BillItem;
import com.lcyl.code.domain.Member;
import com.lcyl.code.domain.ServiceOrder;
import com.lcyl.code.domain.ServiceOrderRefund;
import com.lcyl.code.domain.dto.ServiceOrderCancelDTO;
import com.lcyl.code.domain.dto.ServiceOrderGenerateBillDTO;
import com.lcyl.code.domain.dto.ServiceOrderRefundApplyDTO;
import com.lcyl.code.domain.vo.ServiceOrderExecutionVO;
import com.lcyl.code.mapper.BillItemMapper;
import com.lcyl.code.mapper.BillMapper;
import com.lcyl.code.mapper.MemberMapper;
import com.lcyl.code.mapper.ServiceOrderMapper;
import com.lcyl.code.mapper.ServiceOrderRefundMapper;
import com.lcyl.code.service.IServiceOrderService;
import com.lcyl.common.exception.ServiceException;
import com.lcyl.common.utils.DateUtils;
import com.lcyl.common.utils.SecurityUtils;
import com.lcyl.common.utils.UserThreadLocal;
import com.lcyl.system.domain.Elder;
import com.lcyl.system.mapper.ElderMapper;

@Service
public class ServiceOrderServiceImpl implements IServiceOrderService
{
    @Autowired
    private ServiceOrderMapper serviceOrderMapper;

    @Autowired
    private ServiceOrderRefundMapper serviceOrderRefundMapper;

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private BillItemMapper billItemMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private ElderMapper elderMapper;

    @Override
    public ServiceOrder selectServiceOrderById(Long id)
    {
        return serviceOrderMapper.selectServiceOrderById(id);
    }

    @Override
    public List<ServiceOrder> selectServiceOrderList(ServiceOrder serviceOrder)
    {
        return serviceOrderMapper.selectServiceOrderList(serviceOrder);
    }

    @Override
    public int insertServiceOrder(ServiceOrder serviceOrder)
    {
        fillApplicantFromCurrentMember(serviceOrder);
        serviceOrder.setCreateTime(DateUtils.getNowDate());
        return serviceOrderMapper.insertServiceOrder(serviceOrder);
    }

    @Override
    public int updateServiceOrder(ServiceOrder serviceOrder)
    {
        serviceOrder.setUpdateTime(DateUtils.getNowDate());
        return serviceOrderMapper.updateServiceOrder(serviceOrder);
    }

    @Override
    public int deleteServiceOrderByIds(Long[] ids)
    {
        return serviceOrderMapper.deleteServiceOrderByIds(ids);
    }

    @Override
    public int deleteServiceOrderById(Long id)
    {
        return serviceOrderMapper.deleteServiceOrderById(id);
    }

    @Override
    public int cancelOrder(ServiceOrderCancelDTO dto)
    {
        if (dto == null || dto.getOrderId() == null)
        {
            throw new ServiceException("订单ID不能为空");
        }
        if (dto.getCancelReason() == null || "".equals(dto.getCancelReason().trim()))
        {
            throw new ServiceException("取消原因不能为空");
        }

        ServiceOrder order = serviceOrderMapper.selectServiceOrderById(dto.getOrderId());
        if (order == null)
        {
            throw new ServiceException("订单不存在");
        }
        if (!"0".equals(order.getOrderStatus()))
        {
            throw new ServiceException("当前订单状态不允许取消");
        }

        order.setOrderStatus("5");
        order.setTradeStatus("5");
        order.setCancelTime(DateUtils.getNowDate());
        order.setCancelReason(dto.getCancelReason().trim());
        order.setUpdateTime(DateUtils.getNowDate());

        return serviceOrderMapper.updateServiceOrder(order);
    }

    @Override
    public int applyRefund(ServiceOrderRefundApplyDTO dto)
    {
        if (dto == null || dto.getOrderId() == null)
        {
            throw new ServiceException("订单ID不能为空");
        }
        if (dto.getRefundReason() == null || "".equals(dto.getRefundReason().trim()))
        {
            throw new ServiceException("退款原因不能为空");
        }

        ServiceOrder order = serviceOrderMapper.selectServiceOrderById(dto.getOrderId());
        if (order == null)
        {
            throw new ServiceException("订单不存在");
        }

        String applicantType = dto.getApplicantType();
        if (applicantType == null || "".equals(applicantType))
        {
            applicantType = "2";
        }

        if ("2".equals(order.getTradeStatus()) || "3".equals(order.getTradeStatus()))
        {
            throw new ServiceException("当前订单已在退款流程中，请勿重复提交");
        }

        if ("1".equals(applicantType))
        {
            if (!"1".equals(order.getOrderStatus()) || !"1".equals(order.getTradeStatus()))
            {
                throw new ServiceException("家属仅可对已支付且未执行的订单申请退款");
            }
        }
        else
        {
            boolean allow = false;

            if ("1".equals(order.getOrderStatus()) && "1".equals(order.getTradeStatus()))
            {
                allow = true;
            }
            else if ("2".equals(order.getOrderStatus()) && "1".equals(order.getTradeStatus()))
            {
                allow = true;
            }

            if (!allow)
            {
                throw new ServiceException("当前订单不满足后台退款条件");
            }
        }

        ServiceOrderRefund refund = new ServiceOrderRefund();
        refund.setRefundNo(generateRefundNo());
        refund.setOrderId(order.getId());
        refund.setOrderNo(order.getOrderNo());
        refund.setRefundAmount(order.getOrderAmount());
        refund.setRefundStatus("0");
        refund.setApplicantId(order.getApplicantId());
        refund.setApplicantName(order.getApplicantName());
        refund.setApplicantType(applicantType);
        refund.setApplyTime(DateUtils.getNowDate());
        refund.setRefundReason(dto.getRefundReason().trim());
        refund.setRefundChannel("原路退回");
        refund.setRefundMethod("微信");
        refund.setDelFlag("0");
        refund.setCreateTime(DateUtils.getNowDate());

        int rows = serviceOrderRefundMapper.insertServiceOrderRefund(refund);
        if (rows <= 0)
        {
            throw new ServiceException("退款申请失败");
        }

        order.setTradeStatus("2");
        order.setUpdateTime(DateUtils.getNowDate());
        return serviceOrderMapper.updateServiceOrder(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int generateExpenseBill(ServiceOrderGenerateBillDTO dto)
    {
        if (dto == null || dto.getOrderId() == null)
        {
            throw new ServiceException("订单ID不能为空");
        }

        ServiceOrder order = serviceOrderMapper.selectServiceOrderById(dto.getOrderId());
        if (order == null)
        {
            throw new ServiceException("订单不存在");
        }
        if (!"3".equals(order.getOrderStatus()))
        {
            throw new ServiceException("仅已完成的服务订单可生成费用账单");
        }
        if (!"1".equals(order.getTradeStatus()))
        {
            throw new ServiceException("仅已支付的服务订单可生成费用账单");
        }
        if (order.getOrderAmount() == null || order.getOrderAmount().compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new ServiceException("订单金额异常，不能生成费用账单");
        }

        int count = billMapper.checkExpenseBillExists(order.getOrderNo());
        if (count > 0)
        {
            throw new ServiceException("该订单已生成费用账单，请勿重复生成");
        }

        Date now = DateUtils.getNowDate();
        Date serviceDate = order.getExpectedServiceTime() != null ? order.getExpectedServiceTime() : now;

        Bill bill = new Bill();
        Elder elder = order.getElderId() == null ? null : elderMapper.selectElderById(order.getElderId());
        bill.setBillNo(generateExpenseBillNo());
        bill.setBillType("2");
        bill.setBillMonth(formatBillMonth(serviceDate));
        bill.setElderId(order.getElderId());
        bill.setElderName(order.getElderName());
        bill.setElderIdCard(elder == null ? null : elder.getIdCardNo());
        bill.setBedId(order.getBedId());
        bill.setBedNo(order.getBedNo());
        bill.setBillAmount(order.getOrderAmount());
        bill.setPayableAmount(order.getOrderAmount());
        bill.setTradeStatus("1");
        bill.setPayDeadline(order.getPayTime() != null ? order.getPayTime() : now);
        bill.setStartDate(serviceDate);
        bill.setEndDate(serviceDate);
        bill.setDaysCount(1L);
        bill.setCreatorId(SecurityUtils.getUserId());
        bill.setCreatorName(SecurityUtils.getUsername());
        bill.setDelFlag("0");
        bill.setCreateBy(SecurityUtils.getUsername());
        bill.setCreateTime(now);
        bill.setUpdateBy(SecurityUtils.getUsername());
        bill.setUpdateTime(now);
        bill.setRemark("来源服务订单：" + order.getOrderNo());

        int rows = billMapper.insertBill(bill);
        if (rows <= 0 || bill.getId() == null)
        {
            throw new ServiceException("生成费用账单失败");
        }

        BillItem item = new BillItem();
        item.setBillId(bill.getId());
        item.setBillNo(bill.getBillNo());
        item.setItemType("1");
        item.setFeeName(order.getProjectName() == null || "".equals(order.getProjectName().trim()) ? "服务费用" : order.getProjectName());
        item.setServiceContent(buildServiceContent(order));
        item.setAmount(order.getOrderAmount());
        item.setSort(1);
        item.setRemark("服务订单生成费用账单");
        billItemMapper.insertBillItem(item);

        return rows;
    }

    @Override
    public int autoCancelTimeoutOrders()
    {
        return serviceOrderMapper.autoCancelTimeoutOrders();
    }

    @Override
    public ServiceOrderExecutionVO selectExecutionByOrderId(Long orderId)
    {
        return serviceOrderMapper.selectExecutionByOrderId(orderId);
    }

    private String generateRefundNo()
    {
        String random = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return "TK" + DateUtils.dateTimeNow("yyyyMMddHHmmss") + random;
    }

    private String generateExpenseBillNo()
    {
        return "FY" + DateUtils.dateTimeNow("yyyyMMddHHmmss") + ThreadLocalRandom.current().nextInt(100, 1000);
    }

    private String formatBillMonth(Date date)
    {
        return new SimpleDateFormat("yyyy-MM").format(date);
    }

    private String buildServiceContent(ServiceOrder order)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("来源订单：").append(order.getOrderNo());
        if (order.getExpectedServiceTime() != null)
        {
            builder.append("，服务时间：").append(new SimpleDateFormat("yyyy-MM-dd").format(order.getExpectedServiceTime()));
        }
        if (order.getRemark() != null && !"".equals(order.getRemark().trim()))
        {
            builder.append("，备注：").append(order.getRemark().trim());
        }
        return builder.toString();
    }

    private void fillApplicantFromCurrentMember(ServiceOrder serviceOrder)
    {
        Long memberId = UserThreadLocal.getUserId();
        if (memberId == null)
        {
            return;
        }

        Member member = memberMapper.selectById(memberId);
        if (member == null)
        {
            return;
        }

        serviceOrder.setOrderSource("1");
        serviceOrder.setApplicantId(memberId);
        serviceOrder.setApplicantName((member.getName() == null || "".equals(member.getName().trim())) ? member.getPhone() : member.getName().trim());
        serviceOrder.setApplicantPhone(member.getPhone());
    }
}
