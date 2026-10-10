package com.lcyl.code.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.lcyl.code.service.IServiceOrderService;

@Component
public class ServiceOrderTask
{
    @Autowired
    private IServiceOrderService serviceOrderService;

    /**
     * 每分钟执行一次，自动取消超时未支付订单
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void autoCancelTimeoutOrders()
    {
        serviceOrderService.autoCancelTimeoutOrders();
    }
}
