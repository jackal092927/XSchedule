package com.tongji.share.view;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.component.schedule.Schedule;


@ManagedBean
@SessionScoped
public class NavigationBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5129732002961551346L;
	
	private Map<String,String> sourceMap;
	private String currentSource;
	private String currentPage;
	
	public NavigationBean(){}
	
	@PostConstruct
	public void initialize(){
		this.sourceMap = new HashMap<String, String>();
		//TODO: initialize the sourceMap;
		this.sourceMap.put("board", "/pages/board.xhtml");
		this.sourceMap.put("schedule", "/pages/schedule.xhtml");
		this.sourceMap.put("people", "/pages/people.xhtml");
		
		this.currentPage = "schedule";
		this.currentSource = this.sourceMap.get(this.currentPage);
		
		
	}

	public String getCurrentSource() {
		return currentSource;
	}

	public void setCurrentSource(String currentSource) {
		this.currentSource = currentSource;
	}

	public Map<String,String> getSourceMap() {
		return sourceMap;
	}

	public void setSourceMap(Map<String,String> sourceMap) {
		this.sourceMap = sourceMap;
	}
	
	public void onTabButtonClick(String sourceKey){
		this.currentPage = sourceKey;
		this.currentSource = sourceMap.get(currentPage);
		
//		FacesContext facesContext = FacesContext.getCurrentInstance();
//		Schedule schedule =  (Schedule)facesContext.getViewRoot().findComponent(":layoutCenterForm:centerSchedule");
//		schedule.setRendered(currentPage.equals("schedule"));
		
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}
	
//	public boolean ifCurrentPage(String sourceKey){
//		if (sourceKey.equals(this.currentPage)) {
//			return true;
//		} else {
//			return false;
//		}
//	}
//	

}
