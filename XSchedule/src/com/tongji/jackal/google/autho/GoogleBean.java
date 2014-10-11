package com.tongji.jackal.google.autho;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.tongji.jackal.google.autho.services.GoogleAuthoService;
import com.tongji.share.tools.MyFacesContextUtils;

@ManagedBean
@RequestScoped
public class GoogleBean implements Serializable {

	private static final String CALENDARSCOPE = "https://www.googleapis.com/auth/calendar";

	private static final String CLIENT_ID = "369292658168.apps.googleusercontent.com";
	private static final String CLIENT_SECRET = "taafYZ5UqIeWseNbyeKHg8ul";

	/**
	 * 
	 */
	private static final long serialVersionUID = -6153437249740345482L;

	public String getAuthorizationUrl() throws IOException {
		GoogleAuthoService googleAuthoService = new GoogleAuthoService();
		return googleAuthoService.getAuthorizaionUrlString();
	}

	public String getGoogleResponse() {
		ServletRequest request = (ServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		String code = request.getParameter("code");
		String error = request.getParameter("error");
		String state = request.getParameter("state");
		
		if ("access_denied".equals(error)) {
		}
		
		return "code: " + code + " error: " + error + " state: " + state;
		
		
	}
	
	public Object getGoogleUserAccountInfo(){
		//TODO:
		return null;
	}
	
}
