package com.uitrs.inteceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.core.JFinal;

public class MyInterceptor extends Controller implements Interceptor {

	String staticPath ="";

	private void after(Invocation inv) {
		inv.getController().setAttr("basePath", JFinal.me().getContextPath()+"/");
	}

	@Override
	public void intercept(Invocation inv) {
		inv.invoke();
		after(inv);
	}
}
