package com.tongji.share.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.ScheduleEvent;

import antlr.Utils;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.tongji.activityinfo.ActivityService;
import com.tongji.activityinfo.IActivityService;
import com.tongji.basicinfo.IUserService;
import com.tongji.basicinfo.MyScheduleEvent;
import com.tongji.basicinfo.UserBean;
import com.tongji.basicinfo.UserFriendsBean;
import com.tongji.persistence.models.UserEventRelation;
import com.tongji.persistence.models.UserModel;
import com.tongji.share.tools.CollectionManipulation;
import com.tongji.share.tools.ModelsUtil;
import com.tongji.share.tools.MyApplicationContextUtils;
import com.tongji.share.tools.MyFacesContextUtils;
import com.tongji.share.view.models.TransferItem;

public class FriendPickListBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2686049430700166109L;

	private IActivityService activityService;

	private DualListModel<UserEventRelation> friendsPickList;

//	private MyScheduleEvent event;

	private Set<TransferItem> differenceList = new HashSet<TransferItem>();

	public FriendPickListBean() {
		initialize();
	}

	public FriendPickListBean(MyScheduleEvent event) {
		initialize();
		if (event != null) {
			activityService = (ActivityService)MyApplicationContextUtils.getBean("activityService");
			Set<UserEventRelation> relatedUserSet = getActivityService()
					.getRelatedUsers(event);
			CollectionManipulation.ListRemoveSetWithKey(
					friendsPickList.getSource(), relatedUserSet,
					event.getPkId());
			friendsPickList.setTarget(ModelsUtil.setToList(relatedUserSet));
		}
	}

	public void initialize() {
		friendsPickList = new DualListModel<UserEventRelation>();
		UserFriendsBean userFriendsBean = (UserFriendsBean) MyFacesContextUtils
				.findBeanByName("userFriendsBean");
		List<UserModel> userList = userFriendsBean.getFriendsList();

		LinkedList<UserEventRelation> sourceList = new LinkedList<UserEventRelation>();
		for (UserModel user : userList) {
			sourceList.add(ModelsUtil.usermodelToUserEventRelation(user));
		}

		friendsPickList.setSource(sourceList);
		friendsPickList.setTarget(new ArrayList<UserEventRelation>());
	}

	// I don't know why the PickList need calling getFriends() then setFriends()
	// once
	// before actionListener iniWithEvent and then show with another getFriends.
//	public void iniWithEvent() {
//		initialize();
//		if (event != null) {
//			Set<UserEventRelation> relatedUserSet = getActivityService()
//					.getRelatedUsers(event);
//			CollectionManipulation.ListRemoveSetWithKey(
//					friendsPickList.getSource(), relatedUserSet,
//					this.event.getPkId());
//			friendsPickList.setTarget(ModelsUtil.setToList(relatedUserSet));
//		}
//		// if(differenceList.size() > 0){
//		// recoverWithDifferenceList();
//		// }
//		//
//	}

	private void recoverWithDifferenceList() {
		sourceListRemoveDiffset();
		for (TransferItem item : differenceList) {
			if (item.isAdd()) {
				this.friendsPickList.getTarget().add(
						(UserEventRelation) item.getItem());
			}
		}
	}

	private void sourceListRemoveDiffset() {
		Iterator it = friendsPickList.getSource().iterator();
		while (it.hasNext()) {
			TransferItem item = new TransferItem(it.next(), false);
			if (differenceList.contains(item)) {
				it.remove();
			}
		}
	}

	public void onTransfer(TransferEvent event) {
		boolean add = event.isAdd();
		List<UserEventRelation> itemList = (List<UserEventRelation>) event
				.getItems();
		for (UserEventRelation userEventRelation : itemList) {
			TransferItem item = new TransferItem(userEventRelation, add);
			mergeTransfers(item);
		}
	}

	private void mergeTransfers(TransferItem item) {
		if (!this.differenceList.add(item)) {
			differenceList.remove(item);
		}
	}

	public DualListModel<UserEventRelation> getFriendsPickList() {
		return friendsPickList;
	}

	public void setFriendsPickList(
			DualListModel<UserEventRelation> friendsPickList) {
		this.friendsPickList = friendsPickList;
	}

	public IActivityService getActivityService() {
		return activityService;
	}

	public void setActivityService(IActivityService activityService) {
		this.activityService = activityService;
	}

//	public MyScheduleEvent getEvent() {
//		return event;
//	}
//
//	public void setEvent(MyScheduleEvent event) {
//		this.event = event;
//	}

	public Set getDifferenceList() {
		return differenceList;
	}

	public void setDifferenceList(Set differenceList) {
		this.differenceList = differenceList;
	}

}
