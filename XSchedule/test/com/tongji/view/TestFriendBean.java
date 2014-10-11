package com.tongji.view;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

import org.primefaces.model.DualListModel;

@ManagedBean
@SessionScoped
public class TestFriendBean {

	private DualListModel<String> friends;

	@PostConstruct
	public void initialize() {
		List<String> sourceList = new ArrayList<String>();
		List<String> targetList = new ArrayList<String>();

		for (int i = 0; i < 10; i++) {
			sourceList.add("label" + i);
		}
		
		friends = new DualListModel<String>(sourceList, targetList);

	}

	public DualListModel<String> getFriends() {
		return friends;
	}

	public void setFriends(DualListModel<String> friends) {
		this.friends = friends;
	}

}
