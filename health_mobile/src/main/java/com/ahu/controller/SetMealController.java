package com.ahu.controller;

import com.ahu.constant.MessageConstant;
import com.ahu.entity.Result;
import com.ahu.pojo.Setmeal;
import com.ahu.service.SetmealService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ：hodor007
 * @date ：Created in 2020/11/26
 * @description ：
 * @version: 1.0
 */
@RestController
@RequestMapping("/setmeal")
public class SetMealController {
    @Reference
    private SetmealService setmealService;

    @RequestMapping("/getSetmeal")
    public Result getSetmeal(){
        try {
            List<Setmeal> setmealList = setmealService.findAll();
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,setmealList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Setmeal setmeal =  setmealService.findById(id);
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}
