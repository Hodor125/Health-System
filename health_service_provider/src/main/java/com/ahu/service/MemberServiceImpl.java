package com.ahu.service;

import com.ahu.dao.MemberDao;
import com.ahu.pojo.Member;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * @author ：hodor007
 * @date ：Created in 2020/11/29
 * @description ：
 * @version: 1.0
 */
@Service
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findByTelephone(String tel) {
        Member byTelephone = memberDao.findByTelephone(tel);
        return byTelephone;
    }

    @Override
    public void add(Member member) {
        memberDao.add(member);
    }

    //查询每个月份的会员总数量
    @Override
    public ArrayList<Integer> findMemberCountByMonth(ArrayList<String> listDate) {
        ArrayList<Integer> listCount = new ArrayList<>();
        for (String s : listDate) {
            s += ".31";
            Integer count = memberDao.findMemberCountByMonth(s);
            listCount.add(count);
        }
        return listCount;
    }
}
