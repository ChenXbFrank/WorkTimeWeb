package com.uitrs.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.jfinal.core.Controller;
import com.uitrs.model.Employee;
import com.uitrs.model.User;
import com.uitrs.model.WorkTime;
import com.uitrs.util.CommonUtil;
import com.uitrs.util.DateUtil;

/**
 * 工时的controller
 * @author ChenXb
 *
 */
public class WorkTimeController extends Controller{

	/** 第2步
	 * 创建工时  根据年月 创建
	 */
	public void create(){
		int year=getParaToInt("year");
		int month=getParaToInt("month");
		CommonUtil com=new CommonUtil();
		//创建工时
		com.createWorkTimes(year, month);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startdate = null;
		String startDate=null;
        try {
        	startdate = sdf.parse(year + "-" + month + "-" + 1);
        	startDate=sdf.format(startdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //计算该月的天数
        int day=DateUtil.calculateDays(startdate);
        Date enddate = null;
        String endDate=null;
        try {
        	enddate = sdf.parse(year + "-" + month + "-" + day);
        	endDate=sdf.format(enddate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //从数据库获取该月的工时 如果有数据就返回true
		String sql="select * from worktime where date between '"+startDate+"' and '"+endDate+"'";
		List<WorkTime> list=WorkTime.dao.find(sql);
		if(list.size()>0){
			renderJson(true);
		}else{
			renderJson(false);
		}
	}
	/**  员工编号 年月
	 * 查询对应员工的工时
	 * @throws ParseException 
	 */
	public void find() throws ParseException{
		CommonUtil com=new CommonUtil();
		String employNo=getPara("employeeNo");
		int year=getParaToInt("year");
		int month=getParaToInt("month");
		Employee employee=new Employee();
		String sql="select * from employee where employeeno=?";
		//查询对应工号的员工
		employee=Employee.dao.find(sql,employNo).get(0);
		String employeeName=employee.getEmployeeName();
		//获取该员工对应的工时
		List<WorkTime> worktimes=com.findList(employee, year, month);
		int total=0;
		for(WorkTime w:worktimes){
			System.out.println(w.toString());
			int morn=w.getMorningworktime();
			int after=w.getAfternoonworktime();
			int night=w.getNightworktime();
			total+=morn+after+night;
		}
		double totalTime=total/60;
		double a=total%60;
		double b=a/60;
		BigDecimal bd = new BigDecimal(b);  
		double f1 = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		double totime=totalTime+f1;
		System.out.println("--------------------------------------------a>"+a);
		System.out.println("--------------------------------------------b>"+b);
		System.out.println("--------------------------------------------totalTime>"+totalTime);
		System.out.println("--------------------------------------------totime>"+totime);
		
		//获取session中保存的用户
		User user=(User) getSession().getAttribute("user");
		//获取用户的级别  1是管理员 0是普通用户
		String grade1=user.getGrade();
		int grade=Integer.parseInt(grade1);
		setAttr("grade", grade);
		setAttr("worktimes", worktimes);
		setAttr("year", year);
		setAttr("month", month);
		setAttr("totalTime",totime);
		setAttr("employeeNo", employNo);
		setAttr("employeeName", employeeName);
		render("/attendance/pages/employee_info.html");
	}
	
	/**
	 * 添加备注和记录
	 */
	public void editRecord(){
		int remarksId=getParaToInt("remarksId");
		String remaks=getPara("remarks"); 
		String sql="select * from worktime where id= ? ";
		List<WorkTime> list=WorkTime.dao.find(sql,remarksId+"");
		WorkTime worktime=list.get(0);
		worktime.setRemarks(remaks);
		worktime.setId(remarksId);
		boolean result = worktime.update();
		renderJson(result);
	}
}
