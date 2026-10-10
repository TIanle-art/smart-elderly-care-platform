package com.lcyl.code.service;

import java.util.List;
import com.lcyl.code.domain.ServiceOrderRefund;
import com.lcyl.code.domain.dto.ServiceOrderRefundFailDTO;

/**
 * 退款管理Service接口
 *
 * @author ruoyi
 * @date 2026-03-21
 */
public interface IServiceOrderRefundService
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
     * 批量删除退款管理
     *
     * @param ids 需要删除的退款管理主键集合
     * @return 结果
     */
    public int deleteServiceOrderRefundByIds(Long[] ids);

    /**
     * 删除退款管理信息
     *
     * @param id 退款管理主键
     * @return 结果
     */
    public int deleteServiceOrderRefundById(Long id);

    /**
     * 退款成功
     *
     * @param id 退款记录主键
     * @return 结果
     */
    public int refundSuccess(Long id);

    /**
     * 退款失败
     *
     * @param dto 退款失败参数
     * @return 结果
     */
    public int refundFail(ServiceOrderRefundFailDTO dto);
}
