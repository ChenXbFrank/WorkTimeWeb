package com.uitrs.util;
import com.uitrs.model.Record;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Created by Administrator on 2016-11-19.
 */
public class FileUtil {
    public static List<Record> convertLogToRecords(String filePath) {
        List<Record> records = new ArrayList<Record>();
        try {
            String encoding = "gbk";
            File file = new File(filePath);
            if (file.isFile() && file.exists()) { //判断文件是否存在
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);//考虑到编码格式
				@SuppressWarnings("resource")
				Buffered bufferedReader = new Buffered(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    //将文件用空格符分割开   如果是特殊字符的时候 需要\\ 防止转义
                    String[] a = lineTxt.split("\t");
                    if(StringUtils.isBlank(a[3])){
//                        System.out.println("该用户已离职！");
                    }
                    else {
                        String employeeNo = a[2].trim();  //  员工号
                        String employeeName = a[3].trim();  //  员工姓名
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                        Date dateTime = sdf.parse(a[4]);
                        Record record=new Record();
                        record.setEmployeeName(employeeName);
                        record.setEmployeeNo(employeeNo);
                        record.setDateTime(dateTime);
                        records.add(record);
                    }
                }
                read.close();
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return records;
    }

}
