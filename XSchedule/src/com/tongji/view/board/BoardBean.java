package com.tongji.view.board;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import com.tongji.activityinfo.IActivityService;
import com.tongji.basicinfo.UserBean;


@ManagedBean
@SessionScoped
public class BoardBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1094539741067141249L;
	
	private static final int LIMIT = 10;
	
	private List recentEvents;
	
	private int limit = LIMIT;
	
	@ManagedProperty(value = "#{userBean}")
	private UserBean currentUser;
	
	@ManagedProperty(value = "#{activityService}")
	private IActivityService activityService;
	
	
	@PostConstruct
	public void initialize(){
		recentEvents = activityService.getLimit(limit, null);
	}


	public IActivityService getActivityService() {
		return activityService;
	}


	public void setActivityService(IActivityService activityService) {
		this.activityService = activityService;
	}


	public UserBean getCurrentUser() {
		return currentUser;
	}


	public void setCurrentUser(UserBean currentUser) {
		this.currentUser = currentUser;
	}


	public List getRecentEvents() {
		return recentEvents;
	}


	public void setRecentEvents(List recentEvents) {
		this.recentEvents = recentEvents;
	}

}
