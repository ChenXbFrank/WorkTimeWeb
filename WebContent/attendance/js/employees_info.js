$(".delete").click(function(){//表格右侧的删除按钮
	var id = $(this).parents("tr").attr("id");//行的id
	$(deleteAlert.alertSelector).attr("data-id", id);
	deleteAlert.alert();
});

var deleteAlert = new ShowAlert({//提示是否要删除的弹出框
	alertSelector:"#delete", //弹出框的选择器
	rightFunc:function(){
		var	id = $(this.alertSelector).attr("data-id"),
			employeeNo = $("#" + id + " [js-name=employeeNo]").html();
		console.log(employeeNo);
		deleteEmployee(id, {employeeNo: employeeNo});
	}
});


$(".modify").click(function(){ //表格右侧的修改按钮
	var id = $(this).parents("tr").attr("id");//行的id
	var param = transferObjPage(id);
	transferObjPage("alert", param);
	$(modifyAlert.alertSelector).attr("data-id", id);
	modifyAlert.alert();
});

$("#modifyInput, #addInput").click(function(){
	if($("#modifyInput")[0].checked){ 
		$(".employeeName").attr("disabled","disabled");
		$(".employeeNo").attr("disabled","disabled");
	}else{
		$(".employeeName").removeAttr("disabled");
		$(".employeeNo").removeAttr("disabled");
	}
});

var modifyAlert = new ShowAlert({ //修改弹出框对象
	alertSelector:"#modify", //弹出框的选择器
	rightFunc:function(){
		var employeeNo = $("#employeeNo").val(),
			id = $(this.alertSelector).attr("data-id"),
			choose = $(this.alertSelector + " input[name=choose]:checked").val(),
			param =  transferObjPage("alert");
		
		switch(choose){
			case "modify":
				modify(id, param);
				break;
			case "add":
				add(id, param);
				break;
		}
	}
});


/********************************ajax*********************************/
function add(id, param){ //增加 
	$.ajax({
		url:"employee/add",
		type:"POST",
		dataType:"json",
		data:param,
		success:function(data){
			if(data.result){
				alertAuto.alert("修改成功");
				var index = parseInt( $("#" + id).children(":first").html() );
				var addQ = $("#" + id).clone(true).insertAfter($("#" + id));
				addQ.nextAll().each(function(){
					var newIndex = parseInt( $(this).children(":first").html() ) + 1;
					$(this).children(":first").html(newIndex);
				});
				addQ.attr("id", data.id).children(":first").html(index+1);
				
			}else{
				alertAuto.alert("修改失败");
			}
		},
		error:function(a,b){
			console.log(a,b,this);
			alertAuto.alert("提交失败");
		}
	});
}

function deleteEmployee(id, data){ //删除
	$.ajax({
		url:"employee/delete",
		type:"POST",
		dataType:"json",
		data:data,
		success:function(result){
			if(result){
				alertAuto.alert("删除成功");
				$("#" + id).remove().nextAll().each(function(){
					var newIndex = parseInt( $(this).children(":first").html() ) - 1;
					$(this).children(":first").html(newIndex);
				});
				
			}else{
				alertAuto.alert("删除失败");
			}
		},
		error:function(a,b){
			console.log(a,b,this);
			alertAuto.alert("提交失败");
		}
	});
}

function modify(id, param){ //修改
	$.ajax({
		url:"employee/edit",
		type:"POST",
		dataType:"json",
		data: param,
		success:function(result){
			if(result){
				alertAuto.alert("修改成功");
				transferObjPage(id, param);
			}else{
				alertAuto.alert("修改失败");
			}
		},
		error:function(a,b){
			console.log(a,b,this);
			alertAuto.alert("提交失败");
		}
	});
}




