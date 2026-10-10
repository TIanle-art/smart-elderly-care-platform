package com.lcyl.code.service;

import com.lcyl.code.domain.ElderResponsible;

import java.util.List;

/**
 * @ClassName IElderResponsibleService
 * @Description TODO
 * @Author hfy
 * @Date 2026-03-25 10:55
 * @Version 1.0
 */
public interface IElderResponsibleService {
    List<ElderResponsible> selectMyElderList();
    void assignNurse(Long elderId, List<Long> nurseIds);
    void batchAssignNurse(Long roomId, List<Long> nurseIds);
}
