package com.ahu.controller;

import com.ahu.constant.MessageConstant;
import com.ahu.entity.PageResult;
import com.ahu.entity.QueryPageBean;
import com.ahu.entity.Result;
import com.ahu.pojo.CheckGroup;
import com.ahu.service.CheckGroupSerice;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * @author ：hodor007
 * @date ：Created in 2020/11/22
 * @description ：
 * @version: 1.0
 */
@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {
    @Reference
    private CheckGroupSerice checkGroupSerice;

    //添加检查组
    @RequestMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup, Integer[] ids){
        try {
            checkGroupSerice.add(checkGroup,ids);
            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }

    @RequestMapping("/findByPage")
    public PageResult findByPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = checkGroupSerice.findByPage(queryPageBean);
        return pageResult;
    }

    @RequestMapping("findById")
    public Result findById(Integer id){
        try {
            CheckGroup checkGroup = checkGroupSerice.findById(id);
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    @RequestMapping("findCheckIdsByCheckGroupId")
    public Result findCheckIdsByCheckGroupId(Integer id){
        try {
            List<Integer> ids = checkGroupSerice.findCheckIdsByCheckGroupId(id);
            return new Result(true,MessageConstant.QUERY_CHECKITEMIDS_BY_CHECKGROUPID_SUCCESS,ids);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.QUERY_CHECKITEMIDS_BY_CHECKGROUPID_FAIL);
        }
    }

    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckGroup checkGroup, Integer[] ids){
        try {
            checkGroupSerice.edit(checkGroup,ids);
            return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }

    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<CheckGroup> checkGroupList = checkGroupSerice.findAll();
            if(checkGroupList != null && checkGroupList.size() > 0){
                return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroupList);
            } else {
                return new Result(true,MessageConstant.QUERY_CHECKGROUP_FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }
}
