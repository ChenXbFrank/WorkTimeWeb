package com.uitrs.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

@SuppressWarnings("serial")
public abstract class BaseEmployee<M extends BaseEmployee<M>> extends Model<M> implements IBean{
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
	
	public java.lang.String getPhone() {
		return get("phone");
	}
	public void setPhone(java.lang.String phone) {
		set("phone", phone);
	}
	public java.lang.String getSex() {
		return get("sex");
	}
	public void setSex(java.lang.String sex) {
		set("sex", sex);
	}
	public java.lang.String getEmail() {
		return get("email");
	}
	public void setEmail(java.lang.String email) {
		set("email", email);
	}
	public java.lang.String getUniversity() {
		return get("university");
	}
	public void setUniversity(java.lang.String university) {
		set("university", university);
	}
	//专业
	public java.lang.String getInstitute() {
		return get("institute");
	}
	public void setInstitute(java.lang.String institute) {
		set("institute", institute);
	}
	public java.lang.String getBirthday() {
		return get("birthday");
	}
	public void setBirthday(java.lang.String birthday) {
		set("birthday", birthday);
	}
	
	public java.lang.String getAddress() {
		return get("address");
	}
	public void setAddress(java.lang.String address) {
		set("address", address);
	}
}
