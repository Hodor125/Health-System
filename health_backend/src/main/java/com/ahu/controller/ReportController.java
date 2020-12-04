package com.ahu.controller;

import com.ahu.constant.MessageConstant;
import com.ahu.entity.Result;
import com.ahu.service.MemberService;
import com.ahu.service.ReportService;
import com.ahu.service.SetmealService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ：hodor007
 * @date ：Created in 2020/12/3
 * @description ：
 * @version: 1.0
 */
@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    private MemberService memberService;
    @Reference
    private SetmealService setmealService;
    @Reference
    private ReportService reportService;

    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){
        ArrayList<String> listDate = new ArrayList<>();
        ArrayList<Integer> listCount = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        //添加过去一年的时间X轴
        for (int i = 1; i <= 12; i++) {
            calendar.add(Calendar.MONTH, -1);
            Date time = calendar.getTime();
            String dateTime = new SimpleDateFormat("yyyy-MM").format(time);
            listDate.add(dateTime);
        }
        Collections.reverse(listDate);

        //添加查询的数据
        try {
            listCount = memberService.findMemberCountByMonth(listDate);
            Map map = new HashMap();
            map.put("months",listDate);
            map.put("memberCount",listCount);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    //查询套餐数量统计
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){
        List<String> setmealNames = new ArrayList<>();
        Map resMap = new HashMap();

        try {
            List<Map<String, Object>> setmealCount = setmealService.findSetmealCount();
            for (Map<String, Object> map : setmealCount) {
                String name = (String) map.get("name");
                setmealNames.add(name);
            }
            resMap.put("setmealNames",setmealNames);
            resMap.put("setmealCount",setmealCount);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,resMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    //查询运营数据
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        try {
            Map resMap = reportService.getBusinessReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, resMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    //导出运营数据
    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try {
            //读取要填写的数据
            Map result = reportService.getBusinessReportData();
            //取出返回结果数据，准备将报表数据写入到Excel文件中
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            //获取exel模板的绝对路径
            String realPath = request.getSession().getServletContext().getRealPath("template")
                    + File.separator + "report_template.xlsx";

            //读取模板创建Excel对象，使用现有的模板，构造函数的参数是文件输入流
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(new File(realPath)));
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);

            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);    //日期

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            int num = 12;
            for (Map map : hotSetmeal) {
                row = sheet.getRow(num++);
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row.getCell(4).setCellValue(name);
                row.getCell(5).setCellValue(setmeal_count);
                row.getCell(6).setCellValue(proportion.doubleValue());
            }

            //传回表格，通过输出流传输回表格
            ServletOutputStream os = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            //以附件的形式下载
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            xssfWorkbook.write(os);
            os.flush();
            os.close();
            xssfWorkbook.close();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
        return null;
    }
}
