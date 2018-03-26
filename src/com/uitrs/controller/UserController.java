package com.uitrs.controller;

import java.util.List;

import com.jfinal.core.Controller;
import com.uitrs.model.User;

/**
 * 用户的controller
 * @author ChenXb
 *
 */
public class UserController extends Controller{
	
	public void index(){
		render("/attendance/pages/register.html");
	}
	
	public void toLogin(){
		render("/attendance/pages/login.html");
	}
	
	public void logout(){
		getSession().removeAttribute("user");
		getSession().invalidate();
		getResponse().setHeader("Cache-Control","no-cache");
		getResponse().setHeader("Cache-Control","no-store");
		getResponse().setDateHeader("Expires", 0);
		getResponse().setHeader("Pragma","no-cache");
		User user=(User) getSession().getAttribute("user");
		if(user==null){
			redirect("/user/toLogin");
		}
	}
	
	/**
	 * 用户注册
	 */
	public void register(){
		String username=getPara("username");
		String password=getPara("password");
		User user=new User();
		user.set("username", username);
		user.set("password", password);
		//注册的都默认是普通的用户
		user.set("grade", 0);
		//先查询该用户是否存在了
		String sql="select * from user where username=?";
		List<User> list =User.dao.find(sql,username);
		if(list.size()==0){
			//将数据保存到数据库
			user.save();
		}
		if(list.size()>0){
			//可以提示用户该用户已经注册
		}
		render("/attendance/pages/login.html");
	}
	/**
	 * 用户登录
	 */
	public void login(){
		String username=getPara("username");
		String password=getPara("password");
		String sql="select * from user where username= ? ";
		List<User> list=User.dao.find(sql,username);
		User user=list.get(0);
		String pass=user.getPassWord();
		//普通用户
		if(pass.equals(password)){
			getSession().setAttribute("user", user);
			//跳转到主页
			redirect("/record");
		}
	}
}
