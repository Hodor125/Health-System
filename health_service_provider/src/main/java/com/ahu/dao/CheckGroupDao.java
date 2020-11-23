package com.ahu.dao;

import com.ahu.pojo.CheckGroup;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author ：XXXX
 * @date ：Created in 2020/11/22
 * @description ：
 * @version: 1.0
 */
public interface CheckGroupDao {
    //添加检查组
    void add(CheckGroup checkGroup);

    void setCheckGroupAndCheckItem(@Param("map") Map<String, Integer> map);

    Page<CheckGroup> findByPage(@Param("queryString") String queryString);

    CheckGroup findById(Integer id);

    List<Integer> findCheckIdsByCheckGroupId(Integer id);

    void deleteCheckIdsAndCheckGroupId(Integer id);

    void updateCheckGroup(CheckGroup checkGroup);

    List<CheckGroup> findAll();
}
