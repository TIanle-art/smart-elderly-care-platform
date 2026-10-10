package com.lcyl.code.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lcyl.code.domain.ServiceOrder;
import com.lcyl.code.domain.ServiceOrderRefund;
import com.lcyl.code.domain.dto.ServiceOrderRefundFailDTO;
import com.lcyl.code.mapper.ServiceOrderMapper;
import com.lcyl.code.mapper.ServiceOrderRefundMapper;
import com.lcyl.code.service.IServiceOrderRefundService;
import com.lcyl.common.exception.ServiceException;
import com.lcyl.common.utils.DateUtils;

/**
 * 退款管理Service业务层处理
 *
 * @author ruoyi
 * @date 2026-03-21
 */
@Service
public class ServiceOrderRefundServiceImpl implements IServiceOrderRefundService
{
    @Autowired
    private ServiceOrderRefundMapper serviceOrderRefundMapper;

    @Autowired
    private ServiceOrderMapper serviceOrderMapper;

    /**
     * 查询退款管理
     *
     * @param id 退款管理主键
     * @return 退款管理
     */
    @Override
    public ServiceOrderRefund selectServiceOrderRefundById(Long id)
    {
        return serviceOrderRefundMapper.selectServiceOrderRefundById(id);
    }

    /**
     * 查询退款管理列表
     *
     * @param serviceOrderRefund 退款管理
     * @return 退款管理
     */
    @Override
    public List<ServiceOrderRefund> selectServiceOrderRefundList(ServiceOrderRefund serviceOrderRefund)
    {
        return serviceOrderRefundMapper.selectServiceOrderRefundList(serviceOrderRefund);
    }

    /**
     * 新增退款管理
     *
     * @param serviceOrderRefund 退款管理
     * @return 结果
     */
    @Override
    public int insertServiceOrderRefund(ServiceOrderRefund serviceOrderRefund)
    {
        serviceOrderRefund.setCreateTime(DateUtils.getNowDate());
        return serviceOrderRefundMapper.insertServiceOrderRefund(serviceOrderRefund);
    }

    /**
     * 修改退款管理
     *
     * @param serviceOrderRefund 退款管理
     * @return 结果
     */
    @Override
    public int updateServiceOrderRefund(ServiceOrderRefund serviceOrderRefund)
    {
        serviceOrderRefund.setUpdateTime(DateUtils.getNowDate());
        return serviceOrderRefundMapper.updateServiceOrderRefund(serviceOrderRefund);
    }

    /**
     * 批量删除退款管理
     *
     * @param ids 需要删除的退款管理主键
     * @return 结果
     */
    @Override
    public int deleteServiceOrderRefundByIds(Long[] ids)
    {
        return serviceOrderRefundMapper.deleteServiceOrderRefundByIds(ids);
    }

    /**
     * 删除退款管理信息
     *
     * @param id 退款管理主键
     * @return 结果
     */
    @Override
    public int deleteServiceOrderRefundById(Long id)
    {
        return serviceOrderRefundMapper.deleteServiceOrderRefundById(id);
    }

    /**
     * 退款成功
     *
     * @param id 退款记录主键
     * @return 结果
     */
    @Override
    public int refundSuccess(Long id)
    {
        ServiceOrderRefund refund = serviceOrderRefundMapper.selectServiceOrderRefundById(id);
        if (refund == null)
        {
            throw new ServiceException("退款记录不存在");
        }
        if (!"0".equals(refund.getRefundStatus()))
        {
            throw new ServiceException("当前退款记录不是处理中状态");
        }

        ServiceOrder order = serviceOrderMapper.selectServiceOrderById(refund.getOrderId());
        if (order == null)
        {
            throw new ServiceException("关联订单不存在");
        }

        refund.setRefundStatus("1");
        refund.setRefundTime(DateUtils.getNowDate());
        refund.setFailCode(null);
        refund.setFailReason(null);
        refund.setUpdateTime(DateUtils.getNowDate());
        serviceOrderRefundMapper.updateServiceOrderRefund(refund);

        order.setOrderStatus("5");
        order.setTradeStatus("3");
        order.setUpdateTime(DateUtils.getNowDate());
        return serviceOrderMapper.updateServiceOrder(order);
    }

    /**
     * 退款失败
     *
     * @param dto 退款失败参数
     * @return 结果
     */
    @Override
    public int refundFail(ServiceOrderRefundFailDTO dto)
    {
        if (dto == null || dto.getRefundId() == null)
        {
            throw new ServiceException("退款记录ID不能为空");
        }
        if (dto.getFailReason() == null || "".equals(dto.getFailReason().trim()))
        {
            throw new ServiceException("失败原因不能为空");
        }

        ServiceOrderRefund refund = serviceOrderRefundMapper.selectServiceOrderRefundById(dto.getRefundId());
        if (refund == null)
        {
            throw new ServiceException("退款记录不存在");
        }
        if (!"0".equals(refund.getRefundStatus()))
        {
            throw new ServiceException("当前退款记录不是处理中状态");
        }

        ServiceOrder order = serviceOrderMapper.selectServiceOrderById(refund.getOrderId());
        if (order == null)
        {
            throw new ServiceException("关联订单不存在");
        }

        refund.setRefundStatus("2");
        refund.setFailCode(dto.getFailCode());
        refund.setFailReason(dto.getFailReason().trim());
        refund.setUpdateTime(DateUtils.getNowDate());
        serviceOrderRefundMapper.updateServiceOrderRefund(refund);

        order.setTradeStatus("4");
        order.setUpdateTime(DateUtils.getNowDate());
        return serviceOrderMapper.updateServiceOrder(order);
    }
}
