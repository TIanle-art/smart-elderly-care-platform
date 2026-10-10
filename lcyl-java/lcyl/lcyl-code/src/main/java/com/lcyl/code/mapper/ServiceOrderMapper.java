package com.lcyl.code.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.lcyl.code.domain.ServiceOrder;
import com.lcyl.code.domain.vo.ServiceOrderExecutionVO;
import com.lcyl.code.domain.vo.WxMyOrderVO;

/**
 * 订单详情Mapper接口
 * 
 * @author ruoyi
 * @date 2026-03-21
 */
public interface ServiceOrderMapper 
{
    /**
     * 查询订单详情
     * 
     * @param id 订单详情主键
     * @return 订单详情
     */
    public ServiceOrder selectServiceOrderById(Long id);

    /**
     * 查询订单详情列表
     * 
     * @param serviceOrder 订单详情
     * @return 订单详情集合
     */
    public List<ServiceOrder> selectServiceOrderList(ServiceOrder serviceOrder);

    /**
     * 新增订单详情
     * 
     * @param serviceOrder 订单详情
     * @return 结果
     */
    public int insertServiceOrder(ServiceOrder serviceOrder);

    /**
     * 修改订单详情
     * 
     * @param serviceOrder 订单详情
     * @return 结果
     */
    public int updateServiceOrder(ServiceOrder serviceOrder);

    /**
     * 删除订单详情
     * 
     * @param id 订单详情主键
     * @return 结果
     */
    public int deleteServiceOrderById(Long id);

    /**
     * 批量删除订单详情
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteServiceOrderByIds(Long[] ids);
    /**
     * 自动取消超时未支付订单
     *
     * @return 结果
     */
    public int autoCancelTimeoutOrders();
    
    public ServiceOrderExecutionVO selectExecutionByOrderId(Long orderId);

    public List<WxMyOrderVO> selectWxMyOrderList(@Param("memberId") Long memberId);

    public ServiceOrder selectWxMyOrderDetail(@Param("memberId") Long memberId, @Param("id") Long id);

    public String selectWxOrderProjectImage(@Param("memberId") Long memberId, @Param("id") Long id);

    /**
     * 查询退住应退订单（独立方法，不影响其他功能）
     */
    List<ServiceOrder> selectRefundServiceOrderList(Long elderId);

    List<ServiceOrder> selectUnfinishedByElderId(@Param("elderId") Long elderId);
}
