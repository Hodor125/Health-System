package com.ahu.controller;

import com.ahu.constant.MessageConstant;
import com.ahu.constant.RedisConstant;
import com.ahu.entity.Result;
import com.ahu.pojo.Order;
import com.ahu.service.OrderService;
import com.ahu.utils.SMSUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @author ：hodor007
 * @date ：Created in 2020/11/28
 * @description ：
 * @version: 1.0
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private OrderService orderService;

    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map){
        //检查验证码
        String validateCode = (String) map.get("validateCode");
        //从redis中获取验证码
        Jedis jedis = jedisPool.getResource();
        String validateCodeInRedis = jedis.get(map.get("telephone") + RedisConstant.SENDTYPE_ORDER);
        jedis.close();
        if(validateCode == null || validateCodeInRedis == null || !validateCode.equals(validateCodeInRedis)){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        Result result = null;

        //调用体检预约服务
        try {
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            result = orderService.order(map);
        } catch (Exception e) {
            e.printStackTrace();
            //直接返回是可以的，js是弱类型，flag没有复赋值就是false
            return result;
        }

        //发送短信通知
        if(result.isFlag()){
            try {
                String tel = (String) map.get("telephone");
                String orderDate = (String) map.get("orderDate");
                SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE, tel, orderDate);
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Map map = orderService.findById(id);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
