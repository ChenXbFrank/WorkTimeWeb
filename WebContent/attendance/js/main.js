//addClick({
//	"tr": "employee_info.html",
//})
$("#submit").click(function(){
	findWorkTime();
});
//导出excel
$("#export").click(function(){
	window.location.href="/WorkTime/record/getExcel";
});

function findWorkTime(){ //创建工时的ajax请求
	var year = $("#year").val(), month = ""; 
	month = $("#month").val();
	var patternYear = /^\d{4}$/g;
	var  employeeNo = $("#employeeNo").val();
	if(employeeNo.length==0){
		alert("工号不能为空");
		return;
	}
	if(patternYear.exec(year) == null){
		alert("年份必须是4位数");
		return;
	}
	if(month.length !=2 && month.length !=1){
		alert("月份必须是1位或2位数");
		return;
	}
	location="worktime/find?employeeNo="+employeeNo+"&year="+year+"&month="+month;
}

$(".query").click(function(){
	var year = $("#year").val(), month = $("#month").val();
	if(!year) { alert("必须输入年份"); return; }
	if(!month) {alert("必须输入月份"); return; }
	
	var href = $(this).attr("data-href") + "&year=" + year + "&month=" + month+"&employeeNo="+employeeNo;
	window.location.href = href;
});











