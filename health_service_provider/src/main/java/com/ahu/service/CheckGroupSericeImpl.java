package com.ahu.service;

import com.ahu.dao.CheckGroupDao;
import com.ahu.entity.PageResult;
import com.ahu.entity.QueryPageBean;
import com.ahu.entity.Result;
import com.ahu.pojo.CheckGroup;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：hodor007
 * @date ：Created in 2020/11/22
 * @description ：
 * @version: 1.0
 */
@Service
@Transactional
public class CheckGroupSericeImpl implements CheckGroupSerice {
    @Autowired
    private CheckGroupDao checkGroupDao;

    @Override
    public void add(CheckGroup checkGroup, Integer[] ids) {
        //添加分组
        checkGroupDao.add(checkGroup);
        //设置关联
        setCheckGroupAndCheckItem(checkGroup.getId(),ids);
    }

    private void setCheckGroupAndCheckItem(Integer checkGroupId,Integer[] ids){
        if(ids != null && ids.length > 0){
            for (Integer id : ids) {
                Map<String, Integer> map = new HashMap<>();
                map.put("checkGroupId",checkGroupId);
                map.put("id",id);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }

    @Override
    public PageResult findByPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<CheckGroup> page = checkGroupDao.findByPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public CheckGroup findById(Integer id) {
        CheckGroup checkGroup = checkGroupDao.findById(id);
        return checkGroup;
    }

    @Override
    public List<Integer> findCheckIdsByCheckGroupId(Integer id) {
        List<Integer> ids = checkGroupDao.findCheckIdsByCheckGroupId(id);
        return ids;
    }

    @Override
    public void edit(CheckGroup checkGroup, Integer[] ids) {
        //删除现有的检查组和检查项的关系
        checkGroupDao.deleteCheckIdsAndCheckGroupId(checkGroup.getId());
        //插入编辑后的检查组和检查项的关系
        setCheckGroupAndCheckItem(checkGroup.getId(),ids);
        //修改检查组
        checkGroupDao.updateCheckGroup(checkGroup);
    }

    @Override
    public List<CheckGroup> findAll() {
        List<CheckGroup> checkGroupList = checkGroupDao.findAll();
        return checkGroupList;
    }
}
