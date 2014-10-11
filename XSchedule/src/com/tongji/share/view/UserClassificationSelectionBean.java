package com.tongji.share.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import com.tongji.basicinfo.IUserService;
import com.tongji.basicinfo.UserBean;
import com.tongji.basicinfo.UserClassifyBean;
import com.tongji.persistence.models.UserEventClassifyModel;
import com.tongji.share.tools.MyFacesContextUtils;

public class UserClassificationSelectionBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -291099156070359072L;
	private List<UserEventClassifyModel> classifications;
	private String selectedCategory = "[unclassified]";
	
	private boolean changed = false;

	public UserClassificationSelectionBean() {
		classifications = new ArrayList<UserEventClassifyModel>();
		UserClassifyBean userClassifyBean = (UserClassifyBean) MyFacesContextUtils
				.findBeanByName("userClassifyBean");
		Iterator<UserEventClassifyModel> it = userClassifyBean
				.getPrivateEventClassifyMap().values().iterator();
		while (it.hasNext()) {
			classifications.add(it.next());
		}
	}

	// @PostConstruct
	// public void initialize() {
	// categories = new ArrayList<UserEventClassifyModel>();
	// if (getCurrentUser() != null) {
	// categories = getUserService().getCategoryModels(
	// getCurrentUser().getAccount());
	// }
	//
	// }

	public String getSelectedCategory() {
		return selectedCategory;
	}

	public void setSelectedCategory(String selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

	public List getClassifications() {
		return classifications;
	}

	public void setClassifications(List classifications) {
		this.classifications = classifications;
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}
}
