package com.ahu.controller;

import com.ahu.constant.MessageConstant;
import com.ahu.entity.Result;
import com.ahu.pojo.Member;
import com.ahu.service.MemberService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aliyuncs.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @author ：hodor007
 * @date ：Created in 2020/11/29
 * @description ：
 * @version: 1.0
 */
@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private MemberService memberService;

    @RequestMapping("/login")
    public Result login(@RequestBody Map map, HttpServletResponse response){
        //获取参数
        String tel = (String) map.get("telephone");
        String val = (String) map.get("validateCode");
        //从redis中获取验证码
        Jedis jedis = jedisPool.getResource();
        String _val = jedis.get(tel);
        if(_val == null || !_val.equals(val)){
            jedis.close();
            return new Result(false, MessageConstant.LOGIN_FAIL);
        }
        //查找是否是新用户
        Member member = null;
        try {
            member = memberService.findByTelephone(tel);
            if(member == null){
                //如果未注册自动注册
                member = new Member();
                member.setPhoneNumber(tel);
                member.setRegTime(new Date());
                memberService.add(member);
            }
        } catch (Exception e) {
            jedis.close();
            e.printStackTrace();
            return new Result(false, MessageConstant.LOGIN_FAIL);
        }

        //保存到Cookie,为什么
        Cookie cookie = new Cookie("login_member_tel",tel);
        cookie.setMaxAge(60 * 60 * 24 * 30);
        cookie.setPath("/");
        response.addCookie(cookie);//保存到浏览器
        //保存会员信息到redis，为什么(方便部署集群)
        String jsonMember = JSON.toJSON(member).toString();
        jedis.setex(tel,60 * 30,jsonMember);
        jedis.close();
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }
}
