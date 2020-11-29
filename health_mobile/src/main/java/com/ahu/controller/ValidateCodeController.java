package com.ahu.controller;

import com.ahu.constant.MessageConstant;
import com.ahu.constant.RedisConstant;
import com.ahu.entity.Result;
import com.ahu.utils.SMSUtils;
import com.ahu.utils.ValidateCodeUtils;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author ：hodor007
 * @date ：Created in 2020/11/27
 * @description ：
 * @version: 1.0
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
        //生成验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
        //发送到手机
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }

        //将验证码存在redis中，并设置有效时间
        Jedis jedis = jedisPool.getResource();
        jedis.setex(telephone + RedisConstant.SENDTYPE_ORDER,300,validateCode.toString());
        jedis.close();
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
