//添加跳转
function addClick(options){
	options.click = options.click ? options.click : "click";
	for(var selector in options){
		$(selector)[options.click](function(){
			window.location.href = this;
		}.bind(options[selector]));
	}
}
addClick({
	".employees": "employee",
	".exit": "user/logout",
	".set": "system_set.html",
	".home": "record",
	".statistics": "employees_statistics.html",
});


/*
{	objName: js-obj, //默认值
	objKey: "table",
	keyName: js-name, //默认值
	data:null,
}
*/
function transferObjPage(options, dataParam){
	var objName, keyName, keyName, data;
	if(typeof options == "object"){
		objName = options.objName || "js-obj";
		keyName = options.keyName || "js-name";
		keyName = options.objKey;
		data = options.data;
	}else if(typeof options == "string"){
		objName = "js-obj";
		keyName = "js-name";
		objKey = options;
		data = dataParam;
	}
	
	var parentSelector = "[" + objName + "=" + objKey +   "]";
	var children = " [" + keyName + "]";
	var obj = {}, key = null;
	$(parentSelector + children).each(function(index, elem){
		key = $(this).attr(keyName);
		if(data != undefined){
			if(data[key] !== undefined){
				if( this.nodeName == "INPUT" || this.nodeName == "TEXTAREA" ){
					 $(this).val( data[key] );
				}else{
					$(this).html( data[key] );
				}
			}
		}else{
			if( this.nodeName == "INPUT" || this.nodeName == "TEXTAREA" ){
				obj[key] = $(this).val();
			}else{
				obj[key] = $(this).html();
			}
		}
	});
	return obj;
}

/*start************ 初始化对象 **********************/

function Init(optionsAll){ //初始化对象类
	this.initObj = optionsAll.initObj; //需要初始化的对象
	
	this.innerAttrs = optionsAll.innerAttrs; //这是initObj内部使用的属性
	this.innerAttrs && this.setAll(this.innerAttrs); //进行初始化
	
	this.config = optionsAll.config; //这是initObj可以从外部传入参数进行配置的属性
	this.config && this.setAll(this.config); //进行初始化
}

Init.prototype.init = function(options){ //初始化配置(含有默认值)
	if(!options){ return; }
	for(var prop in this.config){
		if(options[prop] == undefined){
			if( /mustHave/.test( this.config[prop] ) ){ //这样的值是必须进行外部传入参数初始化的
				throw Error(prop + ": 必须初始化。");
			}
		}else{
			if(/mustHaveBind/.test( this.config[prop] )){ //如果config的值是这样，就进行事件绑定this
				this.initObj[prop] = options[prop].bind(this.initObj);
			}else{
				this.initObj[prop] = options[prop];
			}
		}
	}
}

Init.prototype.set = function(options){ //根据 options 更改 initObj的属性值，不可以更改内部属性
	if(!options){ return; }
	for(var prop in options){
		if(prop in this.config){ //如果是可配置的属性，就进行配置
			this.initObj[prop] = options[prop];
		}
	}
}

Init.prototype.setAll = function(options){ //根据 options 更改 initObj的属性值，可以更改所有属性
	if(!options){ return; }
	for(var prop in options){
		this.initObj[prop] = options[prop];
	}
}

Init.prototype.format = function(options, def){ //根据 options 更改 initObj的属性值，可以更改所有属性
	if(!options){ return def; }
	for(var prop in def){
		options[prop] = def[prop];
	}
	return options;
}
Init.format = function(options, def){ //根据 options 更改 initObj的属性值，可以更改所有属性
	if(!options){ return def; }
	for(var prop in def){
		options[prop] = def[prop];
	}
	return options;
}
/*end************ 初始化对象 **********************/

/*start********************* 弹出框 **************************/

function ShowAlert(config){//显示弹出框
	this.init(config);
}

ShowAlert.prototype.init = function(config){	
	this.initObject = new Init({
		initObj:this,
		innerAttrs:{
			data:{},
		},
		config:{
			alertSelector:".x-alert-box",
			alertBodySelector:".alert-body",
			maskSelector:".x-mask",
			messageSelector:">h3",
			leftBtnSelector:".cancel",
			rightBtnSelector:".sure",
			leftBtnVal:"取消",
			rightBtnVal:"确定",
			message:"",
			transitionTime: 200, //淡入淡出时间
			beforeFunc:function(){},//弹出之前调用
			leftFunc:function(){}, //默认的函数是直接隐藏弹出框
			rightFunc:function(){}, //默认的函数是直接隐藏弹出框
		}
	});
	config && this.initObject.init(config); //用外部参数进行配置
	this.leftF = this.leftF.bind(this);//默认的函数是直接隐藏弹出框
	this.rightF = this.rightF.bind(this); //默认的函数是直接隐藏弹出框
	this.createAlert();
	if($(this.alertSelector).find(this.alertBodySelector).length != 0){//如果有
		$(this.alertSelector).find(this.messageSelector).addClass("x-clear-MP");
	}
	$(this.maskSelector).click(function(){
		this.hideAlert();
	}.bind(this));
};

ShowAlert.prototype.set = function(config){
	config && this.initObject.init(config); //用外部参数进行配置
}

ShowAlert.prototype.setData = function(name, value){ //存储数据 
	this.data[name] = value; 
}

ShowAlert.prototype.getData = function(name){ //取出数据
	return this.data[name];
}

ShowAlert.prototype.hideAlert = function (){//"弹框"中的确定 按钮
	$(this.maskSelector).fadeOut(this.transitionTime)
	$(this.alertSelector).fadeOut(this.transitionTime);
}

ShowAlert.prototype.alert = function(options, leftBtnVal, rightBtnVal){//显示弹出框
	var message, leftBtn, rightBtn;
	if(!options){
		message = this.message;
		leftBtn = leftBtnVal || this.leftBtnVal;
		rightBtn = rightBtnVal || this.rightBtnVal;
	}else if( typeof options !== "object"){
		message = options;
		leftBtn = leftBtnVal || this.leftBtnVal;
		rightBtn = rightBtnVal || this.rightBtnVal;
	}else if( typeof options === "object" ){
		message = options.message;
		leftBtn = options.leftBtnVal || this.leftBtnVal;
		rightBtn = options.rightBtnVal || this.rightBtnVal;
	}
	var result = this.beforeFunc();
	if( result === false ){ return; }
	$(this.maskSelector).fadeIn(this.transitionTime);
	var alertBox = $(this.alertSelector);
	if($.trim(message) !== ""){
		alertBox.find(this.messageSelector).html(message);
	}
	
	$(this.alertSelector + " " + this.leftBtnSelector).click(this.leftF);
	$(this.alertSelector + " " + this.rightBtnSelector).click(this.rightF);
	
	var height = parseInt(alertBox.css("height"));
	alertBox.css("margin-top",-height/2);
	alertBox.find(this.leftBtnSelector).html(leftBtn);
	alertBox.find(this.rightBtnSelector).html(rightBtn);

	$(this.alertSelector).fadeIn(this.transitionTime);
}
ShowAlert.prototype.leftF = function (){//"弹框"中左侧按钮
	if( !this.leftFunc() ){ //返回值为false时，才隐藏
		this.hideAlert();
	}
	$(this.alertSelector + " " + this.rightBtnSelector).unbind("click",this.rightF);
	$(this.alertSelector + " " + this.leftBtnSelector).unbind("click",this.leftF);
	
}
ShowAlert.prototype.rightF = function (){//"弹框"中右侧按钮
	if( !this.rightFunc() ){ //返回值为false时，才隐藏
		this.hideAlert();
	}
	$(this.alertSelector + " " + this.rightBtnSelector).unbind("click",this.rightF);
	$(this.alertSelector + " " + this.leftBtnSelector).unbind("click",this.leftF);
}

ShowAlert.prototype.createAlert = function(){
	if($(".x-mask").length || $(".x-alert-box").length){
		return;
	}
	var html = '<div class="x-mask"></div>'
					+'<div class="x-alert x-alert-box">'
						+'<h3></h3>'
						+'<div class="btn-box">'
							+'<a class="cancel" ></a>'
							+'<a class="sure" ></a>'
						+'</div>'
					+'</div>';
	$("body").append(html);									
}

/*end********************* 弹出框 **************************/

/*start************ 信息提示框 **********************/

function ShowAlertAuto(config){
	this.init(config);
}

ShowAlertAuto.prototype.init = function(config){	
	this.initObject = new Init({
		initObj:this,
		innerAttrs:{
			wWidth:$(window).width()
		},
		config:{
			showTime:2000, //弹出后的显示时间，默认2s
			fadeTime:200, //淡入和淡出时间
			lrDis:10,//弹出框距离窗体左右边距的最小值
			selector:".x-alert-message" //弹出框的选择器
		}
	});
	config && this.initObject.init(config); //用外部参数进行配置
};

ShowAlertAuto.prototype.set = function(options){ //更改属性
	this.initObject.set(options);
}

ShowAlertAuto.prototype.alert = function(message, showTime){ //显示信息提示框[message,seconds]
	var showT = showTime == undefined ? this.showTime : showTime;
	var selector = this.selector, self = this;
    this.create(message);
   	$(selector).css("margin-left", - this.wWidth); //注意这个必须有，否则会不对
    setTimeout(function(){
        self.hide();               
    },showT);
 
    $(selector)[0].style.width = "auto";
    var alertWidth = parseInt($(selector).width()); //弹出框的宽度 
    var paddingLeft = parseInt($(selector).css("padding-left"));
    var paddingTop = parseInt($(selector).css("padding-top"));
    if(alertWidth - 2*paddingLeft - this.lrDis > this.wWidth){ //如果弹出框太宽，就将弹出框的宽度设小一点，保证完全在窗口中显示
    	alertWidth = this.wWidth - 2*paddingLeft - 2*this.lrDis; 
    	$(selector).css("width",alertWidth);
    }
    var alertHeight = parseInt($(selector).height()); //弹出框的高度 (if语句可能会改变高度，所以要在这个位置获取)
    $(selector).css("margin-left",-alertWidth/2- paddingLeft ).css("margin-top",-alertHeight/2-paddingTop);
    this.show();
};

ShowAlertAuto.prototype.create = function(message){
    if(!$(this.selector)[0]){
        message = message || "";
       $('<div class="' + this.selector.replace(".","") + '">' + message + '</div>').appendTo("body");
    }else{
    	$(this.selector).html(message);
    }
};

ShowAlertAuto.prototype.show = function(){//显示信息提示框
    $(this.selector).fadeIn(this.fadeTime);
};

ShowAlertAuto.prototype.hide = function(){//隐藏信息提示框
    $(this.selector).fadeOut(this.fadeTime);
};

var alertAuto = new ShowAlertAuto(); //创建一个对象
/*end************ 信息提示框 **********************/

