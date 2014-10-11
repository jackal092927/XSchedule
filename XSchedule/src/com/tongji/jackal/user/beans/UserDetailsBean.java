package com.tongji.jackal.user.beans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIOutput;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;

import com.tongji.basicinfo.IUserService;
import com.tongji.basicinfo.UserBean;
import com.tongji.persistence.models.UserModel;

@ManagedBean
@RequestScoped
public class UserDetailsBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9113151634404595258L;

	private UserModel userModel;

	private boolean followed;

	private boolean editable = false;

	@ManagedProperty(value = "#{userBean}")
	private UserBean currentUserBean;

	@ManagedProperty(value = "#{userService}")
	private IUserService userService;

	public UserDetailsBean() {
	}

	@PostConstruct
	public void initialize() {
		if (getCurrentUserBean() != null) {
			this.setUserModel(getUserService().getUserById(
					getCurrentUserBean().getAccount()));
		}
		followed = userService.ifFollowed(currentUserBean.getAccount(),
				userModel.getAccount());
	}

	public void reset() {
		this.initialize();
	}

	public void storeUserDetails() {
		userService.storeUser(userModel);
		changeEditable();
	}

	public UserBean getUserBean() {
		return getCurrentUserBean();
	}

	public void setUserBean(UserBean currentUserBean) {
		this.setCurrentUserBean(currentUserBean);
	}

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public UserModel getUserModel() {
		return userModel;
	}

	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}

	public UserBean getCurrentUserBean() {
		return currentUserBean;
	}

	public void setCurrentUserBean(UserBean currentUserBean) {
		this.currentUserBean = currentUserBean;
	}

	public boolean isFollowed() {
		return followed;
	}

	public void setFollowed(boolean followed) {
		this.followed = followed;
	}

	public boolean isEditable() {
		return editable && isSelf();
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public void changeEditable() {
		editable = !editable;
	}

	public void followClick() {
		if (!followed) {
			userService.aFollowb(currentUserBean.getAccount(),
					userModel.getAccount());
		} else {
			userService.aNotFollowb(currentUserBean.getAccount(),
					userModel.getAccount());
		}
		followed = !followed;
	}

	public boolean isSelf() {
		return currentUserBean.getAccount().equals(userModel.getAccount());
	}

	public void onValueChanged(ValueChangeEvent event) {
		String oldString = (String)event.getOldValue();
		String newString = (String)event.getNewValue();
		if (oldString.equals(newString)) {
			int i = 0;
			
		}
	}
	
	public void onSave(String fildString){
			userService.storeUser(userModel);
	}
	
	
}
