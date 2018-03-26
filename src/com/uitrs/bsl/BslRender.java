package com.uitrs.bsl;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.boilit.bsl.IEngine;
import org.boilit.bsl.ITemplate;

import com.jfinal.core.BslPlugin;
import com.jfinal.core.JFinal;
import com.jfinal.render.Render;
import com.jfinal.render.RenderException;
public class BslRender extends Render {
	public static String path = ""; //根路径
	public static Map<String, String> viewPathCache = new HashMap<String, String>();//记录缓存

	@Override
	public void render() {
		Map<String, Object> data = new HashMap<String, Object>();
		for (Enumeration<String> attrs = request.getAttributeNames(); attrs.hasMoreElements();) {
			String attrName = attrs.nextElement();
			data.put(attrName, request.getAttribute(attrName));
		}
		OutputStream responseOut = null;
		ByteArrayOutputStream byteOutput = null;
		try {
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setContentType("text/html; charset=" + getEncoding());
			response.setCharacterEncoding(getEncoding()); 
			
			//获得解析引擎 
			IEngine engine = BslPlugin.getEngine();
			
			String realView = JFinal.me().getServletContext().getRealPath(path + view); 
			
			ITemplate template = null; 
			String templatePath = realView;
			
			if (engine.isUseTemplateCache()) {
				//从Map对象中取realView, 如果没有在Map对象中记录过，就用真实路径， 否则从Map对象中取出缓存的路径
				templatePath = null == viewPathCache.get(realView) ? realView : viewPathCache.get(realView);
			}
			template = engine.getTemplate(templatePath); 
			
			if (engine.isUseTemplateCache()) { 
				viewPathCache.put(realView, templatePath);
			}
			byteOutput = new ByteArrayOutputStream(8192); 
			template.execute(data, byteOutput);
			responseOut = response.getOutputStream(); 
			responseOut.write(byteOutput.toByteArray()); 
			responseOut.flush(); //刷新缓存
		} catch (Exception e) {
			throw new RenderException(e);
		} finally {
			try {
				if (responseOut != null) {
					responseOut.close(); //关闭网络输出流
				}
				if (byteOutput != null) {
					byteOutput.close(); //关闭字节输出流
				}
			} catch (IOException e) {
				new RenderException(e);
			}
		}
	}
	
	public BslRender(String view) { 
		this.view = view;
	}
	
	@SuppressWarnings("static-access")
	public void setPath(String path){ 
		this.path = path;
	}
}
