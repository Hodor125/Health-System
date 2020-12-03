package com.ahu.controller;

import com.ahu.constant.MessageConstant;
import com.ahu.entity.Result;
import com.ahu.service.MemberService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.poi.ss.formula.ptg.ScalarConstantPtg;
import org.omg.CORBA.INTERNAL;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ：hodor007
 * @date ：Created in 2020/12/3
 * @description ：
 * @version: 1.0
 */
@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    private MemberService memberService;

    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){
        ArrayList<String> listDate = new ArrayList<>();
        ArrayList<Integer> listCount = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        //添加过去一年的时间X轴
        for (int i = 1; i <= 12; i++) {
            calendar.add(Calendar.MONTH, -1);
            Date time = calendar.getTime();
            String dateTime = new SimpleDateFormat("yyyy-MM").format(time);
            listDate.add(dateTime);
        }
        Collections.reverse(listDate);

        //添加查询的数据
        try {
            listCount = memberService.findMemberCountByMonth(listDate);
            Map map = new HashMap();
            map.put("months",listDate);
            map.put("memberCount",listCount);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }
}
