package com.lcyl.code.mapper;

import com.lcyl.code.domain.LcRetreatHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName LcRetreatHistoryMapper
 * @Description TODO
 * @Author ziluo
 * @Date 2026-03-26 10:32
 * @Version 1.0
 */
public interface LcRetreatHistoryMapper {
    int insert(LcRetreatHistory record);
    List<LcRetreatHistory> selectByRetreatId(@Param("retreatId") Long retreatId);

}
