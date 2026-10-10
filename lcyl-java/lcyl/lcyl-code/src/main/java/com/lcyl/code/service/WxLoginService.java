package com.lcyl.code.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.lcyl.code.domain.Member;
import com.lcyl.code.domain.dto.UserLoginRequestDto;

import com.lcyl.code.dto.MemberProfileUpdateDto;

import com.lcyl.code.domain.dto.WxCreateOrderDTO;
import com.lcyl.code.domain.dto.WxRefundApplyDTO;
import com.lcyl.code.domain.vo.WxCreateOrderVO;
import com.lcyl.code.domain.vo.WxMyBillDetailVO;
import com.lcyl.code.domain.vo.WxMyOrderDetailVO;
import com.lcyl.code.domain.vo.WxMyBillVO;
import com.lcyl.code.domain.vo.WxMyOrderVO;

import com.lcyl.code.dto.WxLoginDTO;
import com.lcyl.code.vo.LoginVo;
import java.util.List;

/**
 * @ClassName WxLoginService
 * @Description TODO
 * @Author GuiGui
 * @Date 2026-03-24 15:27
 * @Version 1.0
 */
public interface WxLoginService {

    LoginVo getAccessToken(UserLoginRequestDto userLoginRequestDto);


    void updateMemberProfile(MemberProfileUpdateDto dto);

    Member getMemberById(Long memberId);

    List<WxMyOrderVO> getMyOrders();

    List<WxMyBillVO> getMyBills();

    WxMyBillDetailVO getMyBillDetail(Long id);

    WxMyOrderDetailVO getMyOrderDetail(Long id);

    WxCreateOrderVO createOrder(WxCreateOrderDTO dto);

    int payOrder(Long id);

    int payBill(Long id);

    int cancelOrder(Long id);

    int applyRefund(WxRefundApplyDTO dto);

}
