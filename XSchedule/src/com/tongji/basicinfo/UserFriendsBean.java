package com.tongji.basicinfo;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import com.tongji.persistence.models.UserModel;

@ManagedBean
@SessionScoped
public class UserFriendsBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5628354744832879464L;
	
	@ManagedProperty(value = "#{userBean}")
	private UserBean currentUser;
	
	@ManagedProperty(value = "#{userService}")
	private IUserService userService;
	
	private List<UserModel> friendsList;
	
	@PostConstruct
	public void initialize(){
		friendsList = getUserService().getFriends(currentUser
				.getAccount());
	}
	
	public UserFriendsBean(){}
	
	public List<UserModel> getFollowersList(){
		//TODO:
		return friendsList;
	}
	
	public List<UserModel> getFollowTargetsList(){
		//TODO:
		return friendsList;
	}
	public List<UserModel> getFriendsList(){
		//TODO:
		return friendsList;
	}
	
	public UserBean getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(UserBean currentUser) {
		this.currentUser = currentUser;
	}

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

}
