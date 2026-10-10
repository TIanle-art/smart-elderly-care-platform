package com.lcyl.code.service.impl;

import com.lcyl.code.constant.LoginConstant;
import com.lcyl.code.domain.Member;
import com.lcyl.code.domain.NursingItem;
import com.lcyl.code.domain.ServiceOrder;
import com.lcyl.code.domain.ServiceOrderRefund;
import com.lcyl.code.domain.Bill;
import com.lcyl.code.domain.BillItem;
import com.lcyl.code.domain.BillPayment;
import com.lcyl.code.domain.dto.UserLoginRequestDto;

import com.lcyl.code.dto.MemberProfileUpdateDto;
import com.lcyl.code.dto.WxLoginDTO;

import com.lcyl.code.domain.dto.BillPayDTO;
import com.lcyl.code.domain.dto.WxCreateOrderDTO;
import com.lcyl.code.domain.dto.WxRefundApplyDTO;
import com.lcyl.code.domain.vo.WxCreateOrderVO;
import com.lcyl.code.domain.vo.WxMyBillDetailVO;
import com.lcyl.code.domain.vo.WxMyBillVO;
import com.lcyl.code.domain.vo.WxMyOrderDetailVO;
import com.lcyl.code.domain.vo.WxMyOrderVO;
import com.lcyl.code.mapper.BillMapper;
import com.lcyl.code.mapper.BillItemMapper;
import com.lcyl.code.mapper.BillPaymentMapper;

import com.lcyl.code.mapper.MemberMapper;
import com.lcyl.code.mapper.BalanceMapper;
import com.lcyl.code.mapper.NursingTaskMapper;
import com.lcyl.code.mapper.ServiceOrderMapper;
import com.lcyl.code.mapper.ServiceOrderRefundMapper;
import com.lcyl.code.service.INursingItemService;
import com.lcyl.code.service.IServiceOrderService;
import com.lcyl.code.service.WxLoginService;
import com.lcyl.code.vo.LoginVo;
import com.lcyl.common.constant.Constants;
import com.lcyl.common.exception.ServiceException;

import com.lcyl.common.utils.SecurityUtils;
import com.lcyl.common.utils.UserThreadLocal;
import com.lcyl.common.utils.http.HttpUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.lcyl.common.utils.DateUtils;
import com.lcyl.common.utils.UserThreadLocal;
import com.lcyl.framework.web.service.TokenService;
import com.lcyl.system.domain.Elder;
import com.lcyl.system.service.IElderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class WxLoginServiceImpl implements WxLoginService
{
    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ServiceOrderMapper serviceOrderMapper;

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private BillItemMapper billItemMapper;

    @Autowired
    private BillPaymentMapper billPaymentMapper;

    @Autowired
    private BalanceMapper balanceMapper;

    @Autowired
    private ServiceOrderRefundMapper serviceOrderRefundMapper;

    @Autowired
    private NursingTaskMapper nursingTaskMapper;

    @Autowired
    private IElderService elderService;

    @Autowired
    private INursingItemService nursingItemService;

    @Autowired
    private IServiceOrderService serviceOrderService;

    @Override
    public LoginVo getAccessToken(UserLoginRequestDto userLoginRequestDto)
    {
        String code = userLoginRequestDto.getCode();
        String phoneCode = userLoginRequestDto.getPhoneCode();

        LoginConstant loginConstant = new LoginConstant();
        String url = loginConstant.getOpenIdURL(code);
        String openId = loginConstant.getOpenId(url);
        String accessTokenUrl = loginConstant.getTokenUrl();
        String accessToken = loginConstant.getAccessToken(accessTokenUrl);
        String loginPhoneUrl = loginConstant.getLoginPhoneUrl(accessToken);
        String phone = loginConstant.getPhone(loginPhoneUrl, phoneCode);

        Member member = memberMapper.selectByOpenId(openId);
        if (member == null)
        {
            Member newMember = new Member();
            newMember.setPhone(phone);
            newMember.setOpenId(openId);
            memberMapper.insert(newMember);
        }

        member = memberMapper.selectByOpenId(openId);

        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.JWT_USERID, member.getId());
        claims.put(Constants.JWT_USERNAME, member.getName());
        String token = tokenService.createToken(claims);
        return new LoginVo(member.getName(), token);
    }


    /**
     * 更新用户资料信息
     * @param dto
     */
    @Override
    public void updateMemberProfile(MemberProfileUpdateDto dto) {
        Long memberId = UserThreadLocal.getUserId(); // 按你的项目实际获取方式来
        Member member = memberMapper.selectById(memberId);

        if (member == null) {
            throw new ServiceException("会员不存在");
        }

        if (!"男".equals(dto.getGender()) && !"女".equals(dto.getGender())) {
            throw new ServiceException("性别参数不合法");
        }

        member.setName(dto.getName());
        member.setAvatar(dto.getAvatar());
        member.setGender(dto.getGender());

        memberMapper.updateById(member);
    }

    /**
     * 根据用户id查询用户的基本信息
     * @param memberId
     * @return
     */
    @Override
    public Member getMemberById(Long memberId) {
        return memberMapper.selectById(memberId);
    }
    @Override
    public List<WxMyOrderVO> getMyOrders()
    {
        return serviceOrderMapper.selectWxMyOrderList(UserThreadLocal.getUserId());

    }

    @Override
    public List<WxMyBillVO> getMyBills()
    {
        return billMapper.selectWxMyBillList(UserThreadLocal.getUserId());
    }

    @Override
    public WxMyBillDetailVO getMyBillDetail(Long id)
    {
        Long memberId = UserThreadLocal.getUserId();
        Bill billInfo = billMapper.selectWxMyBillDetail(memberId, id);
        if (billInfo == null)
        {
            throw new ServiceException("账单不存在或无权查看");
        }

        BillItem queryItem = new BillItem();
        queryItem.setBillId(id);
        List<BillItem> billItemList = billItemMapper.selectBillItemList(queryItem);

        BillPayment queryPayment = new BillPayment();
        queryPayment.setBillId(id);
        List<BillPayment> paymentList = billPaymentMapper.selectBillPaymentList(queryPayment);

        WxMyBillDetailVO detailVO = new WxMyBillDetailVO();
        detailVO.setBillInfo(billInfo);
        detailVO.setBillItemList(billItemList);
        detailVO.setPaymentRecord(paymentList == null || paymentList.isEmpty() ? null : paymentList.get(0));
        return detailVO;
    }

    @Override
    public WxMyOrderDetailVO getMyOrderDetail(Long id)
    {
        Long memberId = UserThreadLocal.getUserId();
        ServiceOrder orderInfo = serviceOrderMapper.selectWxMyOrderDetail(memberId, id);
        if (orderInfo == null)
        {
            throw new ServiceException("订单不存在或无权查看");
        }

        WxMyOrderDetailVO detailVO = new WxMyOrderDetailVO();
        detailVO.setOrderInfo(orderInfo);
        detailVO.setExecutionInfo(serviceOrderMapper.selectExecutionByOrderId(id));
        detailVO.setProjectImage(serviceOrderMapper.selectWxOrderProjectImage(memberId, id));
        detailVO.setRefundInfo(serviceOrderRefundMapper.selectLatestRefundByOrderId(id));
        return detailVO;
    }

    @Override
    public WxCreateOrderVO createOrder(WxCreateOrderDTO dto)
    {
        Long memberId = UserThreadLocal.getUserId();
        if (memberId == null)
        {
            throw new ServiceException("当前登录已过期，请重新登录");
        }
        if (dto == null || dto.getElderId() == null)
        {
            throw new ServiceException("服务对象不能为空");
        }
        if (dto.getProjectId() == null)
        {
            throw new ServiceException("服务项目不能为空");
        }
        if (dto.getExpectedServiceTime() == null || "".equals(dto.getExpectedServiceTime().trim()))
        {
            throw new ServiceException("期望服务时间不能为空");
        }

        Elder elder = loadBoundElder(memberId, dto.getElderId());
        NursingItem nursingItem = nursingItemService.selectNursingItemById(dto.getProjectId());
        if (nursingItem == null)
        {
            throw new ServiceException("服务项目不存在");
        }

        ServiceOrder order = new ServiceOrder();
        order.setOrderNo(generateOrderNo());
        order.setElderId(elder.getId());
        order.setElderName(elder.getName());
        order.setBedId(elder.getBedId());
        order.setBedNo(elder.getBedNumber());
        order.setProjectId(nursingItem.getId());
        order.setProjectName(nursingItem.getItemName());
        order.setOrderAmount(BigDecimal.valueOf(nursingItem.getPrice() == null ? 0D : nursingItem.getPrice()));
        order.setExpectedServiceTime(parseExpectedServiceTime(dto.getExpectedServiceTime()));
        order.setRemark(dto.getRemark());
        order.setOrderStatus("0");
        order.setTradeStatus("0");
        order.setOrderSource("1");
        order.setDelFlag("0");

        int rows = serviceOrderService.insertServiceOrder(order);
        if (rows <= 0 || order.getId() == null)
        {
            throw new ServiceException("下单失败，请稍后重试");
        }

        WxCreateOrderVO result = new WxCreateOrderVO();
        result.setOrderId(order.getId());
        result.setOrderNo(order.getOrderNo());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int payOrder(Long id)
    {
        ServiceOrder order = serviceOrderMapper.selectWxMyOrderDetail(UserThreadLocal.getUserId(), id);
        if (order == null)
        {
            throw new ServiceException("订单不存在或无权操作");
        }
        if (!"0".equals(order.getOrderStatus()) || !"0".equals(order.getTradeStatus()))
        {
            throw new ServiceException("当前订单状态不允许支付");
        }

        order.setOrderStatus("1");
        order.setTradeStatus("1");
        order.setPayTime(DateUtils.getNowDate());
        int rows = serviceOrderService.updateServiceOrder(order);
        if (rows <= 0)
        {
            throw new ServiceException("支付成功后更新订单状态失败");
        }

        if (nursingTaskMapper.countTaskExecutionByOrderId(order.getId()) <= 0)
        {
            nursingTaskMapper.insertTaskExecutionInit(order.getId(), String.valueOf(UserThreadLocal.getUserId()), DateUtils.getNowDate());
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int payBill(Long id)
    {
        Long memberId = UserThreadLocal.getUserId();
        Bill bill = billMapper.selectWxMyBillDetail(memberId, id);
        if (bill == null)
        {
            throw new ServiceException("账单不存在或无权操作");
        }
        if (!"0".equals(bill.getTradeStatus()))
        {
            throw new ServiceException("当前账单状态不允许支付");
        }

        Member member = memberMapper.selectById(memberId);
        if (member == null)
        {
            throw new ServiceException("当前用户不存在");
        }

        BillPayDTO dto = new BillPayDTO();
        dto.setBillId(bill.getId());
        dto.setBillNo(bill.getBillNo());
        dto.setPaymentNo(generateBillPaymentNo());
        dto.setPayTime(DateUtils.getNowDate());
        dto.setPayChannel("1");
        dto.setPayMethod("微信");
        dto.setWechatOrderNo("WX" + DateUtils.dateTimeNow("yyyyMMddHHmmss") + ThreadLocalRandom.current().nextInt(100, 1000));
        dto.setPayAmount(bill.getPayableAmount());
        dto.setVoucherUrl("小程序在线支付");
        dto.setPayRemark("小程序账单支付");
        dto.setOperatorId(memberId);
        dto.setOperatorName(member.getName());
        dto.setCreateBy(String.valueOf(memberId));
        dto.setCreateTime(DateUtils.getNowDate());
        billMapper.insertBillPaymentRecord(dto);

        bill.setTradeStatus("1");
        bill.setUpdateBy(String.valueOf(memberId));
        bill.setUpdateTime(DateUtils.getNowDate());
        int rows = billMapper.updateBill(bill);
        if (rows <= 0)
        {
            throw new ServiceException("账单支付成功后更新状态失败");
        }

        if ("1".equals(bill.getBillType()) && bill.getElderId() != null && bill.getPayableAmount() != null)
        {
            balanceMapper.decreaseArrearsAmount(bill.getElderId(), bill.getPayableAmount(), memberId);
        }
        return rows;
    }

    @Override
    public int cancelOrder(Long id)
    {
        ServiceOrder order = serviceOrderMapper.selectWxMyOrderDetail(UserThreadLocal.getUserId(), id);
        if (order == null)
        {
            throw new ServiceException("订单不存在或无权操作");
        }
        if (!"0".equals(order.getOrderStatus()) || !"0".equals(order.getTradeStatus()))
        {
            throw new ServiceException("只有待支付订单可以取消");
        }

        order.setOrderStatus("5");
        order.setTradeStatus("5");
        order.setCancelTime(DateUtils.getNowDate());
        order.setCancelReason("小程序家属主动取消订单");
        return serviceOrderService.updateServiceOrder(order);
    }

    @Override
    public int applyRefund(WxRefundApplyDTO dto)
    {
        Long memberId = UserThreadLocal.getUserId();
        if (dto == null || dto.getOrderId() == null)
        {
            throw new ServiceException("订单ID不能为空");
        }
        if (dto.getRefundReason() == null || "".equals(dto.getRefundReason().trim()))
        {
            throw new ServiceException("退款原因不能为空");
        }

        ServiceOrder order = serviceOrderMapper.selectWxMyOrderDetail(memberId, dto.getOrderId());
        if (order == null)
        {
            throw new ServiceException("订单不存在或无权操作");
        }
        if (!"1".equals(order.getTradeStatus()) && !"4".equals(order.getTradeStatus()))
        {
            throw new ServiceException("当前订单状态不允许申请退款");
        }

        ServiceOrderRefund latestRefund = serviceOrderRefundMapper.selectLatestRefundByOrderId(order.getId());
        if (latestRefund != null && "0".equals(latestRefund.getRefundStatus()))
        {
            throw new ServiceException("当前订单已有退款申请在处理中");
        }

        Member member = memberMapper.selectById(memberId);
        if (member == null)
        {
            throw new ServiceException("当前用户不存在");
        }

        ServiceOrderRefund refund = new ServiceOrderRefund();
        refund.setRefundNo(generateRefundNo());
        refund.setOrderId(order.getId());
        refund.setOrderNo(order.getOrderNo());
        refund.setRefundAmount(order.getOrderAmount());
        refund.setRefundStatus("0");
        refund.setApplicantId(memberId);
        refund.setApplicantName(member.getName());
        refund.setApplicantType("1");
        refund.setApplyTime(DateUtils.getNowDate());
        refund.setRefundReason(dto.getRefundReason().trim());
        refund.setRefundChannel("1");
        refund.setRefundMethod("微信");
        refund.setDelFlag("0");
        refund.setCreateBy(String.valueOf(memberId));
        refund.setCreateTime(DateUtils.getNowDate());

        int rows = serviceOrderRefundMapper.insertServiceOrderRefund(refund);
        if (rows <= 0)
        {
            throw new ServiceException("退款申请失败，请稍后重试");
        }

        order.setTradeStatus("2");
        serviceOrderService.updateServiceOrder(order);
        return rows;
    }

    private Elder loadBoundElder(Long memberId, Long elderId)
    {
        List<Elder> elderList = elderService.selectElderByUser(memberId);
        if (elderList == null || elderList.isEmpty())
        {
            throw new ServiceException("当前账号未绑定老人");
        }
        for (Elder elder : elderList)
        {
            if (elderId.equals(elder.getId()))
            {
                Elder fullElder = elderService.selectElderById(elderId);
                if (fullElder == null)
                {
                    throw new ServiceException("老人信息不存在");
                }
                return fullElder;
            }
        }
        throw new ServiceException("无权为该老人下单");
    }

    private Date parseExpectedServiceTime(String expectedServiceTime)
    {
        String value = expectedServiceTime.trim();
        if (value.length() == 16)
        {
            value = value + ":00";
        }
        try
        {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value);
        }
        catch (ParseException e)
        {
            throw new ServiceException("期望服务时间格式错误");
        }
    }

    private String generateOrderNo()
    {
        return "DD" + DateUtils.dateTimeNow("yyyyMMddHHmmss") + ThreadLocalRandom.current().nextInt(100, 1000);
    }

    private String generateRefundNo()
    {
        return "TK" + DateUtils.dateTimeNow("yyyyMMddHHmmss") + ThreadLocalRandom.current().nextInt(100, 1000);
    }

    private String generateBillPaymentNo()
    {
        return "FK" + DateUtils.dateTimeNow("yyyyMMddHHmmss") + ThreadLocalRandom.current().nextInt(100, 1000);
    }
}
