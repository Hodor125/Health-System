package com.ahu.jobs;

import com.ahu.constant.RedisConstant;
import com.ahu.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * @author ：hodor007
 * @date ：Created in 2020/11/24
 * @description ：
 * @version: 1.0
 */
public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;

    public void clearImg(){
        Jedis jedis = jedisPool.getResource();
        //根据redis中两个集合进行差值计算，得到垃圾图片的集合，将其从redis和七牛云服务器中同时删除
        Set<String> sdiff = jedis.sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        //进行判空处理
        if(sdiff != null){
            for (String s : sdiff) {
                QiniuUtils.deleteFileFromQiniu(s);
                jedis.srem(RedisConstant.SETMEAL_PIC_RESOURCES,s);
            }
        }
        jedis.close();
    }
}
