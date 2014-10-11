package com.tongji.basicinfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import com.tongji.persistence.models.UserEventClassifyModel;

@ManagedBean
@SessionScoped
public class UserClassifyBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6995151545938954572L;

	@ManagedProperty(value = "#{userBean}")
	private UserBean currentUser;

	@ManagedProperty(value = "#{userService}")
	private IUserService userService;

	private Map<String, UserEventClassifyModel> publicEventClassifyMap = new HashMap<String, UserEventClassifyModel>();
	private Map<String, UserEventClassifyModel> privateEventClassifyMap = new HashMap<String, UserEventClassifyModel>();
	private Map<String, UserEventClassifyModel> relatedEventClassifyMap = new HashMap<String, UserEventClassifyModel>();

	@PostConstruct
	public void initialize() {
		List<UserEventClassifyModel> classifications = new ArrayList<UserEventClassifyModel>();
		classifications = getUserService().getEventClassifyModel(
				getCurrentUser().getAccount());
		for (UserEventClassifyModel userEventClassifyModel : classifications) {
			privateEventClassifyMap.put(userEventClassifyModel.getCategory(),
					userEventClassifyModel);
		}
	}

	public UserClassifyBean() {

	}

	public UserBean getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(UserBean currentUser) {
		this.currentUser = currentUser;
	}

	public Map<String, UserEventClassifyModel> getPublicEventClassifyMap() {
		return publicEventClassifyMap;
	}

	public void setPublicEventClassifyMap(
			Map<String, UserEventClassifyModel> publicEventClassifyMap) {
		this.publicEventClassifyMap = publicEventClassifyMap;
	}

	public Map<String, UserEventClassifyModel> getPrivateEventClassifyMap() {
		return privateEventClassifyMap;
	}

	public void setPrivateEventClassifyMap(
			Map<String, UserEventClassifyModel> privateEventClassifyMap) {
		this.privateEventClassifyMap = privateEventClassifyMap;
	}

	public Map<String, UserEventClassifyModel> getRelatedEventClassifyMap() {
		return relatedEventClassifyMap;
	}

	public void setRelatedEventClassifyMap(
			Map<String, UserEventClassifyModel> relatedEventClassifyMap) {
		this.relatedEventClassifyMap = relatedEventClassifyMap;
	}

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

}
