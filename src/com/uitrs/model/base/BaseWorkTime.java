package com.uitrs.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

@SuppressWarnings("serial")
public class BaseWorkTime <M extends BaseWorkTime<M>> extends Model<M> implements IBean{
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
	public java.util.Date getDate() {
		return get("date");
	}
	public void setDate(java.util.Date date) {
		set("date", date);
	}
	
	public java.lang.Integer getMorningworktime() {
		return get("morningworktime");
	}
	public void setMorningworktime(java.lang.Integer morningworktime) {
		set("morningworktime", morningworktime);
	}
	
	public java.lang.Integer getAfternoonworktime() {
		return get("afternoonworktime");
	}
	public void setAfternoonworktime(java.lang.Integer afternoonworktime) {
		set("afternoonworktime", afternoonworktime);
	}
	
	public java.lang.Integer getNightworktime() {
		return get("nightworktime");
	}
	public void setNightworktime(java.lang.Integer nightworktime) {
		set("nightworktime", nightworktime);
	}
	
	public java.lang.Integer getFirstrecordid() {
		return get("firstrecordid");
	}
	public void setFirstrecordid(java.lang.Integer firstrecordid) {
		set("firstrecordid", firstrecordid);
	}
	
	public java.lang.Integer getLastrecordid() {
		return get("lastrecordid");
	}
	public void setLastrecordid(java.lang.Integer lastrecordid) {
		set("lastrecordid", lastrecordid);
	}
	
	//最早的打卡记录
	public java.lang.String getFirstRecord() {
		return get("firstrecord");
	}
	public void setFirstRecord(java.lang.String firstrecord) {
		set("firstrecord", firstrecord);
	}
	
	//最晚的打卡记录
	public java.lang.String getLastRecord() {
		return get("lastrecord");
	}
	public void setLastRecord(java.lang.String lastrecord) {
		set("lastrecord", lastrecord);
	}
	
	//最晚的打卡记录
	public java.lang.String getRemarks() {
		return get("remarks");
	}
	public void setRemarks(java.lang.String remarks) {
		set("remarks", remarks);
	}
}
