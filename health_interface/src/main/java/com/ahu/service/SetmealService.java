package com.ahu.service;

import com.ahu.entity.PageResult;
import com.ahu.entity.QueryPageBean;
import com.ahu.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @author ：XXXX
 * @date ：Created in 2020/11/23
 * @description ：
 * @version: 1.0
 */

public interface SetmealService {

    void add(Setmeal setmeal, Integer[] ids);

    PageResult findPage(QueryPageBean queryPageBean);

    List<Setmeal> findAll();

    Setmeal findById(Integer id);

    List<Map<String,Object>> findSetmealCount();
}
