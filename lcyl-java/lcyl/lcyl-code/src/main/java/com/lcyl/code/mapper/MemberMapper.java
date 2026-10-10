package com.lcyl.code.mapper;

import com.lcyl.code.domain.Member;

/**
 * @ClassName MemberMapper
 * @Description TODO
 * @Author GuiGui
 * @Date 2026-03-24 21:02
 * @Version 1.0
 */
public interface MemberMapper {

    int insert(Member member);



    Member selectByOpenId(String phone);

    /**
     * 根据id更新个人资料（编辑资料）
     * @param member
     */
    void updateById(Member member);

    /**
     * 根据用户id查询用户信息
     * @param memberId
     * @return
     */
    Member selectById(Long memberId);
}
