package com.lcyl.code.service;

import java.util.List;
import com.lcyl.code.domain.ServiceOrder;
import com.lcyl.code.domain.dto.ServiceOrderCancelDTO;
import com.lcyl.code.domain.dto.ServiceOrderGenerateBillDTO;
import com.lcyl.code.domain.dto.ServiceOrderRefundApplyDTO;
import com.lcyl.code.domain.vo.ServiceOrderExecutionVO;

public interface IServiceOrderService
{
    public ServiceOrder selectServiceOrderById(Long id);

    public List<ServiceOrder> selectServiceOrderList(ServiceOrder serviceOrder);

    public int insertServiceOrder(ServiceOrder serviceOrder);

    public int updateServiceOrder(ServiceOrder serviceOrder);

    public int deleteServiceOrderByIds(Long[] ids);

    public int deleteServiceOrderById(Long id);

    public int cancelOrder(ServiceOrderCancelDTO dto);

    public int applyRefund(ServiceOrderRefundApplyDTO dto);

    public int generateExpenseBill(ServiceOrderGenerateBillDTO dto);

    public int autoCancelTimeoutOrders();

    public ServiceOrderExecutionVO selectExecutionByOrderId(Long orderId);
}
