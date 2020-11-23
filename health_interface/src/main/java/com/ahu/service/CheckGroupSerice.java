package com.ahu.service;

import com.ahu.entity.PageResult;
import com.ahu.entity.QueryPageBean;
import com.ahu.entity.Result;
import com.ahu.pojo.CheckGroup;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 检查组事务
 * @author ：XXXX
 * @date ：Created in 2020/11/22
 * @description ：
 * @version: 1.0
 */
public interface CheckGroupSerice {
    void add(CheckGroup checkGroup, Integer[] ids);

    PageResult findByPage(QueryPageBean queryPageBean);

    CheckGroup findById(Integer id);

    List<Integer> findCheckIdsByCheckGroupId(Integer id);

    void edit(CheckGroup checkGroup, Integer[] ids);

    List<CheckGroup> findAll();
}
