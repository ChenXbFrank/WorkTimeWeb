package com.uitrs.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

@SuppressWarnings("serial")
public class BaseRecord <M extends BaseRecord<M>> extends Model<M> implements IBean{
	public java.lang.Integer getId() {
		return get("id");
	}
	public void setId(java.lang.Integer id) {
		set("id", id);
	}
	public java.lang.String getEmployeeNo() {
		return get("employeeno");
	}
	public void setEmployeeNo(java.lang.String employeeno) {
		set("employeeno", employeeno);
	}
	public java.lang.String getEmployeeName() {
		return get("employeename");
	}
	public void setEmployeeName(java.lang.String employeename) {
		set("employeename", employeename);
	}
	//打卡时间
	public java.util.Date getDateTime() {
		return get("datetime");
	}
	public void setDateTime(java.util.Date datetime) {
		set("datetime", datetime);
	}
	
	public java.util.Date getSearchStartTime() {
		return get("searchStartTime");
	}
	public void setSearchStartTime(java.util.Date searchStartTime) {
		set("searchStartTime", searchStartTime);
	}
	
	public java.util.Date getSearchEndTime() {
		return get("searchEndTime");
	}
	public void setSearchEndTime(java.util.Date searchEndTime) {
		set("searchEndTime", searchEndTime);
	}
}
