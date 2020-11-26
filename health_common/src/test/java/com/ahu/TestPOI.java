package com.ahu;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author ：hodor007
 * @date ：Created in 2020/11/24
 * @description ：
 * @version: 1.0
 */
public class TestPOI {
//    @Test
    public void TestPOI1(){
        XSSFWorkbook workbook = null;
        try {
            //创建工作簿
            workbook = new XSSFWorkbook("D:\\java\\POI.xlsx");
            //获取工作表
            XSSFSheet sheet = workbook.getSheetAt(0);
            //遍历
            for (Row cells : sheet) {
                System.out.println();
                for (Cell cell : cells) {
                    String value = cell.getStringCellValue();
                    System.out.print(value);
//                    System.out.print(cell + " ");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    @Test
    public void TestPOI2(){
        XSSFWorkbook workbook = null;
        try {
            //创建工作簿
            workbook = new XSSFWorkbook("D:\\POI.xlsx");
            //获取工作表
            XSSFSheet sheet = workbook.getSheetAt(0);
            int lastRowNum = sheet.getLastRowNum();
            for (int i = 0; i <= lastRowNum; i++) {
                XSSFRow row = sheet.getRow(i);
                short lastCellNum = row.getLastCellNum();
                for (int j = 0; j < lastCellNum; j++) {
                    String stringCellValue = row.getCell(j).getStringCellValue();
                    System.out.print(stringCellValue + " ");
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void TestPOI3(){
        //在内存中创建表格
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建工作表，指定工作表格
        XSSFSheet sheet = workbook.createSheet("AHU_COM");
        //创建行
        XSSFRow row0 = sheet.createRow(5);
        row0.createCell(0).setCellValue("学号");
        row0.createCell(1).setCellValue("姓名");
        row0.createCell(2).setCellValue("成绩");

        XSSFRow row1 = sheet.createRow(6);
        row1.createCell(0).setCellValue("E001");
        row1.createCell(1).setCellValue("Jack");
        row1.createCell(2).setCellValue("23");

        XSSFRow row2 = sheet.createRow(7);
        row2.createCell(0).setCellValue("E002");
        row2.createCell(1).setCellValue("Mark");
        row2.createCell(2).setCellValue("98");

        try {
            FileOutputStream fileOutputStream = new FileOutputStream("D:\\java\\INSERT.xlsx");
            workbook.write(fileOutputStream);
//            fileOutputStream.flush();
            fileOutputStream.close();
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
