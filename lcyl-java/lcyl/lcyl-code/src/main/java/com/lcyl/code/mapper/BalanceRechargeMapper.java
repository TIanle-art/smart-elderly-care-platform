package com.lcyl.code.mapper;

import java.math.BigDecimal;
import java.util.List;
import com.lcyl.code.domain.BalanceRecharge;
import com.lcyl.code.domain.dto.BalanceRechargeDTO;
import com.lcyl.code.domain.vo.RechargeElderOptionVO;
import org.apache.ibatis.annotations.Param;

/**
 * 预缴款充值记录Mapper接口
 * 
 * @author ruoyi
 * @date 2026-03-27
 */
public interface BalanceRechargeMapper 
{
    /**
     * 查询预缴款充值记录
     * 
     * @param id 预缴款充值记录主键
     * @return 预缴款充值记录
     */
    public BalanceRecharge selectBalanceRechargeById(Long id);

    /**
     * 查询预缴款充值记录列表
     * 
     * @param balanceRecharge 预缴款充值记录
     * @return 预缴款充值记录集合
     */
    public List<BalanceRecharge> selectBalanceRechargeList(BalanceRecharge balanceRecharge);

    /**
     * 新增预缴款充值记录
     * 
     * @param balanceRecharge 预缴款充值记录
     * @return 结果
     */
    public int insertBalanceRecharge(BalanceRecharge balanceRecharge);

    /**
     * 修改预缴款充值记录
     * 
     * @param balanceRecharge 预缴款充值记录
     * @return 结果
     */
    public int updateBalanceRecharge(BalanceRecharge balanceRecharge);

    /**
     * 删除预缴款充值记录
     * 
     * @param id 预缴款充值记录主键
     * @return 结果
     */
    public int deleteBalanceRechargeById(Long id);

    /**
     * 批量删除预缴款充值记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBalanceRechargeByIds(Long[] ids);

    /**
     * 查询充值可选老人列表
     *
     * @return 老人选项
     */
    public List<RechargeElderOptionVO> selectRechargeElderOptions();

    /**
     * 新增充值流水
     *
     * @param dto 充值参数
     * @return 结果
     */
    public int insertRechargeRecord(BalanceRechargeDTO dto);

    /**
     * 查询余额账户是否存在
     *
     * @param elderId 老人ID
     * @return 数量
     */
    public int countBalanceByElderId(Long elderId);

    /**
     * 更新预缴款余额
     *
     * @param elderId 老人ID
     * @param rechargeAmount 充值金额
     * @param updateBy 更新人
     * @return 结果
     */
    public int updateBalancePrepaid(@Param("elderId") Long elderId,
                                    @Param("rechargeAmount") BigDecimal rechargeAmount,
                                    @Param("elderName") String elderName,
                                    @Param("bedNo") String bedNo,
                                    @Param("updateBy") Long updateBy);

    /**
     * 首次充值时初始化余额账户
     *
     * @param dto 充值参数
     * @return 结果
     */
    public int insertBalanceAccountByRecharge(@Param("dto") BalanceRechargeDTO dto,
                                              @Param("userId") Long userId);

    /**
     * 根据老人姓名查询老人ID
     *
     * @param elderName 老人姓名
     * @return 老人ID
     */
    public Long selectElderIdByName(String elderName);
}
