package com.tongji.share.tools;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import com.sun.org.apache.bcel.internal.generic.NEW;

public class MyFacesContextUtils {

	/**
	 * 
	 * @return AbsolutePath: WebRootDir + "resources/events/";
	 */
	public static String getResourcesEventDir() {
		return getWebrootDir() + "resources/events/";
	}

	public static String getWebrootDir() {
		ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance()
				.getExternalContext().getContext();
		String pathString = ctx.getRealPath("/");
		return pathString;
	}

	public static String getEventTempDir(String tempId) {
		String fileDirName = getResourcesEventDir() + "share/temp/" + tempId
				+ "/";
		return fileDirName;
	}

	public static String getEventImgsDir(String uuid) {
		String fileDirName = MyFacesContextUtils.getResourcesEventDir() + uuid
				+ "/imgs/";
		return fileDirName;
	}

	public static String getRelatedWebResourceEventDirPre() {
		return "resources/events/";
	}

	public static Object findBeanByName(String name) {
		String expression = "#{" + name + "}";
		FacesContext context = FacesContext.getCurrentInstance();
		ELContext elContext = context.getELContext();// ?
		Application application = context.getApplication();
		
		Object obj = application.getExpressionFactory()
				.createValueExpression(elContext, expression, Object.class)
				.getValue(elContext);

		return obj;
	}
}
