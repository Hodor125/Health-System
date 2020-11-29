package com.ahu.service;

import com.ahu.constant.MessageConstant;
import com.ahu.dao.MemberDao;
import com.ahu.dao.OrderDao;
import com.ahu.dao.OrderSettingDao;
import com.ahu.entity.Result;
import com.ahu.pojo.Member;
import com.ahu.pojo.Order;
import com.ahu.pojo.OrderSetting;
import com.ahu.utils.DateUtils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ：hodor007
 * @date ：Created in 2020/11/28
 * @description ：
 * @version: 1.0
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    //体检预约，抛出异常让controller接收
    @Override
    public Result order(Map map) throws Exception {
        //检查当天是否预约设置，防止各种异常情况
        String orderDate = (String) map.get("orderDate");
        Date date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSettingByDate = orderSettingDao.getOrderSettingByDate(date);
        if(orderSettingByDate == null){
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //检查所选的日期是否预约满
        int number = orderSettingByDate.getNumber();
        int reservations = orderSettingByDate.getReservations();
        if(reservations >= number){
            return new Result(false, MessageConstant.ORDER_FULL);
        }

        //查看用户是否已经注册过了，如果还未注册过则自动注册
        String tel = (String) map.get("telephone");
        Member member = memberDao.findByTelephone(tel);
        if(member == null){
            //未注册过
            member = new Member();
            member.setName((String) map.get("name"));
            member.setSex((String) map.get("sex"));
            member.setPhoneNumber((String) map.get("telephone"));
            member.setIdCard((String) map.get("idCard"));
            member.setRegTime(new Date());
            Integer rows = memberDao.add(member);
            System.out.println(rows);
        } else {
            //查看是否重复预约，同一人在同一天预约了同一个套餐
            Order order = new Order();
            order.setMemberId(member.getId());
            String setmealId = (String) map.get("setmealId");
            order.setSetmealId(Integer.parseInt(setmealId));
            order.setOrderDate(date);
            List<Order> byCondition = orderDao.findByCondition(order);
            if(byCondition != null && byCondition.size() > 0){
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        }

        //可以预约，预约人数+1，保存到数据库
        orderSettingByDate.setReservations(orderSettingByDate.getReservations() + 1);
        Integer rows = orderSettingDao.editReservationsByDate(orderSettingByDate);
        System.out.println(rows);

        //更改预约记录
        Order order = new Order();
        String setmealId = (String) map.get("setmealId");
        order.setSetmealId(Integer.parseInt(setmealId));
        order.setOrderDate(date);
            order.setMemberId(member.getId());
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        order.setOrderType((String) map.get("orderType"));
        orderDao.add(order);
        return new Result(true, MessageConstant.ORDER_SUCCESS,order.getId());
    }

    @Override
    public Map findById(Integer id) {
        //查找用户表和套餐表
        Map map = orderDao.findById4Detail(id);
        return map;
    }
}
