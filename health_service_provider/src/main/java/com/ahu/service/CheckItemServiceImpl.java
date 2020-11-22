package com.ahu.service;

import com.ahu.constant.MessageConstant;
import com.ahu.dao.CheckItemDao;
import com.ahu.entity.PageResult;
import com.ahu.entity.QueryPageBean;
import com.ahu.entity.Result;
import com.ahu.pojo.CheckItem;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 检查项服务
 * @author ：hodor007
 * @date ：Created in 2020/11/21
 * @description ：
 * @version: 1.0
 */
@Service
@Transactional
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemDao checkItemDao;
    //新增检查项
    @Override
    public void addItem(CheckItem checkItem) {
        checkItemDao.addItem(checkItem);
    }

    //分页查询，使用pageHelper插件
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
//        String queryString = queryPageBean.getQueryString();
//        if(queryString != null && queryString.length() > 0){
//            PageHelper.startPage(1, 1);
//        }
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<CheckItem> page = checkItemDao.selectByCondition(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    //删除检查项，如果它不在检查组里面
    @Override
    public void deleteById(Integer id) {
        Long count = checkItemDao.findCountByCheckItemId(id);
        if(count > 0){
            throw new RuntimeException(MessageConstant.CHECKITEM_HAS_ASSOCIATION);
        }
        checkItemDao.deleteById(id);
    }

    //根据id查询检查项
    @Override
    public CheckItem findById(Integer id) {
        CheckItem checkItem = checkItemDao.findById(id);
        return checkItem;
    }

    //修改检查项
    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.updateById(checkItem);
    }
}
