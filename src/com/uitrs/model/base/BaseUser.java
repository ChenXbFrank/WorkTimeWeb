package com.uitrs.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

@SuppressWarnings("serial")
public abstract class BaseUser<M extends BaseUser<M>> extends Model<M> implements IBean {
	public java.lang.Integer getId() {
		return get("id");
	}
	public void setId(java.lang.Integer id) {
		set("id", id);
	}
	public java.lang.String getUserName() {
		return get("username");
	}
	public void setUserName(java.lang.String username) {
		set("username", username);
	}
	public java.lang.String getPassWord() {
		return get("password");
	}
	public void setPassWord(java.lang.String password) {
		set("password", password);
	}
	//级别
	public java.lang.String getGrade() {
		return get("grade");
	}
	public void setGrade(java.lang.String grade) {
		set("grade", grade);
	}
}
