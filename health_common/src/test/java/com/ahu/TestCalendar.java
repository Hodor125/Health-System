package com.ahu;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * @author ：hodor007
 * @date ：Created in 2020/12/3
 * @description ：
 * @version: 1.0
 */
public class TestCalendar {
//    @Test
    public void TestCalendar(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        Date time = calendar.getTime();
    }
}
