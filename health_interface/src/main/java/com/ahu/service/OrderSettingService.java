package com.ahu.service;

import com.ahu.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * @author ：XXXX
 * @date ：Created in 2020/11/25
 * @description ：
 * @version: 1.0
 */
public interface OrderSettingService {

    void upload(List<OrderSetting> orderSettingList);

    List<Map> getOrderListByMonth(String date);

    void setNumByDay(OrderSetting orderSetting);
}
