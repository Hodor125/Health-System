package com.ahu.service;

import com.ahu.entity.PageResult;
import com.ahu.entity.QueryPageBean;
import com.ahu.pojo.Setmeal;

/**
 * @author ：XXXX
 * @date ：Created in 2020/11/23
 * @description ：
 * @version: 1.0
 */

public interface SetmealService {

    void add(Setmeal setmeal, Integer[] ids);

    PageResult findPage(QueryPageBean queryPageBean);
}
