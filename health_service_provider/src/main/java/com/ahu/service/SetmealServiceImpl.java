package com.ahu.service;

import com.ahu.constant.MessageConstant;
import com.ahu.constant.RedisConstant;
import com.ahu.dao.SetmealDao;
import com.ahu.entity.PageResult;
import com.ahu.entity.QueryPageBean;
import com.ahu.pojo.Setmeal;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：hodor007
 * @date ：Created in 2020/11/23
 * @description ：
 * @version: 1.0
 */
@Service
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;
    @Autowired
    private JedisPool jedisPool;
    @Override
    public void add(Setmeal setmeal, Integer[] ids) {
        //保存检查套餐
        setmealDao.add(setmeal);
        //保存对应关系
        setSetmealAndCheckGroupIds(setmeal.getId(),ids);
        //图片名字存入数据库之后顺便存入redis以便清理垃圾图片
        Jedis jedis = jedisPool.getResource();
        jedis.sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
        jedis.close();
    }

    private void setSetmealAndCheckGroupIds(Integer setMealId,Integer[] ids){
        if(ids != null && ids.length > 0){
            for (Integer id : ids) {
                Map map = new HashMap<>();
                map.put("setMealId",setMealId);
                map.put("checkGroupId",id);
                setmealDao.setSetmealAndCheckGroupIds(map);
            }
        }
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<Setmeal> page = setmealDao.findPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public List<Setmeal> findAll() {
        List<Setmeal> setmealList = setmealDao.findAll();
        return setmealList;
    }

    //级联查询
    @Override
    public Setmeal findById(Integer id) {
        Setmeal setmeal = setmealDao.findById(id);
        return setmeal;
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {
        List<Map<String, Object>> setmealCount = setmealDao.findSetmealCount();
        return setmealCount;
    }
}
