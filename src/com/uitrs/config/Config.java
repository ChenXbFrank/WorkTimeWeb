package com.uitrs.config;

import java.io.File;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.BslPlugin;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.uitrs.controller.EmployeeController;
import com.uitrs.controller.RecordController;
import com.uitrs.controller.UserController;
import com.uitrs.controller.WorkTimeController;
import com.uitrs.inteceptor.MyInterceptor;
import com.uitrs.model.Employee;
import com.uitrs.model.Record;
import com.uitrs.model.User;
import com.uitrs.model.WorkTime;

/**
 * 这就是自己定义的配置文件类
 * @author Administrator
 *
 */
public class Config extends JFinalConfig {
	/**
	 * 
	 */
	@Override
	public void configConstant(Constants me) {
		//加载数据库配置信息
		loadPropertyFile("jdbc.properties");
		//设置开发模式
		me.setDevMode(getPropertyToBoolean("devMode",true));
		//设置视图格式  默认的
		me.setViewType(ViewType.FREE_MARKER);
		
		me.setBaseUploadPath("D:" +File.separator+"MyUpLoad"+File.separator);
	}

	/**
	 * 设置上下文路径
	 */
	@Override
	public void configHandler(Handlers me) {
		me.add(new ContextPathHandler("contextPath"));
	}

	/**
	 * 配置拦截器
	 */
	@Override
	public void configInterceptor(Interceptors me) {
		me.add(new MyInterceptor());
	}
	
	/**
	 * 配置插件（第三方的）
	 */
	@Override
	public void configPlugin(Plugins me) {
		//添加数据源
		String password=getProperty("jdbc.password");
		String username=getProperty("jdbc.username");
		String url=getProperty("jdbc.url");
		//连接池    没有使用c3p0
		DruidPlugin druid=new DruidPlugin(url, username, password);
		me.add(druid);
		//接入activeRecord插件  必须有数据源的支持
		ActiveRecordPlugin arp=new ActiveRecordPlugin(druid);
		me.add(arp);
		//映射数据库   默认是id为主键
		
		arp.addMapping("employee","id", Employee.class);
		arp.addMapping("record", "id", Record.class);
		arp.addMapping("worktime", "id", WorkTime.class);
		arp.addMapping("user", "id", User.class);
		me.add(new BslPlugin()); //
	}

	/**
	 * 配置路由    添加controller
	 */
	@Override
	public void configRoute(Routes me) {
		me.add("/employee",EmployeeController.class);
		//习惯问题  加上页面的前缀文件夹名
		me.add("/record",RecordController.class);
		me.add("/worktime",WorkTimeController.class);
		me.add("/user",UserController.class);
	}

}
