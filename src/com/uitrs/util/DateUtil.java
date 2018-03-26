package com.uitrs.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016-11-19.
 */
public class DateUtil {
    /**
     * 计算指定月份的天数
     * @param date
     * @return
     */
	@SuppressWarnings("deprecation")
	public static int  calculateDays(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.set(date.getYear(), date.getMonth(), 1);
        return   calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
    /**
	 * 时间格式转换
	 * */
	public static String newDate(Date nowTime) {
		SimpleDateFormat time=new SimpleDateFormat("yyyy/MM/dd HH:mm"); 
		return time.format(nowTime);
	}
}
