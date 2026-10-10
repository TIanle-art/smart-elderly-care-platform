package com.lcyl.code.mapper;

import java.math.BigDecimal;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.lcyl.code.domain.Balance;

/**
 * 余额Mapper接口
 * 
 * @author ruoyi
 * @date 2026-03-27
 */
public interface BalanceMapper 
{
    /**
     * 查询余额
     * 
     * @param id 余额主键
     * @return 余额
     */
    public Balance selectBalanceById(Long id);

    /**
     * 查询余额列表
     * 
     * @param balance 余额
     * @return 余额集合
     */
    public List<Balance> selectBalanceList(Balance balance);

    /**
     * 根据老人ID查询余额账户
     *
     * @param elderId 老人ID
     * @return 余额账户
     */
    public Balance selectBalanceByElderId(Long elderId);

    /**
     * 增加欠费金额
     *
     * @param elderId 老人ID
     * @param elderName 老人姓名
     * @param bedNo 床位号
     * @param arrearsAmount 欠费金额
     * @param paymentDeadline 支付截止时间
     * @param updateBy 更新人
     * @return 结果
     */
    public int increaseArrearsAmount(@Param("elderId") Long elderId,
                                     @Param("elderName") String elderName,
                                     @Param("bedNo") String bedNo,
                                     @Param("arrearsAmount") BigDecimal arrearsAmount,
                                     @Param("paymentDeadline") java.util.Date paymentDeadline,
                                     @Param("updateBy") Long updateBy);

    /**
     * 减少欠费金额
     *
     * @param elderId 老人ID
     * @param arrearsAmount 欠费金额
     * @param updateBy 更新人
     * @return 结果
     */
    public int decreaseArrearsAmount(@Param("elderId") Long elderId,
                                     @Param("arrearsAmount") BigDecimal arrearsAmount,
                                     @Param("updateBy") Long updateBy);

    /**
     * 初始化账单余额账户
     *
     * @param balance 余额账户
     * @return 结果
     */
    public int insertBalanceForBill(Balance balance);

    /**
     * 新增余额
     * 
     * @param balance 余额
     * @return 结果
     */
    public int insertBalance(Balance balance);

    /**
     * 修改余额
     * 
     * @param balance 余额
     * @return 结果
     */
    public int updateBalance(Balance balance);

    /**
     * 删除余额
     * 
     * @param id 余额主键
     * @return 结果
     */
    public int deleteBalanceById(Long id);

    /**
     * 批量删除余额
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBalanceByIds(Long[] ids);


    /**
     * 根据老人ID查询余额信息
     * @param elderId 老人ID
     * @return 余额对象
     */
    Balance selectByElderId(@Param("elderId") Long elderId);

    /**
     * 更新余额状态
     * @param balance 余额对象
     */
    void updateElderBalance(Balance balance);
}
