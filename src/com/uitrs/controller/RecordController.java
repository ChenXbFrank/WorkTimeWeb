package com.uitrs.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import com.jfinal.core.Controller;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.upload.UploadFile;
import com.uitrs.model.Employee;
import com.uitrs.model.Record;
import com.uitrs.model.User;
import com.uitrs.model.WorkTime;
import com.uitrs.util.CommonUtil;

/**
 * 打卡记录的controller
 * @author ChenXb
 *
 */
public class RecordController extends Controller{
	/**第一步
	 * 导入记录
	 */
	public void index(){
		//需导入的文件路径
		String filePath=(String) getSession().getAttribute("filePath");
		if(filePath!=null){
			CommonUtil common=new CommonUtil();
			common.importRecord(filePath);
			String sql="select * from employee";
			List<Employee> list=Employee.dao.find(sql); 
			setAttr("list", list);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			//导入数据的同时 创建工时
			String sql1="select * from record";
			List<Record> list1=Record.dao.find(sql1);
			List<Integer> yearList=new ArrayList<Integer>();
			List<Integer> monthList=new ArrayList<Integer>();
			for(Record red:list1){
				String date=sdf.format(red.getDateTime());
				String a[] =date.split("-");
				int year=Integer.parseInt(a[0]);
				if(!yearList.contains(year)){
					yearList.add(year);
				}
				int month=Integer.parseInt(a[1]);
				if(!monthList.contains(month)){
					monthList.add(month);
				}
			}
			CommonUtil com=new CommonUtil();
			//从list里边取出年月
			for(int i=0;i<monthList.size();i++){
				//创建工时
				com.createWorkTimes(yearList.get(0), monthList.get(i));
			}
		}
		String sql="select * from employee";
		List<Employee> list=Employee.dao.find(sql);
		setAttr("list", list);
		//当前登录用户
		User user=(User) getSession().getAttribute("user");
		setAttr("username",user.getUserName());
		getSession().removeAttribute("filePath");
		render("/attendance/pages/main.html");
	}
	
	
	
	/**
	 * 修改记录的操作
	 * @throws ParseException 
	 */
	public void edit() throws ParseException{
		Record record=new Record();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		//员工编号
		String employeeNo = getPara("employeeNo");
		//最早的打卡记录
		String morning = getPara("morning");
		String[] a=morning.split("-");
		int year=Integer.parseInt(a[0]);
		int month=Integer.parseInt(a[1]);
		//最晚的打卡记录
		String after = getPara("after");
		//备注
		String remarks = getPara("remarks");
		//该工时记录的id
		int worktimeId=getParaToInt("id");
		//返回给前端的标识
		boolean result = false;
		//通过工号去查询员工的名字
		String sql3="select * from employee where employeeno ="+employeeNo+"";
		List<Employee> list3=Employee.dao.find(sql3);
		Employee employee=list3.get(0);
		String employeename=employee.getEmployeeName();
		//通过员工号和最早的打卡记录去查询看是否有该记录 没有才添加
		String sql="select * from record where employeeno="+employeeNo+" and datetime='"+morning+"'";
		List<Record> list=Record.dao.find(sql);
		if(list.size()==0){
			record.setEmployeeNo(employeeNo);
			record.setEmployeeName(employeename);
			record.setDateTime(sdf.parse(morning));
        	record.save();
        	result = true;
        	CommonUtil com=new CommonUtil();
    		//创建工时
    		com.createWorkTimes(year, month);
        }
		//通过员工号和最早的打卡记录去查询看是否有该记录 没有才添加
		String sql1="select * from record where employeeno="+employeeNo+" and datetime='"+after+"'";
		List<Record> list1=Record.dao.find(sql1);
		if(list1.size()==0){
			record.setEmployeeNo(employeeNo);
			record.setEmployeeName(employeename);
			record.setDateTime(sdf.parse(after));
        	record.save();
        	result = true;
        	//添加成功后要再创建一次工时
        	CommonUtil com=new CommonUtil();
    		//创建工时
    		com.createWorkTimes(year, month);
        }
		//通过wirktimeid查询该工时记录 添加修改一个备注信息
		String sql2="select * from worktime where id= "+worktimeId+"";
		List<WorkTime> list2=WorkTime.dao.find(sql2);
		if(list2.size()>0){
			WorkTime worktime=list2.get(0);
			worktime.set("id", worktimeId)
					.set("remarks",remarks)
					.update();
			result=true;
		}
		System.out.println("添加成功！");
		renderJson(result);
	}
	
	/**
	 * 添加打卡记录
	 * @throws ParseException 
	 */
	public void add() throws ParseException{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		//员工编号
		String employeeNo = getPara("employeeNo");
		//最早的打卡时间
		String morning = getPara("morning");
		String[] a=morning.split("-");
		int year=Integer.parseInt(a[0]);
		int month=Integer.parseInt(a[1]);
		//最晚的打卡时间
		String after = getPara("after");
		//备注信息   将这个备注添加给 工时记录最新的一条
		String remarks = getPara("remarks");
		//返回给前端的标识
		boolean result = false;
		//通过工号去查询员工的名字
		String sql3="select * from employee where employeeno ="+employeeNo+"";
		List<Employee> list3=Employee.dao.find(sql3);
		Employee employee=list3.get(0);
		String employeename=employee.getEmployeeName();
		//通过员工号和最早的打卡记录去查询看是否有该记录 没有才添加
		String sql="select * from record where employeeno="+employeeNo+" and datetime='"+morning+"'";
		List<Record> list=Record.dao.find(sql);
		if(list.size()==0){
			Record record=new Record();
			record.setEmployeeNo(employeeNo);
			record.setEmployeeName(employeename);
			record.setDateTime(sdf.parse(morning));
        	record.save();
        	result = true;
        }
		//通过员工号和最早的打卡记录去查询看是否有该记录 没有才添加
		String sql1="select * from record where employeeno="+employeeNo+" and datetime='"+after+"'";
		List<Record> list1=Record.dao.find(sql1);
		if(list1.size()==0){
			Record red=new Record();
			red.setEmployeeNo(employeeNo);
			red.setEmployeeName(employeename);
			red.setDateTime(sdf.parse(after));
        	red.save();
        	result = true;
        }
		
		CommonUtil com=new CommonUtil();
		//创建工时
		com.createWorkTimes(year, month);
		
		//通过wirktimeid查询该工时记录 添加修改一个备注信息
		String sql2="select * from worktime order by id desc limit 1 ";
		List<WorkTime> list2=WorkTime.dao.find(sql2);
		if(list2.size()>0){
			WorkTime worktime=list2.get(0);
			worktime.set("id", worktime.getId())
					.set("remarks",remarks)
					.update();
			result=true;
		}
		
		String id = "2";
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", result);
		data.put("employeeNo", id);
		renderJson(data);
	}
	
	
	/**
	 * 删除记录
	 */
	public void delete(){
		String employeeNo = getPara("employeeNo");
		System.out.println(employeeNo);
		String dateTime = getPara("dateTime");
		String b = dateTime.substring(0,10);
		String start=b+" 00:00:00";
		String end=b+" 23:59:59";
		//删除record
		String sql="delete from record where datetime between '"+start+"' and '"+end+"'";
		Db.update(sql);
		//删除该天的工时
		String sql1="delete from worktime where date= ?";
		Db.update(sql1,b);
		boolean result = true;
		renderJson(result);
	}
	
	/**
	 * 文件上传
	 */
	public void upload(){
		UploadFile file = getFile("filename");
		String name=file.getFileName();
		String filePath ="D:" +File.separator+"MyUpLoad"+File.separator+name;
		getSession().setAttribute("filePath", filePath);
		System.out.println(file.getUploadPath());
		redirect("/record/index");
    }
	
	
	/**
	 * 浏览器导出类
	 * @author pantao
	 * JXLS导出excel:http://jxls.sourceforge.net/samples/tagsample.html
	 * 遍历：jx:forEach 标签遍历数据
	 * 求和：$[SUM(B4)]
	 * 再了解一下jXLS的实现，它是主要基于两个开源项目，用POI进行Excel文件操作,用jexl进行表达式处理。
	 * 这两个项目本身都是比较成熟的项目 了。
	 * POI对Excel的任何字体、颜色、边框等几乎任何格式都能处理。
	 * jexl是参考JSTL实现的，常用的表达式计算都能处理，复杂的计算可直接调用 java对象的方法进行实现。
	 * 导出EXCEL
	 * @throws ParseException 
	 */
	public void getExcel() throws ParseException{
		// Jfinal获取项目文件路径
		String templateFilePath = JFinal.me().getServletContext().getRealPath("templete") + File.separator + "xtask_export_template.xls";
		// 导出excel的标题
		String title = "锤石科技考勤记录";
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
		
		datamap.put("list", list);	// 导出excel的数据
		datamap.put("title", title);	// 导出excel的标题
		InputStream in = null;
		try {
			in = new FileInputStream(templateFilePath);	// 将模板文件转换为文件流
			
			XLSTransformer transformer = new XLSTransformer();	// jxls生成excel
			
			HSSFWorkbook wb = (HSSFWorkbook) transformer.transformXLS(in, datamap);	// 将excel流转换为Workbook 
			
			Sheet sheet = wb.getSheetAt(0);	// 取第一个sheet
               
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 0));	// 四个参数分别是：起始行，结束行，起始列，结束列
			
			writeStream(title, wb, getResponse());	// 返回excel
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParsePropertyException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} finally{
			try {
				if(in != null) in.close();
			} catch (IOException e) {
			}
		}
		renderNull();
	}
	
	/**
	 * 导出某一个人的工时
	 * @throws ParseException 
	 */
	public void getOneEmpWorkTime() throws ParseException{
		CommonUtil com=new CommonUtil();
		int year = getParaToInt("year");
		int month = getParaToInt("month");
		String employeeNo = getPara("employeeNo");
		String employeeName = getPara("employeeName");
		String sql="select * from employee where employeeno=?";
		Employee employee=new Employee();
		//查询对应工号的员工
		employee=Employee.dao.find(sql,employeeNo).get(0);
		//获取该员工对应的工时
		List<WorkTime> worktimes=com.findList(employee, year, month);
		// Jfinal获取项目文件路径
		String templateFilePath = JFinal.me().getServletContext().getRealPath("templete") + File.separator + "xtask_export_template.xls";
		// 导出excel的标题
		String title = employeeName+year+"-"+month+"考勤记录";
		Map<String, Object> datamap = new HashMap<String, Object>();
		datamap.put("list", worktimes);	// 导出excel的数据
		datamap.put("title", title);	// 导出excel的标题
		InputStream in = null;
		try {
			in = new FileInputStream(templateFilePath);	// 将模板文件转换为文件流
			
			XLSTransformer transformer = new XLSTransformer();	// jxls生成excel
			
			HSSFWorkbook wb = (HSSFWorkbook) transformer.transformXLS(in, datamap);	// 将excel流转换为Workbook 
			
			Sheet sheet = wb.getSheetAt(0);	// 取第一个sheet
               
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 0));	// 四个参数分别是：起始行，结束行，起始列，结束列
			
			writeStream(title, wb, getResponse());	// 返回excel
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParsePropertyException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} finally{
			try {
				if(in != null) in.close();
			} catch (IOException e) {
			}
		}
		renderNull();
	}
	
	/**
	 * 输出文件流
	 * @param filename
	 * @param wb
	 * @param response
	 */
	public static void writeStream(String filename, Workbook wb, HttpServletResponse response) {
		try {
			filename += ".xls";

			filename.replaceAll("/", "-");
			filename = URLEncoder.encode(filename, "UTF-8");
			
			response.reset();
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
			response.setContentType("application/octet-stream;charset=UTF-8");
			
			OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
			
			wb.write(outputStream);
			
			outputStream.flush();
			outputStream.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
