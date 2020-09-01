package com.itheima.health.service;

import com.itheima.health.pojo.Member;

import java.util.List;

/**
 * Description: No Description
 * User: Eric
 */
public interface MemberService {
    /**
     * 通过手机号码查询会员
     * @param telephone
     * @return
     */
    Member findByTelephone(String telephone);

    /**
     * 添加新会员
     * @param member
     */
    void add(Member member);

    /**
     * 统计每个月最后一天时 会员的总数量
     * @param months
     * @return
     */
    List<Integer> getMemberReport(List<String> months);
}
