package com.uitrs.model;

import java.util.Date;

import com.uitrs.model.base.BaseWorkTime;

@SuppressWarnings("serial")
public class WorkTime extends BaseWorkTime<WorkTime>{
	public static final WorkTime dao = new WorkTime();
	 private  Date  searchStartDate;
	 private  Date  searchEndDate;
	 public Date getSearchStartDate() {
	        return searchStartDate;
	    }
	 public void setSearchStartDate(Date searchStartDate) {
	        this.searchStartDate = searchStartDate;
	    }
	public Date getSearchEndDate() {
		return searchEndDate;
	}
	public void setSearchEndDate(Date searchEndDate) {
		this.searchEndDate = searchEndDate;
	}
}
