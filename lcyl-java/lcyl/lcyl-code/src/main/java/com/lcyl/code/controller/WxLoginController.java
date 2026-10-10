package com.lcyl.code.controller;

import com.lcyl.code.domain.*;
import com.lcyl.code.dto.MemberProfileUpdateDto;
import com.lcyl.code.domain.Bill;
import com.lcyl.code.domain.LcReservation;
import com.lcyl.code.domain.NursingItem;
import com.lcyl.code.mapper.MemberMapper;
import com.lcyl.code.service.*;
import com.lcyl.common.core.controller.BaseController;
import com.lcyl.common.core.page.TableDataInfo;
import com.lcyl.code.domain.dto.WxCreateOrderDTO;
import com.lcyl.code.domain.dto.WxRefundApplyDTO;

import com.lcyl.code.domain.vo.MyVisitVo;
import com.lcyl.code.service.IBillService;
import com.lcyl.code.domain.vo.MyVisitVo;
import com.lcyl.code.service.IBillService;
import com.lcyl.common.utils.Result;
import com.lcyl.system.domain.Contract;

import com.lcyl.code.domain.vo.MyVisitVo;
import com.lcyl.code.service.IBillService;
import com.lcyl.system.domain.dto.AddInfo;
import com.lcyl.code.domain.dto.UserLoginRequestDto;
import com.lcyl.common.core.domain.AjaxResult;
import com.lcyl.common.utils.UserThreadLocal;
import com.lcyl.system.domain.Elder;
import com.lcyl.system.domain.dto.FlatBedDTO;
import com.lcyl.system.domain.dto.FloorRoomBedDTO;
import com.lcyl.system.domain.vo.BedVO;
import com.lcyl.system.service.BedService;
import com.lcyl.system.service.IContractService;
import com.lcyl.system.service.IElderService;
import com.lcyl.system.service.ILcRoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.lcyl.common.core.domain.AjaxResult.success;

/**
 * @ClassName WxLoginController
 * @Description TODO
 * @Author GuiGui
 * @Date 2026-03-24 14:25
 * @Version 1.0
 */
@RestController
@RequestMapping("/wxLogin")
public class WxLoginController extends BaseController {

    @Autowired
    private WxLoginService wxLoginService;

    @Autowired
    private ILcRoomTypeService lcRoomTypeService;

    @Autowired
    private ILcReservationService lcReservationService;

    @Autowired
    private IElderService elderService;

    @Autowired
    private IBillService iBillService;
    @Autowired
    private INursingItemService nursingItemService;
    @Autowired
    private IContractService contractService;

    @Autowired
    private BedService bedService;




    @PostMapping("/phoneLogin")
    public AjaxResult wxLogin(@RequestBody UserLoginRequestDto userLoginRequestDto){

        return success(wxLoginService.getAccessToken(userLoginRequestDto));
    }

    @PostMapping("/addvisitor")
    public AjaxResult getCancelledReservationCount(@RequestBody LcReservation lcReservation) {
        return success(lcReservationService.insertLcReservation(lcReservation));

    }
    @GetMapping("/item/list")
    public TableDataInfo list(NursingItem nursingItem)
    {
        startPage();
        List<NursingItem> list = nursingItemService.selectNursingItemList(nursingItem);
        return getDataTable(list);
    }
    @GetMapping(value = "/item/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(nursingItemService.selectNursingItemById(id));
    }
    @GetMapping(value = "/roomType/{id}")
    public AjaxResult getInfo1(@PathVariable("id") Long id)
    {
        return success(lcRoomTypeService.selectLcRoomTypeById (id));
    }
    @GetMapping("/contract/list")
    public TableDataInfo list(Contract contract)
    {
        startPage();
        List<Contract> list = contractService.selectContractList(contract);
        return getDataTable(list);
    }
    @GetMapping("/bed/{elderId}")
    public AjaxResult getElderPhoto(@PathVariable Long elderId) {
        return success (bedService.getFloorRoomBedByFloorId1(elderId));
    }
    @GetMapping("/getRoomType")
    public AjaxResult getRoomType() {
        return success(lcRoomTypeService.selectLcRoomTypeList(null));
    }

    @GetMapping("/getElder")
    public AjaxResult getElder() {
        Long memberId = UserThreadLocal.getUserId();
        List<Elder> elderList = elderService.selectElderByUser(memberId);
        return success(elderList);
    }

    @GetMapping("/getElderList")
    public AjaxResult getElderList() {
        List<Elder> elderList = elderService.selectAllElder();
        return success(elderList);
    }

    @GetMapping("/myOrders")
    public AjaxResult getMyOrders() {
        return success(wxLoginService.getMyOrders());
    }

    @GetMapping("/myBills")
    public AjaxResult getMyBills() {
        return success(wxLoginService.getMyBills());
    }

    @GetMapping("/myBillDetail/{id}")
    public AjaxResult getMyBillDetail(@PathVariable Long id) {
        return success(wxLoginService.getMyBillDetail(id));
    }

    @GetMapping("/myOrderDetail/{id}")
    public AjaxResult getMyOrderDetail(@PathVariable Long id) {
        return success(wxLoginService.getMyOrderDetail(id));
    }

    @PostMapping("/createOrder")
    public AjaxResult createOrder(@RequestBody WxCreateOrderDTO dto) {
        return success(wxLoginService.createOrder(dto));
    }

    @PostMapping("/payOrder/{id}")
    public AjaxResult payOrder(@PathVariable Long id) {
        return success(wxLoginService.payOrder(id));
    }

    @PostMapping("/payBill/{id}")
    public AjaxResult payBill(@PathVariable Long id) {
        return success(wxLoginService.payBill(id));
    }

    @PostMapping("/cancelOrder/{id}")
    public AjaxResult cancelOrder(@PathVariable Long id) {
        return success(wxLoginService.cancelOrder(id));
    }

    @PostMapping("/applyRefund")
    public AjaxResult applyRefund(@RequestBody WxRefundApplyDTO dto) {
        return success(wxLoginService.applyRefund(dto));
    }

    @PostMapping("addInfo")
    public AjaxResult addInfo(@RequestBody AddInfo addInfo){
        return success(elderService.insertInfo(addInfo));
    }

    @PostMapping("getElderBedList")
    public AjaxResult getElderBedList(){
        List<BedVO> bedVO= elderService.getElderBedList();
        return success(bedVO);
    }

    @DeleteMapping("/deleteElder/{id}")
    public AjaxResult deleteElder(@PathVariable Long id){
        return success(elderService.deleteElderInfoById(id));
    }

    @PostMapping("/list")
    public AjaxResult list(@RequestBody Bill bill){
        return success(iBillService.selectBillList(bill));
    }

    @PostMapping("selectVisitInfo")
    public AjaxResult selectvisitInfo(@RequestBody Map<String, Integer> params){
        Integer status = params.get("status");
        List<MyVisitVo> myVisitVos = lcReservationService.selectvisitInfo(status);
        return success(myVisitVos);
    }

    @PutMapping("/deleteVisit/{id}")
    public AjaxResult deleteVisit(@PathVariable Long id) {
        return success(lcReservationService.deleteReservationById(id));
    }


    /**
     * 更新当前登录会员的个人资料
     */
    @PutMapping("/profile")
    public AjaxResult updateProfile(@Validated @RequestBody MemberProfileUpdateDto dto) {
        wxLoginService.updateMemberProfile(dto);
        return success("更新成功");
    }

    /**
     * 根据id查询当前登录的个人资料
     */
    @GetMapping("/get/profile")
    public AjaxResult getInfo() {
        Long memberId = UserThreadLocal.getUserId();

        if (memberId == null) {
            return AjaxResult.error("用户未登录");
        }

        Member member = wxLoginService.getMemberById(memberId);

        if (member == null) {
            return AjaxResult.error("用户不存在");
        }

        return AjaxResult.success(member);
    }
}
