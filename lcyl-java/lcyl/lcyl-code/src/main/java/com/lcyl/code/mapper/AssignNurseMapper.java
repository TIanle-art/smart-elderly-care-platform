package com.lcyl.code.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName AssignNurseMapper
 * @Description TODO
 * @Author ziluo
 * @Date 2026-03-31 16:37
 * @Version 1.0
 */
public interface AssignNurseMapper {
    List<String> selectNurseByElderId(Long elderId);

    void unbindByElderId(@Param("elderId") Long elderId);
}
