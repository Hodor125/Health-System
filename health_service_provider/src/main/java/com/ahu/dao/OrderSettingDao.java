package com.ahu.dao;

import com.ahu.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ：XXXX
 * @date ：Created in 2020/11/25
 * @description ：
 * @version: 1.0
 */
public interface OrderSettingDao {

    Long findCountByDate(Date orderDate);

    void setOrderNumByDate(OrderSetting orderSetting);

    void addOrderList(OrderSetting orderSetting);

    List<OrderSetting> getOrderListByMonth(@Param("map") Map map);

    void setNumByDay(OrderSetting orderSetting);
}
