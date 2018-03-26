package com.uitrs.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.uitrs.model.Employee;
import com.uitrs.model.Record;
import com.uitrs.model.User;
import com.uitrs.model.WorkTime;

public class CommonUtil {

	/**
	 * 查询所有的员工
	 */
	public List<Employee> findAllEmployee(){
		String sql="select * from employee";
		return Employee.dao.find(sql);
	}
	
	/**
	 * 根据开始时间和结束时间查询所有的打卡记录
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<Record> findRecord(Date startTime, Date endTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sql="select * from record where datetime BETWEEN '"+sdf.format(startTime)+"' and '"+sdf.format(endTime)+"' ";
        return Record.dao.find(sql);
    }
	
	 /**  调试通过
     * 根据具体的时间获取对应员工当天所有的打卡记录
     * @param employee
     * @param year
     * @param month
     * @param day
     * @return
     */
    public List<Record> findRecord(Employee employee, int year, int month, int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date startTime = null;
        Date endTime = null;
        try {
            startTime = sdf.parse(year + "-" + month + "-" + day + " 00:00");
            endTime = sdf.parse(year + "-" + month + "-" + day + " 23:59");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //查询对应的员工对应的记录
        String employeeno=employee.getEmployeeNo();
        String starttime=sdf.format(startTime);
        String endtime=sdf.format(endTime);
        String sql="select * from record where employeeno= "+employeeno+" and datetime BETWEEN '"+starttime+"' and '"+endtime+"' ";
        List<Record> list= Record.dao.find(sql);
        for(Record red:list){
        	System.out.println(red.toString());
        }
        return list;
    }
    
    /**
     *    测试通过
     * 根据具体的时间 获取对应员工每天最早的一条打卡记录
     * @param employee
     * @param year
     * @param month
     * @param day
     * @return
     */
    public Record getFirstRecord(Employee employee, int year, int month, int day) {
        List<Record> records = findRecord(employee, year, month, day);
        Record firstRecord = null;
        /**
         * 从当天打卡记录里获取最早的打卡记录
         */
        if (records != null && records.size() > 0) {
            firstRecord = records.get(0);
            for (Record  record  :  records){
                if(firstRecord.getDateTime().compareTo(record.getDateTime())  >  0){
                    firstRecord  =  record;
                }
            }
        }
        return firstRecord;
    }
    
    /**
     *  测试通过
     * 根据具体的时间 获取对应员工每天最晚的一条打卡记录
     * @param employee
     * @param year
     * @param month
     * @param day
     * @return
     */
    public Record getLastRecord(Employee employee, int year, int month, int day) {
        List<Record> records = findRecord(employee, year, month, day);
        Record lastRecord = null;
        /**
         * 从当天打卡记录里获取最晚的打卡记录
         */
        if (records != null && records.size() > 0) {
            lastRecord = records.get(0);
            for (Record  record  :  records){
                if(lastRecord.getDateTime().compareTo(record.getDateTime())  <  0){
                    lastRecord  =  record;
                }
            }
        }
        return lastRecord;
    }
    
    /**
     *  测试通过
     * 导入打卡记录的原生记录
     * @param txtFilePath  txt 文件路径
     * @throws ParseException 
     */
    public void importRecord(String txtFilePath){
    	//导入了 数据库
        List<Record>  records  = FileUtil.convertLogToRecords(txtFilePath);
        //如果记录表里有记录之后 就不用添加了 
		for (Record  record  : records){
	    	Record red=new Record();
	    	Employee employee=new Employee();
	    	String employeeNo=record.getEmployeeNo();
	    	String employeeName=record.getEmployeeName();
	    	employee.setEmployeeNo(employeeNo);
	    	employee.setEmployeeName(employeeName);
	    	//先查找该员工是否存在 如果没有则添加
	    	String sql1="select * from employee where employeeno = ? ";
	    	List<Employee> empList=Employee.dao.find(sql1,employeeNo);
	    	if(empList.size()==0){
	    		employee.save();
	    	}
	        red.setEmployeeNo(employeeNo);
	        red.setEmployeeName(employeeName);
	        red.setDateTime(record.getDateTime());
	        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        String time=sdf.format(record.getDateTime());
	        String sql="select * from record where employeeno="+employeeNo+" and datetime='"+time+"'";
	        List<Record> list=Record.dao.find(sql);
	        if(list.size()==0){
	        	red.save();
	        }
	        //创建用户
	        String sql2="select * from user where username = '"+employeeName+"' ";
	        List<User> list1=User.dao.find(sql2);
	        if(list1.size()==0){
	        	User user=new User();
	        	user.set("username",employeeName)
	        	    .set("password","123456") 
	        	    .set("grade","0")
	        	    .save();
	        }
        }
    }
    
    /**
     *   测试通过
     * 根据年月创建对应的工时
     * @param year
     * @param month
     */
    public void createWorkTimes(int year, int month) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(year + "-" + month + "-" + 1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //查询出所有的员工
        List<Employee> employees = findAllEmployee();
        
        for (int day = 1; day <= DateUtil.calculateDays(date); day++) {
            for (Employee employee : employees) {
                WorkTime workTime = new WorkTime();
                workTime.setEmployeeNo(employee.getEmployeeNo());
                workTime.setEmployeeName(employee.getEmployeeName());
                Date currentDay = null;
                try {
                    currentDay = sdf.parse(year + "-" + month + "-" + day);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                workTime.setDate(currentDay);
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                //  最早打卡记录
                Record firstRecord = getFirstRecord(employee, year, month, day);
                //  最晚打卡记录
                Record lastRecord = getLastRecord(employee, year, month, day);
                if (firstRecord != null && lastRecord != null) {
                	//设置最早的打卡记录
            	    Date firstDate=firstRecord.getDateTime();
                    String fisrtdate=sdf1.format(firstDate);
                    workTime.setFirstRecord(fisrtdate);
                  //设置最晚的打卡记录
                    Date lastDate=lastRecord.getDateTime();
                    String lastdate=sdf1.format(lastDate);
                    workTime.setLastRecord(lastdate);
                    //设置最早的打卡记录的id
                    workTime.setFirstrecordid(firstRecord.getId());
                    //设置最晚的打卡记录的id
                    workTime.setLastrecordid(lastRecord.getId());
                    //计算早上的工时
                    workTime.setMorningworktime(WorkTimeUtil.calculateMorningTime(firstRecord.getDateTime(), lastRecord.getDateTime()));
                    //计算下午的工时
                    workTime.setAfternoonworktime(WorkTimeUtil.calculateAfternoonTime(firstRecord.getDateTime(), lastRecord.getDateTime()));
                    //计算晚上加班的工时
                    workTime.setNightworktime(WorkTimeUtil.calculateNightTime(firstRecord.getDateTime(), lastRecord.getDateTime()));
                    //先获取该员工的对应日期的工时 如果没有才添加
                    List<WorkTime> worktimeList = find(employee, year, month, day);
                    if(worktimeList.size()>0){
                    	//如果数据库有了 就更新
                    	for(WorkTime w:worktimeList){
                    		 workTime.set("id", w.getId()).update();
                    	}
                    }
                   else {
                    	//没有就添加
                        workTime.save();
                    }
                }
                
            }
        }
    }
    
    /**
     *   测试通过
     * 获取对应员工某天的所有工时
     * @param employee
     * @param year
     * @param month
     * @param day
     * @return
     */
    public List<WorkTime> find(Employee employee, int year, int month, int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(year + "-" + month + "-" + day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        WorkTime worktime = new WorkTime();
        worktime.setEmployeeNo(employee.getEmployeeNo());
        worktime.setDate(date);
        String sql="select * from worktime where employeeno=" +employee.getEmployeeNo() +" and date='"+sdf.format(date)+"'";
        List<WorkTime> list=WorkTime.dao.find(sql);
        return list;
    }
    
    /**
     * 获取某个员工具体的年月的所有的工时
     * @param employee
     * @param year
     * @param month
     * @return
     * @throws ParseException 
     */
    public List<WorkTime> findList(Employee employee, int year, int month) throws ParseException {
        WorkTime p = new WorkTime();
        p.setEmployeeNo(employee.getEmployeeNo());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String employeeNo = employee.getEmployeeNo();
        //设置查询的开始时间
        Date start = sdf.parse(year + "-" + month + "-" + 1);
        String startStr = sdf.format(start);
        //获取该月的天数
        int day =  DateUtil.calculateDays(start);
        //设置查询的结束时间
        Date end = sdf.parse(year + "-" + month + "-" + day);
        String endStr = sdf.format(end);
        //查询该员工对应时间段的工时
        String sql="select * from worktime where employeeno= "+employeeNo+" and date BETWEEN '"+startStr+"' and  '"+endStr+"' ";
        List<WorkTime> list=WorkTime.dao.find(sql);
        return list;
    }
}
