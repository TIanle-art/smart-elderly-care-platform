package com.lcyl.code.service.impl;

import com.lcyl.code.domain.ElderResponsible;
import com.lcyl.code.mapper.ElderResponsibleMapper;
import com.lcyl.code.service.IElderResponsibleService;
import com.lcyl.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName ElderResponsibleServiceImpl
 * @Description TODO
 * @Author hfy
 * @Date 2026-03-25 10:57
 * @Version 1.0
 */
@Service
public class ElderResponsibleServiceImpl implements IElderResponsibleService {
    @Autowired
    private ElderResponsibleMapper elderResponsibleMapper;
    @Override
    public List<ElderResponsible> selectMyElderList() {
        Long userId = SecurityUtils.getUserId();
        List<ElderResponsible> list = elderResponsibleMapper.selectMyElderList(userId);

        list.forEach(item -> {
            String nurseNamesStr = item.getNurseNames();
            if (nurseNamesStr != null && !nurseNamesStr.isEmpty()) {
                item.setNurseNameList(
                        Arrays.stream(nurseNamesStr.split(" "))
                                .map(String::trim)
                                .filter(s -> !s.isEmpty())
                                .collect(Collectors.toList())
                );
            } else {
                item.setNurseNameList(Collections.emptyList());
            }
        });
        return list;
    }

    @Override
    @Transactional
    public void assignNurse(Long elderId, List<Long> nurseIds) {
        elderResponsibleMapper.deleteByElderId(elderId);
        if (nurseIds != null && !nurseIds.isEmpty()) {
            elderResponsibleMapper.insertAssign(elderId, nurseIds);
        }
    }

    /**
     * 批量设置【房间】护理员（遍历房间下所有老人，逐个设置）
     */
    @Override
    @Transactional
    public void batchAssignNurse(Long roomId, List<Long> nurseIds) {
        List<Long> elderIds = elderResponsibleMapper.selectElderIdsByRoomId(roomId);
        if (elderIds.isEmpty()) return;
        // 1. 批量删除该房间下所有老人旧绑定
        elderResponsibleMapper.deleteByElderIds(elderIds);
        // 2. 批量插入新绑定
        if (nurseIds != null && !nurseIds.isEmpty()) {
            elderResponsibleMapper.batchInsertAssign(elderIds, nurseIds);
        }
    }
}
