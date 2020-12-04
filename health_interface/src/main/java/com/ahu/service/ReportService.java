package com.ahu.service;

import java.util.Map;

/**
 * 查询统计报表
 * @author ：XXXX
 * @date ：Created in 2020/12/3
 * @description ：
 * @version: 1.0
 */
public interface ReportService {

    Map getBusinessReportData() throws Exception;
}
