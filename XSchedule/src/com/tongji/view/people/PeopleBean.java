package com.tongji.view.people;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import com.tongji.basicinfo.UserBean;
import com.tongji.persistence.models.UserModel;

@ManagedBean
@SessionScoped
public class PeopleBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -803158089652175359L;
	
	@ManagedProperty(value="#{userBean}")
	private UserBean currentUser;
	
	private List friendsList;
	
	private List majorUserList;
	
	@PostConstruct
	public void initialize(){
		majorUserList = new ArrayList<UserModel>();
		for (int i = 0; i < 10; i++) {
			UserModel user = new UserModel();
			user.setAccount("test" + i);
			user.setUsername("robot" + i);
			
		}
	}

	public UserBean getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(UserBean currentUser) {
		this.currentUser = currentUser;
	}

	public List getFriendsList() {
		return friendsList;
	}

	public void setFriendsList(List friendsList) {
		this.friendsList = friendsList;
	}

	public List getMajorUserList() {
		return majorUserList;
	}

	public void setMajorUserList(List majorUserList) {
		this.majorUserList = majorUserList;
	}
	
	public void pageChange(int pageNo, int currentPage){
		int i=0;
		i++;
	}

	
}
