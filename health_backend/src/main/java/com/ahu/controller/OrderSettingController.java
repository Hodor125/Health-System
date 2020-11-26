package com.ahu.controller;

import com.ahu.constant.MessageConstant;
import com.ahu.entity.Result;
import com.ahu.pojo.OrderSetting;
import com.ahu.service.OrderSettingService;
import com.ahu.utils.POIUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ：hodor007
 * @date ：Created in 2020/11/25
 * @description ：
 * @version: 1.0
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    @Reference
    private OrderSettingService orderSettingService;

    @RequestMapping("/upload")
    public Result upload(MultipartFile excelFile){
        List<OrderSetting> orderSettingList = new ArrayList<>();
        try {
            List<String[]> rows = POIUtils.readExcel(excelFile);
            if(rows != null && rows.size() > 0){
                //遍历行，存入对象
                for (String[] row : rows) {
                    OrderSetting orderSetting = new OrderSetting();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    Date date = simpleDateFormat.parse(row[0]);
                    orderSetting.setOrderDate(date);
                    orderSetting.setNumber(Integer.parseInt(row[1]));
                    orderSettingList.add(orderSetting);
                }
                //调用service保存
                orderSettingService.upload(orderSettingList);
                return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return null;
    }

    @RequestMapping("/getOrderListByMonth")
    public Result getOrderListByMonth(String date){
        try {
            List<Map> dates = orderSettingService.getOrderListByMonth(date);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,dates);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.QUERY_ORDER_FAIL);
        }
    }

    @RequestMapping("/setOrderNum")
    public Result setOrderNum(@RequestBody OrderSetting orderSetting){
        try {
            orderSettingService.setNumByDay(orderSetting);
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
