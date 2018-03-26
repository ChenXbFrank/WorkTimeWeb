package com.jfinal.core;
import org.boilit.bsl.AbstractEngine;
import org.boilit.bsl.Engine;
import org.boilit.bsl.IEngine;
import org.boilit.bsl.formatter.NumberFormatter;

import com.jfinal.config.Constants;
import com.jfinal.plugin.IPlugin; 
import com.jfinal.render.IMainRenderFactory;
import com.jfinal.render.Render;
import com.uitrs.bsl.BslRender;
public class BslPlugin  implements IPlugin{
	private static IEngine engine = null; 
	@SuppressWarnings("unused")
	private String baseViewPath; 

	public static IEngine getEngine() { 
		return engine;
	}

	public BslPlugin() {
	}

	public BslPlugin(String baseViewPath) { 
		this.baseViewPath = baseViewPath;
	}

	@Override
	public boolean start() {
		System.out.println("******************* 0.0  xfl plug start");
		try {

			engine = Engine.getEngine(); 
			
		
			engine.registerFormatter(Double.class, new NumberFormatter(".00") ); //注册格式化函数
			engine.registerFormatter(Integer.class, new NumberFormatter(".00") ); 
			((AbstractEngine) engine).setUseTemplateCache(false);
			
			Constants c = Config.getConstants(); 
			
			//在jfinal中注册渲染的模板(即render函数默认的模板)
			c.setMainRenderFactory(new IMainRenderFactory() {
				@Override
				public Render getRender(String view) { 
					return new BslRender(view);
				}

				@Override
				public String getViewExtension() { 
					return ".html";
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean stop() {
		System.out.println("******************* xfl plug end");
		if (null != engine && engine.isUseTemplateCache()) {
			engine.getTemplateCache().clear();
			engine = null;
		}
		return true;
	}
}
