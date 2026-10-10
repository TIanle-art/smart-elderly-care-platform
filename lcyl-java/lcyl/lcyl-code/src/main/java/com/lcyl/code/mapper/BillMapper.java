package com.lcyl.code.mapper;

import java.math.BigDecimal;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.lcyl.code.domain.Bill;
import com.lcyl.code.domain.dto.BillPayDTO;
import com.lcyl.code.domain.vo.BillGenerateElderVO;
import com.lcyl.code.domain.vo.WxMyBillVO;

public interface BillMapper
{
    public Bill selectBillById(Long id);

    public List<Bill> selectBillList(Bill bill);

    public int insertBill(Bill bill);

    public int updateBill(Bill bill);

    public int deleteBillById(Long id);

    public int deleteBillByIds(Long[] ids);

    public int checkMonthlyBillExists(@Param("elderId") Long elderId, @Param("billMonth") String billMonth);

    public int checkExpenseBillExists(@Param("orderNo") String orderNo);


    int countBillByElderIdAndMonth(@Param("elderId") Long elderId, @Param("billMonth") String billMonth);

    public BillGenerateElderVO selectGenerateElderInfo(Long elderId);

    public BigDecimal selectRoomTypePriceByElderId(Long elderId);

    public BigDecimal selectNursingMonthlyFeeByElderId(Long elderId);

    public int insertBillPaymentRecord(BillPayDTO dto);

    public List<WxMyBillVO> selectWxMyBillList(@Param("memberId") Long memberId);

    public Bill selectWxMyBillDetail(@Param("memberId") Long memberId, @Param("id") Long id);

    List<Bill> selectPendingBillsByElderId(Long elderId);
}
