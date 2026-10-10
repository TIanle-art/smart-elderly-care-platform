package com.lcyl.code.mapper;

import com.lcyl.code.domain.Retreat;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RetreatMapper {

    // 根据ID查询
    Retreat selectRetreatById(@Param("id") Long id);

    // 条件分页查询（支持多条件搜索）
    List<Retreat> selectRetreatList(Retreat retreat);

    // 批量查询（根据ID列表）
    List<Retreat> selectBatchIds(@Param("ids") List<Long> ids);  // 新增

    // 新增
    int insertRetreat(Retreat retreat);

    // 修改
    int updateRetreat(Retreat retreat);

    // 单个删除
    int deleteRetreatById(Long id);

    // 批量删除
    int deleteRetreatByIds(Long[] ids);

    // 待办列表查询（根据角色返回对应节点的申请单）
    List<Retreat> selectTodoList(@Param("role") String role, @Param("userId") Long userId);

    String selectNickNameByusername(String username);

    Long selectElderId(Long id);

    String selectApplicatName(Long applicatId);
}