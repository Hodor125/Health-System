package com.ahu.dao;

import com.ahu.pojo.CheckItem;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 框架扫描已经配置在spring-dao.xml文件中了，会动态代理生成实现类
 * @author ：hodor007
 * @date ：Created in 2020/11/21
 * @description ：
 * @version: 1.0
 */
public interface CheckItemDao {
    Integer addItem(CheckItem checkItem);

    Page<CheckItem> selectByCondition(@Param("queryString") String queryString);

    Long findCountByCheckItemId(Integer id);

    void deleteById(Integer id);

    CheckItem findById(Integer id);

    void updateById(CheckItem checkItem);

    List<CheckItem> findAll();
}
