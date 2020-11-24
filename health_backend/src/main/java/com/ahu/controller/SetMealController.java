package com.ahu.controller;

import com.ahu.constant.MessageConstant;
import com.ahu.constant.RedisConstant;
import com.ahu.entity.PageResult;
import com.ahu.entity.QueryPageBean;
import com.ahu.entity.Result;
import com.ahu.pojo.Setmeal;
import com.ahu.service.SetmealService;
import com.ahu.utils.QiniuUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

/**
 * @author ：hodor007
 * @date ：Created in 2020/11/23
 * @description ：
 * @version: 1.0
 */
@RestController
@RequestMapping("/setmeal")
public class SetMealController {
    @Reference
    private SetmealService setmealService;
    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/upload")
    public Result upload(MultipartFile imgFile){
        Jedis jedis = jedisPool.getResource();
        try {
            String originalFilename = imgFile.getOriginalFilename();
            int index = originalFilename.lastIndexOf('.');
            String substring = originalFilename.substring(index);
            String fileName = UUID.randomUUID().toString() + substring;
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);
            jedis.sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            return new Result(true, MessageConstant.UPLOAD_SUCCESS,fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        } finally {
            //归还连接
            jedis.close();
        }
    }

    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] ids){
        try {
            setmealService.add(setmeal,ids);
            return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = setmealService.findPage(queryPageBean);
        return pageResult;
    }
}
