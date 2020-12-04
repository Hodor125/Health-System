package com.ahu.dao;

import com.ahu.pojo.Setmeal;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author ：XXXX
 * @date ：Created in 2020/11/23
 * @description ：
 * @version: 1.0
 */
public interface SetmealDao {

    void add(Setmeal setmeal);

    void setSetmealAndCheckGroupIds(@Param("map") Map map);

    Page<Setmeal> findPage(String queryString);

    List<Setmeal> findAll();

    Setmeal findById(Integer id);

    //进行套餐统计
    public List<Map<String,Object>> findSetmealCount();

    //查找最热门套餐
    List<Map<String, Object>> findHotSetmeal();
}
