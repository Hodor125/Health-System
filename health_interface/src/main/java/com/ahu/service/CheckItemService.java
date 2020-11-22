package com.ahu.service;

import com.ahu.entity.PageResult;
import com.ahu.entity.QueryPageBean;
import com.ahu.entity.Result;
import com.ahu.pojo.CheckItem;

/**
 * @author ：XXXX
 * @date ：Created in 2020/11/21
 * @description ：
 * @version: 1.0
 */
public interface CheckItemService {
    void addItem(CheckItem checkItem);

    PageResult findPage(QueryPageBean queryPageBean);

    void deleteById(Integer id);

    CheckItem findById(Integer id);

    void edit(CheckItem checkItem);
}
