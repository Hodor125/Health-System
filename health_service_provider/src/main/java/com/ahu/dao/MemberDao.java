package com.ahu.dao;

import com.github.pagehelper.Page;
import com.ahu.pojo.Member;

import java.util.List;

public interface MemberDao {

    public List<Member> findAll();

    public Page<Member> selectByCondition(String queryString);

    public Integer add(Member member);

    public void deleteById(Integer id);

    public Member findById(Integer id);

    public Member findByTelephone(String telephone);

    public void edit(Member member);

    public Integer findMemberCountBeforeDate(String date);

    public Integer findMemberCountByDate(String date);

    public Integer findMemberCountAfterDate(String date);

    public Integer findMemberTotalCount();

    //查询截至这个月的会员总数
    Integer findMemberCountByMonth(String s);
}
