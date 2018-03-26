package com.uitrs.controller;
import java.util.List;

import com.jfinal.core.Controller;
import com.uitrs.model.Employee;
import com.uitrs.model.User;

/**]
 * 员工的controller
 * @author ChenXb
 *
 */
public class EmployeeController extends Controller{
	/**
	 * 员工的主页
	 */
	public void index(){
		String sql="select * from employee";
		List<Employee> list=Employee.dao.find(sql);
		setAttr("list", list);
		//权限问题
		User user=(User) getSession().getAttribute("user");
		String grade1=user.getGrade();
		int grade=Integer.parseInt(grade1);
		setAttr("grade", grade);
		render("/attendance/pages/employees_info.html");
	}
    
	/**
	 * 员工修改资料
	 */
	public void edit(){
		String employeeName=getPara("employeeName");
		String employeeNo=getPara("employeeNo");
		String sex=getPara("sex");
		String phone=getPara("phone");
		String email=getPara("email");
		String university=getPara("university");
		String institute=getPara("institute");
		String birthday=getPara("birthday");
		String address=getPara("address");
		String sql="select * from employee where employeeno="+employeeNo+"";
		List<Employee> list=Employee.dao.find(sql);
		Employee employee= list.get(0);
		boolean result=employee.set("id", employee.getId())
				.set("employeeno", employeeNo)
				.set("employeename", employeeName)
				.set("phone", phone)
				.set("sex", sex)
				.set("email", email)
				.set("university", university)
				.set("institute", institute)
				.set("birthday", birthday)
				.set("address", address)
				.update();
		//修改资料成功
		renderJson(result);
	}
	
	/**
	 * 删除员工
	 */
	public void delete(){
		String employeeNo=getPara("employeeNo");
		//根据工号删除
		/*String sql1="delete from employee where employeeno= ?";
		Db.update(sql1,employeeNo);*/
		//先查找出该员工 再删除
		String sql="select * from employee where employeeno="+employeeNo+"";
		List<Employee> list=Employee.dao.find(sql);
		Employee employee= new Employee();
		//根据id删除
		boolean result=employee.set("id", list.get(0).getId()).delete();
		//删除成功
		renderJson(result);
	}
	
	/**
	 * 添加员工
	 */
	public void add(){
		String employeeName=getPara("employeeName");
		String employeeNo=getPara("employeeNo");
		String sex=getPara("sex");
		String phone=getPara("phone");
		String email=getPara("email");
		String university=getPara("university");
		String institute=getPara("institute");
		String birthday=getPara("birthday");
		String address=getPara("address");
		Employee employee=new Employee();
		boolean result=employee.set("employeeno", employeeNo)
		        .set("employeename", employeeName)
		        .set("phone", phone)
		        .set("sex", sex)
		        .set("email", email)
		        .set("university", university)
		        .set("institute", institute)
		        .set("birthday", birthday)
		        .set("address", address)
		        .save();
		renderJson(result);
	}
}
