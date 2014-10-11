package com.tongji.basicinfo;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import javax.faces.event.ValueChangeEvent;
import javax.mail.Store;

import org.primefaces.event.FileUploadEvent;

import com.tongji.activityinfo.ActivityService;
import com.tongji.activityinfo.IActivityService;
import com.tongji.persistence.models.ActivityModel;
import com.tongji.share.beans.FileBean;
import com.tongji.share.tools.ModelsUtil;
import com.tongji.share.tools.MyApplicationContextUtils;
import com.tongji.share.tools.MyEnum.EventRelatedType;
import com.tongji.share.tools.MyEnum.FileType;
import com.tongji.share.tools.MyFacesContextUtils;
import com.tongji.share.view.FriendPickListBean;
import com.tongji.share.view.UserClassificationSelectionBean;
import com.tongji.share.view.schedule.CommentsBean;
import com.tongji.share.view.schedule.ContextEditorBean;

public class ScheduleEventBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -446961549497367295L;

	private UserBean currentUser;

	private MyScheduleEvent event;

	private FriendPickListBean friendPickList;

	private UserClassificationSelectionBean classificationSelection;

	private FileBean fileBean;

	private ContextEditorBean contextEditorBean = new ContextEditorBean();

	private CommentsBean commentsBean;

	private IActivityService activityService;

	private IUserService userService;

	private IUserEventRelationService userEventRelationService;

	public ScheduleEventBean() {
		currentUser = (UserBean) MyFacesContextUtils.findBeanByName("userBean");

		event = new MyScheduleEvent();

		friendPickList = new FriendPickListBean();

		classificationSelection = new UserClassificationSelectionBean();

		fileBean = new FileBean();

		commentsBean = new CommentsBean(currentUser);
	}

	public ScheduleEventBean(MyScheduleEvent event) {
		currentUser = (UserBean) MyFacesContextUtils.findBeanByName("userBean");

		this.event = event;

		friendPickList = new FriendPickListBean(event);

		classificationSelection = new UserClassificationSelectionBean();
		if (event.getRelatedType().equals(EventRelatedType.PRIVATE)) {
			ModelsUtil.ModelConstraintCheck(event.getEvent());
			event.setEditable(true);
			if (event.getCategory() == null || event.getCategory().length() < 1) {
				event.setCategory(event.getEvent().getOwnerCategory());

			}
		}
		classificationSelection.setSelectedCategory(event.getCategory());

		fileBean = new FileBean(event);
		// fileBean.setUuid(event.getUuid());
		// fileBean.loadFiles(FileType.IMG);

		// ini CommentsBean(currentUser, eventId)
		commentsBean = new CommentsBean(currentUser, event.getEvent().getId());
	}

	public void updateEvent() {
		event.getEvent().setPublishTime(new Date());
		event.getEvent().setOwnerCategory(
				classificationSelection.getSelectedCategory());//
		event.getEvent().setImageCount(
				event.getEvent().getImageCount() + fileBean.getFileCount());
		File currentFile = fileBean.getCurrentFile().getFile();
		if (currentFile != null) {
			event.getEvent().setImageDefault(currentFile.getName());
		}
		// TODO: how to set default value?
		if (event.getEvent().getOwnerColorSetting() == null) {
			event.getEvent().setOwnerColorSetting("DEFAULT");
		}
	}

	public MyScheduleEvent getEvent() {
		return event;
	}

	public void setEvent(MyScheduleEvent event) {
		this.event = event;
	}

	public FriendPickListBean getFriendPickList() {
		return friendPickList;
	}

	public void setFriendPickList(FriendPickListBean friendPickList) {
		this.friendPickList = friendPickList;
	}

	public UserClassificationSelectionBean getClassificationSelection() {
		return classificationSelection;
	}

	public void setClassificationSelectionBean(
			UserClassificationSelectionBean classificationSelection) {
		this.classificationSelection = classificationSelection;
	}

	public FileBean getFileBean() {
		return fileBean;
	}

	public void setFileBean(FileBean fileBean) {
		this.fileBean = fileBean;
	}

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		fileBean.handleFileUpload(event);
	}

	public ContextEditorBean getContextEditorBean() {
		return contextEditorBean;
	}

	public void setContextEditorBean(ContextEditorBean contextEditorBean) {
		this.contextEditorBean = contextEditorBean;
	}

	public CommentsBean getCommentsBean() {
		return commentsBean;
	}

	public void setCommentsBean(CommentsBean commentsBean) {
		this.commentsBean = commentsBean;
	}

	public UserBean getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(UserBean currentUser) {
		this.currentUser = currentUser;
	}

	public void pinEvent() {
		// TODO
		IUserService userService = (UserService) MyApplicationContextUtils
				.getBean("userService");
		userService.pinEvent(currentUser.getAccount(),
				event.getEvent().getId(), !event.isPin());
		event.setPin(!event.isPin());
	}

	public void joinEvent() {
		getUserService().joinEvent(currentUser.getAccount(),
				event.getEvent().getId(), !event.isJoin());
		event.setJoin(!event.isJoin());
	}

	public void setNewevent(boolean newEvent) {
		event.setNewEvent(newEvent);
		event.setOwner(currentUser.getAccount());
		event.setEditable(true);
	}

	public boolean isPrivate() {
		return event.getRelatedType().equals(EventRelatedType.PRIVATE);
	}

	public boolean isNewevent() {
		return event.isNewEvent();
	}

	public boolean isEditable() {
		return event.isEditable();
	}

	public void onSave() {
		if (!event.isNewEvent()) {
			ActivityModel eventModel = this.event.getEvent();
			if (classificationSelection.isChanged()) {
				if (event.getRelatedType().equals(EventRelatedType.PRIVATE)) {
					getActivityService().store(eventModel);
				} else {
					getUserEventRelationService().storeUserEventRelation(
							currentUser.getAccount(), eventModel.getId(),
							classificationSelection.getSelectedCategory());
				}
				classificationSelection.setChanged(false);
			} else {
				activityService.store(eventModel);
			}
		}
	}

	public void onSubmit() {
		ActivityModel eventModel = this.event.getEvent();
		int eventId = getActivityService().store(eventModel);
		activityService.store(eventModel);
	}

	public void onCancle() {

	}

	public boolean notNewEvent() {
		return !isNewevent();
	}

	public boolean isDeletable(boolean isAdmin) {
		return (!isNewevent()) && (isPrivate() || isAdmin);
	}

	public void onClassify(ValueChangeEvent event) {
		String oldString = (String) event.getOldValue();
		String newString = (String) event.getNewValue();
		if (!oldString.equals(newString)) {
			classificationSelection.setChanged(true);
			if (isPrivate()) {
				this.event.getEvent().setOwnerCategory(newString);
			} else {
				this.event.setCategory(newString);
			}
		}

	}

	public IActivityService getActivityService() {
		if (activityService == null) {
			activityService = (ActivityService) MyApplicationContextUtils
					.getBean("activityService");
		}
		return activityService;
	}

	public void setActivityService(IActivityService activityService) {
		this.activityService = activityService;
	}

	public IUserService getUserService() {
		if (userService == null) {
			userService = (UserService) MyApplicationContextUtils
					.getBean("userService");
		}
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public IUserEventRelationService getUserEventRelationService() {
		if (userEventRelationService == null) {
			userEventRelationService = (UserEventRelationService) MyApplicationContextUtils
					.getBean("userEventRelationService");
		}
		return userEventRelationService;
	}

	public void setUserEventRelationService(
			IUserEventRelationService userEventRelationService) {
		this.userEventRelationService = userEventRelationService;
	}

}
