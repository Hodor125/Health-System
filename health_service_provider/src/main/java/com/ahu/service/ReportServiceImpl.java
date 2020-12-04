package com.ahu.service;

import com.ahu.dao.MemberDao;
import com.ahu.dao.OrderDao;
import com.ahu.dao.SetmealDao;
import com.ahu.utils.DateUtils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author ：hodor007
 * @date ：Created in 2020/12/3
 * @description ：
 * @version: 1.0
 */
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private SetmealDao setmealDao;

    @Override
    public Map getBusinessReportData() throws Exception {
        Date thisWeekMonday = DateUtils.getThisWeekMonday();    //本周周一
        Date firstDay4ThisMonth = DateUtils.getFirstDay4ThisMonth();    //本月一号
        String reportDate = DateUtils.parseDate2String(new Date());    //今天

        Integer todayNewMember = memberDao.findMemberCountByDate(reportDate);
        Integer totalMember = memberDao.findMemberTotalCount();
        Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(DateUtils.parseDate2String(thisWeekMonday));
        Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(DateUtils.parseDate2String(firstDay4ThisMonth));
        Integer todayOrderNumber = orderDao.findOrderCountByDate(reportDate);
        Integer todayVisitsNumber = orderDao.findVisitsCountByDate(reportDate);
        Integer thisWeekOrderNumber = orderDao.findOrderCountAfterDate(DateUtils.parseDate2String(thisWeekMonday));
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(DateUtils.parseDate2String(thisWeekMonday));
        Integer thisMonthOrderNumber = orderDao.findVisitsCountAfterDate(DateUtils.parseDate2String(firstDay4ThisMonth));
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(DateUtils.parseDate2String(firstDay4ThisMonth));
        List<Map<String, Object>> hotSetmeal = setmealDao.findHotSetmeal();

        Map map = new HashMap();
        map.put("todayNewMember",todayNewMember);
        map.put("totalMember",totalMember);
        map.put("thisWeekNewMember",thisWeekNewMember);
        map.put("thisMonthNewMember",thisMonthNewMember);
        map.put("todayOrderNumber",todayOrderNumber);
        map.put("todayVisitsNumber",todayVisitsNumber);
        map.put("thisWeekOrderNumber",thisWeekOrderNumber);
        map.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        map.put("thisMonthOrderNumber",thisMonthOrderNumber);
        map.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        map.put("hotSetmeal",hotSetmeal);
        return map;
    }

    /*
    reportDate:null,
                    todayNewMember :0,
                    totalMember :0,
                    thisWeekNewMember :0,
                    thisMonthNewMember :0,
                    todayOrderNumber :0,
                    todayVisitsNumber :0,
                    thisWeekOrderNumber :0,
                    thisWeekVisitsNumber :0,
                    thisMonthOrderNumber :0,
                    thisMonthVisitsNumber :0,
                    hotSetmeal :[
                        {name:'阳光爸妈升级肿瘤12项筛查（男女单人）体检套餐',setmeal_count:200,proportion:0.222},
                        {name:'阳光爸妈升级肿瘤12项筛查体检套餐',setmeal_count:200,proportion:0.222}
                    ]
     */
}
