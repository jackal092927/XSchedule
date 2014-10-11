package com.tongji.basicinfo;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.ScheduleModel;

import com.tongji.share.beans.FileBean;
import com.tongji.share.tools.MyEnum.EventRelatedType;
import com.tongji.share.tools.MyEnum.FileType;
import com.tongji.share.tools.MyFacesContextUtils;
import com.tongji.share.view.NavigationBean;
import com.tongji.share.view.NavigationScheduleBean;

@ManagedBean
@SessionScoped
public class ScheduleBean implements Serializable {
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 4048239141072564861L;
	private ScheduleModel eventModel = new MyScheduleModel();
	
	private TimeZone timeZone = TimeZone.getDefault();
	// private MyScheduleEvent event;

	// TODO: reconstruct
	private ScheduleEventBean scheduleEventBean = new ScheduleEventBean();
	//

	//
	private String theme;
	//

	// TODO: to be packaged
	private List<String> imgList = new ArrayList<String>();

	// private FileBean fileBean = new FileBean();

	@ManagedProperty(value = "#{scheduleService}")
	private IScheduleService scheduleService;

	@ManagedProperty(value = "#{userBean}")
	private UserBean currentUser;

	// @ManagedProperty(value = "#{friendBean}")
	// private FriendPickListBean friendList;

	// @ManagedProperty(value = "#{ownerCategoryBean}")
	// private UserClassificationSelectionBean category;

	@ManagedProperty(value = "#{userEventRelationService}")
	private IUserEventRelationService userEventRelationService;

	@PostConstruct
	public void initialize() {
		if (currentUser != null) {
			try {
				// eventModel =
				// getScheduleService().getScheduleModel(currentUser);
			} catch (Exception e) {
				// TODO: what exception?
				e.printStackTrace();
			}
		}
	}

	public ScheduleBean() {
	}

	public void setCurrentUser(UserBean currentUser) {
		if (currentUser != null && currentUser.getAccount() != null
				&& currentUser.getAccount().length() > 0) {
			this.currentUser = currentUser;
		} else {
			// TODO: load the public view?
		}
	}

	public ScheduleModel getEventModel() {

		return eventModel;

	}

	// public void setEventModel(ScheduleModel eventModel) {
	// this.eventModel = eventModel;
	// }

	public MyScheduleEvent getEvent() {
		if (this.scheduleEventBean == null) {
			this.scheduleEventBean = new ScheduleEventBean();
		}
		return this.scheduleEventBean.getEvent();
	}

	public void setEvent(MyScheduleEvent event) {
		if (this.scheduleEventBean == null) {
			this.scheduleEventBean = new ScheduleEventBean();
		}
		this.scheduleEventBean.setEvent(event);
	}

	public void saveEvent() {

		if (getEvent().getRelatedType() == EventRelatedType.PRIVATE) {
			// TODO: try catch?
			scheduleEventBean.updateEvent();
			if (getEvent().getId() == null) {
				// TODO: pre defined may be better;
				String uuid = UUID.randomUUID().toString();
				getEvent().setId(uuid);
				getFileBean().setUuid(uuid);
				getEvent().setOwner(currentUser.getAccount());
				// getEvent().setPublishTime(new Date());
				//
				// event.setModelClass(ActivityModel.class.getName());
				//
				// getEvent().setCategory(
				// scheduleEventBean.getClassificationSelection()
				// .getSelectedCategory());
			}
			//
			// event.setImageDefault(fileBean.getFile().getFileName());
			// getEvent().setImageCount(
			// getEvent().getImageCount() + getFileBean().getFileCount());
			//
			Set diffSet = scheduleEventBean.getFriendPickList()
					.getDifferenceList();
			if (diffSet.size() > 0) {
				scheduleService.storeEventWithRelatedUsersDiff(getEvent(),
						diffSet);
			} else {
				scheduleService.storeEvent(getEvent());
			}

			// TODO: if mergeTemp false? through exception?
			mergeTemp();

			// this.initialize();
			// this.friendList.initialize();
			// this.friendList.setEvent(null);
			// this.friendList.getDifferenceList().clear();
			scheduleEventBean = new ScheduleEventBean(); // reset dialog form
			updateSchedule();
			// fileBean = new FileBean();
		}
	}

	private void updateSchedule() {
		NavigationScheduleBean navigationScheduleBean = (NavigationScheduleBean) MyFacesContextUtils
				.findBeanByName("navigationScheduleBean");
		// TODO: update process;
		navigationScheduleBean.initialize();
	}

	private void mergeTemp() {
		mergeTemp(FileType.IMG);
		mergeTemp(FileType.MATERIAL);
		mergeTemp(FileType.OTHERS);
	}

	private boolean mergeTemp(FileType fileType) {
		String fileTypeString = File.separator + fileType.toString()
				+ File.separator;
		String tempId = getFileBean().getTempId();
		String srcFileDir;
		if (tempId != null) {
			srcFileDir = MyFacesContextUtils.getEventTempDir(tempId)
					+ fileTypeString;
		} else {
			return false;
		}
		String uuid = getFileBean().getUuid();
		String destFileDir;
		if (uuid != null) {
			destFileDir = MyFacesContextUtils.getResourcesEventDir() + uuid
					+ fileTypeString;
		} else {
			return false;
		}
		// TODO: if overlay == false?
		Boolean overlay = true;
		return getFileBean().mergeFile(srcFileDir, destFileDir, overlay);
	}

	public void deleteEvent() {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"System Error", "Please try again later.");

		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void onEventSelect(SelectEvent selectEvent) {
		// if(this.event == null){
		// this.event = new MyScheduleEvent();
		// }
		MyScheduleEvent scheduleEvent = (MyScheduleEvent) selectEvent
				.getObject();
		scheduleEventBean = new ScheduleEventBean(scheduleEvent);
		// this.fileBean = new FileBean();
		// fileBean.setUuid(getEvent().getUuid());
		// TODO: to be packaged;
		// fileBean.loadFiles(FileType.IMG);
		//
		// this.friendList.setEvent(this.event);
		// this.friendList.iniWithEvent();
		// this.friendList.getDifferenceList().clear();
		// category.setSelectedCategory(this.event.getCategory());
	}

	public void onDateSelect(SelectEvent selectEvent) {
		MyScheduleEvent scheduleEvent = new MyScheduleEvent("",
				(Date) selectEvent.getObject(), (Date) selectEvent.getObject(),
				false);
		scheduleEvent.setNewEvent(true);
		scheduleEvent.setEditable(true);
		scheduleEvent.setOwner(currentUser.getAccount());
		scheduleEvent.setTitle("NEW TITLE");
		scheduleEvent.setRelatedType(EventRelatedType.PRIVATE);
		
		//test
		scheduleEvent.setLocation("NA");
		//
		
		
		scheduleEventBean = new ScheduleEventBean();
		scheduleEventBean.setEvent(scheduleEvent);

		// this.fileBean = new FileBean();
		// this.category.setSelectedCategory(null);
		// this.friendList.setEvent(null);
		// this.friendList.initialize();
		// this.friendList.getDifferenceList().clear();
	}

	public void onEventMove(ScheduleEntryMoveEvent event) {
		// TODO: save event;
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Event moved", "Day delta:" + event.getDayDelta()
						+ ", Minute delta:" + event.getMinuteDelta());
		addMessage(message);
	}

	public void onEventResize(ScheduleEntryResizeEvent event) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Event resized", "Day delta:" + event.getDayDelta()
						+ ", Minute delta:" + event.getMinuteDelta());
		addMessage(message);
	}

	private void addMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public IScheduleService getScheduleService() {
		return this.scheduleService;
	}

	public void setScheduleService(IScheduleService scheduleService) {
		this.scheduleService = scheduleService;
	}

	public IUserEventRelationService getUserRelationService() {
		return getUserEventRelationService();
	}

	public void setUserRelationService(
			IUserEventRelationService userRelationService) {
		this.setUserEventRelationService(userRelationService);
	}

	public IUserEventRelationService getUserEventRelationService() {
		return userEventRelationService;
	}

	public void setUserEventRelationService(
			IUserEventRelationService userEventRelationService) {
		this.userEventRelationService = userEventRelationService;
	}

	public FileBean getFileBean() {
		if (scheduleEventBean == null) {
			scheduleEventBean = new ScheduleEventBean();
		}
		return scheduleEventBean.getFileBean();
	}

	public void setFileBean(FileBean fileBean) {
		if (scheduleEventBean == null) {
			scheduleEventBean = new ScheduleEventBean();
		}
		scheduleEventBean.setFileBean(fileBean);
	}

	/*
	 * public void handleFileUpload(FileUploadEvent event) throws IOException {
	 * this.fileBean.setFile(event.getFile()); try { if (fileBean.getFile() !=
	 * null) { String tempId = fileBean.getTempId(); if (tempId == null) {
	 * tempId = UUID.randomUUID().toString(); }
	 * 
	 * String pathString = MyFacesContextUtils.getEventTempDir(tempId); File
	 * file = fileBean.storeFile(pathString);
	 * 
	 * // String uuid = this.event.getUuid(); // if (uuid != null) { // // TODO:
	 * // ServletContext ctx = (ServletContext) FacesContext //
	 * .getCurrentInstance().getExternalContext() // .getContext(); // String
	 * pathString = ctx.getRealPath("/") // + "resources/events/" + uuid +
	 * "/imgs/"; // fileBean.storeFile(pathString); // } else { // // TODO: //
	 * String pathString = "temp"; // fileBean.storeFile(pathString); // }
	 * 
	 * // this.event.setImageDefault(fileBean.getFile().getFileName()); // TODO:
	 * to be simplified; fileBean.getFiles().add(new EventImgFile(file));
	 * fileBean.updateCount(); } } catch (IOException e) { // TODO: handle
	 * exception e.printStackTrace(); FacesMessage msg = new
	 * FacesMessage("Fail", fileBean.getFile() .getFileName() +
	 * " upload fail."); FacesContext.getCurrentInstance().addMessage(null,
	 * msg); } catch (Exception e) { // TODO: handle exception
	 * e.printStackTrace(); }
	 * 
	 * }
	 */

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		scheduleEventBean.handleFileUpload(event);
	}

	public NavigationBean getNavigationBean() {
		// TODO:
		return null;
	}

	public void cancelEvent() {
		String fileOrDirAbsolutePath = MyFacesContextUtils
				.getEventTempDir(getFileBean().getTempId());
		if (getFileBean().getFileCount() > 0) {
			getFileBean().removeFileOrDir(fileOrDirAbsolutePath);
		}
		scheduleEventBean = new ScheduleEventBean();
		// fileBean = new FileBean();
	}

	public ScheduleEventBean getScheduleEventBean() {
		return scheduleEventBean;
	}

	public void setScheduleEventBean(ScheduleEventBean scheduleEventBean) {
		this.scheduleEventBean = scheduleEventBean;
	}

	public void pinClick() {
		scheduleEventBean.pinEvent();
	}

	public void joinClick() {
		scheduleEventBean.joinEvent();
	}

	public void fileDialogClick() {
		int i = 0;
		i++;
	}

	public void test1() {
		int i = 0;
		i++;
	}
	// public List<String> getImgList() {
	// return fileBean.getFileNames();
	// }
	//
	// public void setImgList(List<String> imgList) {
	// this.imgList = imgList;
	// }

	public TimeZone getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

}
