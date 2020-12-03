package com.ahu.service;

import com.ahu.pojo.Member;

import java.util.ArrayList;

/**
 * @author ：XXXX
 * @date ：Created in 2020/11/29
 * @description ：
 * @version: 1.0
 */
public interface MemberService {

    Member findByTelephone(String tel);

    void add(Member member);

    //查询过去一年的各个月份的会员总数量
    ArrayList<Integer> findMemberCountByMonth(ArrayList<String> listDate);
}
