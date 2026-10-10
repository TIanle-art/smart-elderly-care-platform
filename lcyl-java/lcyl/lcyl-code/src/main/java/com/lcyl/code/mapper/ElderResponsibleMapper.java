package com.lcyl.code.mapper;

import com.lcyl.code.domain.ElderResponsible;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName ElderResponsibleMapper
 * @Description TODO
 * @Author hfy
 * @Date 2026-03-25 10:49
 * @Version 1.0
 */
public interface ElderResponsibleMapper {
    List<ElderResponsible> selectMyElderList(@Param("userId") Long userId);
    void deleteByElderId(@Param("elderId") Long elderId);
    void insertAssign(@Param("elderId") Long elderId, @Param("nurseIds") List<Long> nurseIds);
    List<Long> selectElderIdsByRoomId(@Param("roomId") Long roomId);
    // 批量删除老人
    void deleteByElderIds(@Param("elderIds") List<Long> elderIds);
    // 批量插入（房间用）
    void batchInsertAssign(@Param("elderIds") List<Long> elderIds, @Param("nurseIds") List<Long> nurseIds);
}
