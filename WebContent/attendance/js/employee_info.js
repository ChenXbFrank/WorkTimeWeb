var deleteAlert = new ShowAlert({//提示是否要删除的弹出框
	alertSelector:"#delete", //弹出框的选择器
	rightFunc:function(){
		alertAuto.alert("点击的是确定按钮");
		var employeeNo = $("#employeeNo").val();
		
		var id = $(this.alertSelector).attr("data-id");
		$.ajax({
			url:"record/delete",
			type:"POST",
			dataType:"json",
			data:{
				employeeNo: employeeNo, //工号
				dateTime: $("#" + id).find(".morning").html()
			},
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
});
var modifyAlert = new ShowAlert({ //修改弹出框对象
	alertSelector:"#modify", //弹出框的选择器
	rightFunc:function(){
		var choose = $(this.alertSelector + " input[name=choose]:checked").val();
		var remarks = $.trim( $(this.alertSelector + " .marks-box>textarea").val() );
		var morning = $(this.alertSelector + " .alert-morning").val();
		var after = $(this.alertSelector + " .alert-after").val();
		var employeeNo = $("#employeeNo").val();
		var id = $(this.alertSelector).attr("data-id");
		if(choose == "modify"){
			$.ajax({
				url:"record/edit",
				type:"POST",
				dataType:"json",
				data:{
					employeeNo: employeeNo,
					remarks: remarks, //备注
					morning: morning, //上上午时间
					after : after //下午时间
				},
				success:function(result){
					if(result){
						alertAuto.alert("修改成功");
						
						$("#" + id).find(".remarks>textarea").val( remarks);
						$("#" + id).find(".morning").html( morning );
						$("#" + id).find(".after").html( after )
						if(remarks){
							$("#" + id + " .remarks>span").removeClass("hidden");
						}else{
							$("#" + id + " .remarks>span").addClass("hidden");
						}
						
					}else{
						alertAuto.alert("修改失败");
					}
				},
				error:function(a,b){
					console.log(a,b,this);
					alertAuto.alert("提交失败");
				}
			});
		}else if(choose == "add"){
			$.ajax({
				url:"record/add",
				type:"POST",
				dataType:"json",
				data:{
					employeeNo: employeeNo,
					remarks: remarks, //备注
					morning: morning, //上上午时间
					after : after //下午时间
				},
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
		
	}
});


$(".addMark").click(function(){
	$(".add").toggle();
});

//导出excel
$("#export").click(function(){
	$("#exportData").submit();
});

var remarksId;

$(".remarks:not(.disabled)").dblclick(function(event){ //双击表格的备注行时，可以修改备注
	remarksId = $(this).parents("tr").attr("id");
	$(".remarks>textarea").hide();
	$(this).children("textarea").show();
	$(this).children("span").addClass("hidden");
	event.stopPropagation();
	event.cancelBubble = true;
});

$(".remarks:not(.disabled)").click(function(event){
	event.stopPropagation();
	event.cancelBubble = true;
});

$(".remarks:not(.disabled)>textarea").change(function(){
	var remarks = $("#"+remarksId + " .remarks>textarea").val();
	remarks = $.trim(remarks);
	if(!remarks){ return; }
	//console.log(remarks);
	$.ajax({
		url:"worktime/editRecord",
		type:"get",
		dataType:"json",
		data:{
			remarksId: remarksId,
			remarks: remarks
		},
		success:function(data){
			if(data){
				alertAuto.alert("修改成功");
				$("#" + remarksId + " .remarks>span").removeClass("hidden");
			}else{
				alertAuto.alert("修改失败");
			}
		},
		error:function(a,b){
			console.log(a,b,this);
			alertAuto.alert("提交失败");
		}
	});
});

$(document).click(function(){
	var remarks = $("#" + remarksId + " .remarks>textarea").val();
	remarks = $.trim(remarks);
	if(remarks){
		$("#" + remarksId + " .remarks>span").removeClass("hidden");
	}
	$(".remarks>textarea").hide();
});

$(".modify").click(function(){ //表格右侧的修改按钮
	var id = $(this).parents("tr").attr("id");//行的id
	var remarks = $("#" + id).find(".remarks>textarea").val(); //获取备注
	var morning = $("#" + id).find(".morning").html(); //获取早上的时间
	var after = $("#" + id).find(".after").html(); //获取下的时间
	
	$(modifyAlert.alertSelector + " .alert-morning").val(morning);
	$(modifyAlert.alertSelector + " .alert-after").val(after);
	$(modifyAlert.alertSelector + " .marks-box>textarea").val(remarks);
	$(modifyAlert.alertSelector).attr("data-id", id);

	modifyAlert.alert();
});

$(".delete").click(function(){
	var id = $(this).parents("tr").attr("id");//行的id
	$(deleteAlert.alertSelector).attr("data-id", id);
	deleteAlert.alert();
});







