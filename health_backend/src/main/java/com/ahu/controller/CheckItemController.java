package com.ahu.controller;

import com.ahu.constant.MessageConstant;
import com.ahu.entity.PageResult;
import com.ahu.entity.QueryPageBean;
import com.ahu.entity.Result;
import com.ahu.pojo.CheckItem;
import com.ahu.service.CheckItemService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.print.attribute.standard.RequestingUserName;
import java.util.List;

/**
 * 检查项控制器
 * @author ：hodor007
 * @date ：Created in 2020/11/21
 * @description ：
 * @version: 1.0
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {
    @Reference
    private CheckItemService checkItemService;

    @RequestMapping("/add")
    public Result addCheckItem(@RequestBody CheckItem checkItem){
        try {
            checkItemService.addItem(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = checkItemService.findPage(queryPageBean);
        return pageResult;
    }

    @RequestMapping("/deleteById")
    @PreAuthorize("hasAnyAuthority('CHECKITEM_DELETE')")
    public Result deleteById(Integer id){
        try {
            checkItemService.deleteById(id);
        } catch (RuntimeException e){
            return new Result(false,e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }

        return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    //根据id删除
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            CheckItem checkItem = checkItemService.findById(id);
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    @RequestMapping("/edit")
    @PreAuthorize("hasAnyAuthority('CHECKITEM_EDIT')")
    public Result edit(@RequestBody CheckItem checkItem){
        try {
            checkItemService.edit(checkItem);
            return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }
    }

    @RequestMapping("findAll")
    public Result findAll(){
        try {
            List<CheckItem> checkItemList = checkItemService.findAll();
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkItemList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }
}
