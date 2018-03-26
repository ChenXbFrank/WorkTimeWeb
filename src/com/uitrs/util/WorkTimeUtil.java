package com.uitrs.util;

import java.util.Date;

/**
 * 计算工时的工具类
 * Created by Administrator on 2016-11-19.
 */
public class WorkTimeUtil {
    private final static int morningStartTime = 9 * 60;  //  开始时间上班的时间
    private final static int morningEndTime = 12 * 60;  //  开始时间上班的时间
    private final static int afternoonStartTime = 13 * 60 + 30;  //  开始时间上班的时间
    private final static int afternoonEndTime = 18 * 60 + 30;  //  开始时间上班的时间
    private final static int nightStartTime = 19 * 60;  //  加班开始时间

    /**
     * 上午工作时间计算公式
     * @param startDate
     * @param endDate
     * @return
     */
    @SuppressWarnings("deprecation")
	public static int calculateMorningTime(Date startDate, Date endDate) {
        int morningTime = 0;
        if (startDate != null && endDate != null) {
            int startTime = startDate.getHours() * 60 + startDate.getMinutes();
            int endTime = endDate.getHours() * 60 + endDate.getMinutes();
            if (startTime <= morningStartTime && endTime >= morningEndTime) {
                morningTime = 180;
            } else if (startTime > morningStartTime && endTime >= morningEndTime) {   //  迟到
                morningTime = 180 - (startTime - morningStartTime);
            } else if (startTime <= morningStartTime && endTime < morningEndTime) {  //  早退
                morningTime = 180 - (morningEndTime - endTime);
            }
            else if(startTime>morningStartTime && endTime< morningEndTime){   //既迟到又早退
                morningTime=endTime-startTime;
            }
        }
        return morningTime > 0 ? morningTime : 0;
    }

    /**
     * 下午工作时间
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int calculateAfternoonTime(Date startDate, Date endDate) {
        int afternoonWorkTime = 0;
        if (startDate != null && endDate != null) {
            @SuppressWarnings("deprecation")
			int startTime = startDate.getHours() * 60 + startDate.getMinutes();
            @SuppressWarnings("deprecation")
			int endTime = endDate.getHours() * 60 + endDate.getMinutes();
            if (startTime <= afternoonStartTime && endTime >= afternoonEndTime) {
                afternoonWorkTime = 300;
            } else if (startTime > afternoonStartTime && endTime >= afternoonEndTime) {   //  迟到
                afternoonWorkTime = 300 - (startTime - afternoonStartTime);
            } else if (startTime <= afternoonStartTime && endTime < afternoonEndTime) {  //  早退
                afternoonWorkTime = 300 - (afternoonEndTime - endTime);
            } else if(startTime>afternoonStartTime && endTime< afternoonEndTime){   // 既迟到又早退
                afternoonWorkTime=endTime-startTime;
            }
        }
        return afternoonWorkTime > 0 ? afternoonWorkTime : 0;
    }

    /**
     * 加班工作时间
     * @param startDate
     * @param endDate
     * @return
     */
    @SuppressWarnings("deprecation")
	public static int calculateNightTime(Date startDate, Date endDate) {
        int nightWorkTime = 0;
        if (startDate != null && endDate != null) {
            int endTime = endDate.getHours() * 60 + endDate.getMinutes();
            if (endTime > nightStartTime) {
                nightWorkTime = endTime - nightStartTime;
            }
        }
        return nightWorkTime;
    }
}
