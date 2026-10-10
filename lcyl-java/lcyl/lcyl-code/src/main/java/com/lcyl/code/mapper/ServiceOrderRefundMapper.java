package com.lcyl.code.mapper;

import java.util.List;
import com.lcyl.code.domain.ServiceOrderRefund;

/**
 * 退款管理Mapper接口
 * 
 * @author ruoyi
 * @date 2026-03-21
 */
public interface ServiceOrderRefundMapper 
{
    /**
     * 查询退款管理
     * 
     * @param id 退款管理主键
     * @return 退款管理
     */
    public ServiceOrderRefund selectServiceOrderRefundById(Long id);

    /**
     * 查询退款管理列表
     * 
     * @param serviceOrderRefund 退款管理
     * @return 退款管理集合
     */
    public List<ServiceOrderRefund> selectServiceOrderRefundList(ServiceOrderRefund serviceOrderRefund);

    /**
     * 新增退款管理
     * 
     * @param serviceOrderRefund 退款管理
     * @return 结果
     */
    public int insertServiceOrderRefund(ServiceOrderRefund serviceOrderRefund);

    /**
     * 修改退款管理
     * 
     * @param serviceOrderRefund 退款管理
     * @return 结果
     */
    public int updateServiceOrderRefund(ServiceOrderRefund serviceOrderRefund);

    /**
     * 删除退款管理
     * 
     * @param id 退款管理主键
     * @return 结果
     */
    public int deleteServiceOrderRefundById(Long id);

    /**
     * 批量删除退款管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteServiceOrderRefundByIds(Long[] ids);

    public ServiceOrderRefund selectLatestRefundByOrderId(Long orderId);
}
