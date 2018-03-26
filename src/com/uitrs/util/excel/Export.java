package com.uitrs.util.excel;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import com.uitrs.model.WorkTime;

/**
 * 本地导出类
 * @author ChenXb
 *
 */
public class Export {
	public void export(){
		// 导出excel的标题
		String title = "锤石科技考勤记录";
		String templateFileName = "xtask_export_template.xls"; 
		URL url = this.getClass().getClassLoader().getResource("");	// class文件所在的路径
		String templateFilePath = url.getPath() + templateFileName;	// 模板文件路径
		String exportFilePath = url.getPath() + title + ".xls"; 	// 导出文件的路径
		//所有的工时
		String sql="select * from worktime";
		List<WorkTime> list=WorkTime.dao.find(sql);
		//将导出excel的数据和导出excel的标题放到集合中
		/**
		 * 这儿申明的map集合是为了后面transformXLS方法转化需要传入Excel里面的一个Map，
		 * jxls根据Template里面的定义和Map里面的对象对Template进行解析，
		 * 将Map里面的对象值填入到Excel文件中。
		 */
		Map<String, Object> datamap = new HashMap<String, Object>();
		datamap.put("title", title);	// 导出excel的标题
		datamap.put("list", list);	// 导出excel的数据
		XLSTransformer transformer = new XLSTransformer();
		try {
			transformer.transformXLS(templateFilePath, datamap, exportFilePath);
			System.out.println("已经导出文件至：" + exportFilePath);
		} catch (ParsePropertyException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
