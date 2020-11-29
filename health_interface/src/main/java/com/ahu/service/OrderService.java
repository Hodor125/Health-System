package com.ahu.service;

import com.ahu.entity.Result;

import java.util.Map;

/**
 * @author ：XXXX
 * @date ：Created in 2020/11/28
 * @description ：
 * @version: 1.0
 */
public interface OrderService {

    Result order(Map map) throws Exception;

    Map findById(Integer id);
}
