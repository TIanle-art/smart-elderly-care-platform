package com.lcyl.code.service.impl;

import com.lcyl.code.constant.RetreatConstants;
import com.lcyl.code.domain.*;
import com.lcyl.code.mapper.*;
import com.lcyl.code.service.RetreatFinishService;
import com.lcyl.common.exception.ServiceException;
import com.lcyl.common.utils.DateUtils;
import com.lcyl.system.domain.Contract;
import com.lcyl.system.domain.Elder;
import com.lcyl.system.mapper.BedMapper;
import com.lcyl.system.mapper.ContractMapper;
import com.lcyl.system.mapper.ElderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RetreatFinishServiceImpl implements RetreatFinishService {

    @Autowired
    private RetreatMapper retreatMapper;

    @Autowired
    private ElderMapper elderMapper;

    @Autowired
    private BeddMapper bedMapper;

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private AssignNurseMapper assignNurseMapper;

    @Autowired
    private BalanceMapper balanceMapper;

    @Autowired
    private ServiceOrderMapper serviceOrderMapper;

    @Autowired
    private BillPaymentMapper billPaymentMapper;

    @Autowired
    private CheckInConfiggMapper checkInConfigMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void finishRetreatData(Long retreatId) {
        // 1. 获取退住单
        Retreat retreat = retreatMapper.selectRetreatById(retreatId);
        if (retreat == null) {
            throw new ServiceException("退住单不存在");
        }

        Long elderId = retreat.getElderId();
        String bedNo = retreat.getBedNo();

        // ==============================================
        // 【核心】退住完成 → 批量更新所有关联数据
        // ==============================================

        // 1. 更新老人状态 → 5=已退住
        elderMapper.updateStatus(elderId, 5);

        // 2. 床位释放 → 置为空闲（0=空闲）
        bedMapper.freeBedByBedNo(bedNo);

        // 3. 合同状态 → 已解除
        Contract contract = contractMapper.selectContractById(elderId);
        if (contract != null) {
            contract.setStatus(2); // 2=已解除
            contract.setReleaseDate(DateUtils.getNowDate());
            contractMapper.updateContract(contract);
        }

        // 4. 解除护理员绑定
        assignNurseMapper.unbindByElderId(elderId);

        // 5. 余额账户状态 → 1=退住
        Balance balance = balanceMapper.selectByElderId(elderId);
        if (balance != null) {
            balance.setStatus(1L);
            balanceMapper.updateElderBalance(balance);
        }

        // 6. 关闭所有未完成服务订单
        List<ServiceOrder> orders = serviceOrderMapper.selectUnfinishedByElderId(elderId);
        for (ServiceOrder order : orders) {
            order.setOrderStatus("5");      // 5=已关闭
            order.setTradeStatus("5");      // 5=已关闭
            serviceOrderMapper.updateServiceOrder(order);
        }

        // 7. 入住配置 清空/标记退住
        CheckInConfigg config = checkInConfigMapper.selectById(elderId);
        if (config != null) {
            checkInConfigMapper.delByElderId(elderId);
        }

        // 8. 退住单标记为完成
        retreat.setStatus(RetreatConstants.STATUS_COMPLETED);
        retreat.setFlowStatus(7);
        retreatMapper.updateRetreat(retreat);
    }
}