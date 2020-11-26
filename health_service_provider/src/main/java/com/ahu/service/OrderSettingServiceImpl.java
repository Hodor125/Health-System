package com.ahu.service;

import com.ahu.constant.MessageConstant;
import com.ahu.dao.OrderSettingDao;
import com.ahu.entity.Result;
import com.ahu.pojo.OrderSetting;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：hodor007
 * @date ：Created in 2020/11/25
 * @description ：
 * @version: 1.0
 */
@Service
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void upload(List<OrderSetting> orderSettingList) {
        if(orderSettingList != null && orderSettingList.size() > 0){
            for (OrderSetting orderSetting : orderSettingList) {
                //检查是否有这一天的数据
                Long count = orderSettingDao.findCountByDate(orderSetting.getOrderDate());
                //如果有就修改
                if(count > 0){
                    orderSettingDao.setOrderNumByDate(orderSetting);
                } else {
                    //没有就添加数据
                    orderSettingDao.addOrderList(orderSetting);
                }
            }
        }
    }

    //{ date: 1, number: 120, reservations: 1 },
    @Override
    public List<Map> getOrderListByMonth(String date) {
        String start_date = date + "-1";
        String end_date = date + "-31";
        Map map = new HashMap();
        map.put("startDate",start_date);
        map.put("endDate",end_date);
        List<OrderSetting> orderSettingList = orderSettingDao.getOrderListByMonth(map);
        List<Map> res = new ArrayList<>();
        if(orderSettingList != null && orderSettingList.size() > 0){
            for (OrderSetting orderSetting : orderSettingList) {
                Map resMap = new HashMap();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String _date = sdf.format(orderSetting.getOrderDate());
                Integer day = Integer.parseInt(_date.split("-")[2]);
                resMap.put("date",day);
                resMap.put("number",orderSetting.getNumber());
                resMap.put("reservations",orderSetting.getReservations());
                res.add(resMap);
            }
        }
        return res;
    }

    @Override
    public void setNumByDay(OrderSetting orderSetting) {
        if(orderSetting.getNumber() > 0){
            //修改预约数
            orderSettingDao.setNumByDay(orderSetting);
        } else {
            //当前日期没有进行预约设置，需要进行添加操作
            orderSettingDao.addOrderList(orderSetting);
        }
    }
}
