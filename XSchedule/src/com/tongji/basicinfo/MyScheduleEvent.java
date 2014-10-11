package com.tongji.basicinfo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.primefaces.model.ScheduleEvent;

import com.tongji.persistence.models.ActivityModel;
import com.tongji.share.tools.MyEnum.EventRelatedType;

public class MyScheduleEvent implements ScheduleEvent, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7541469194698856558L;
	
	private ActivityModel event = new ActivityModel();
	
	private boolean allDay = false;//TODO
	private boolean editable = false;
	private String styleClass;//styleClass for css
	private List authorityList; //TODO:
	private String modelClass = ActivityModel.class.getName();//eventType
	
	private EventRelatedType relatedType;
	private boolean newEvent = false;
	
	private boolean pin;
	private boolean join;
	private String category;
	private String categorySetColor;
	

	
	
	public ActivityModel getEvent() {
		return event;
	}

	public void setEvent(ActivityModel event) {
		this.event = event;
	}

	public EventRelatedType getRelatedType() {
		return relatedType;
	}

	public void setRelatedType(EventRelatedType relatedType) {
		this.relatedType = relatedType;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategorySetColor() {
		return categorySetColor;
	}

	public void setCategorySetColor(String categorySetColor) {
		this.categorySetColor = categorySetColor;
	}

	public MyScheduleEvent(){
		
	}
	
	public MyScheduleEvent(String title, Date start, Date end, boolean allDay) {
		setTitle(title);
		setStartDate(start);
		setEndDate(end);
		setAllDay(allDay);
	}
	
	@Override
	public Object getData() {
		return null;
	}

	@Override
	public Date getEndDate() {
		return event.getEndTime();
	}

	@Override
	public String getId() {
		return event.getUuid();
	}

	@Override
	public Date getStartDate() {
		return event.getBeginTime();
	}

	@Override
	public String getStyleClass() {
		return this.styleClass;
	}

	@Override
	public String getTitle() {
		return event.getTitle();
	}

	@Override
	public boolean isAllDay() {
		return this.allDay;
	}

	@Override
	public boolean isEditable() {
		return editable;
	}

	@Override
	public void setId(String uuid) {
		event.setUuid(uuid);
	}

	public String getUuid() {
		return event.getUuid();
	}

	public void setTitle(String title) {
		event.setTitle(title);
	}

	public void setStartDate(Date startDate) {
		event.setBeginTime(startDate);
	}

	public void setEndDate(Date endDate) {
		event.setEndTime(endDate);
	}

	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
		
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}
	public void setUuid(String uuid) {
		event.setUuid(uuid);
	}

	public String getOwner() {
		return event.getOwnerAccount();
	}

	public void setOwner(String owner) {
		event.setOwnerAccount(owner);
	}

	public String getLocation() {
		return event.getLocation();
	}

	public void setLocation(String location) {
		event.setLocation(location);
	}

	public Date getPublishTime() {
		return event.getPublishTime();
	}

	public void setPublishTime(Date publishTime) {
		event.setPublishTime(publishTime);
	}

	public void setPkId(int pkId) {
		event.setId(new Integer(pkId));
	}

	public int getPkId(){
		return event.getId().intValue();
	}

	public List getAuthorityList() {
		return authorityList;
	}

	public void setAuthorityList(List authorityList) {
		this.authorityList = authorityList;
	}

	@Override
	public int hashCode(){
		//TODO:
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + (getId() != null ? getId().hashCode() : 0 );
		return result;
	}

	public String getImageDefault() {
		return event.getImageDefault();
	}

	public void setImageDefault(String imageDefault) {
		event.setImageDefault(imageDefault);
	}

	public int getImageCount() {
		return event.getImageCount();
	}

	public void setImageCount(int imageCount) {
		event.setImageCount(imageCount);
	}
	
	public String getImageDefaultUri(){
		String imageUri;
		if(getImageCount() == 0 ){
			if(getImageDefault() == null){
				imageUri = "share/imgs/noImg.jpg";
				return imageUri;
			}else {
				return null;
			}
		}else {
			if(getImageDefault() != null){
				//TODO: if in temp dir?
				imageUri = getUuid() + "/imgs/" + getImageDefault();
				return imageUri;
			}else {
				return null;
			}
		}
	}

	public String getModelClass() {
		return modelClass;
	}

	public void setModelClass(String modelClass) {
		this.modelClass = modelClass;
	}

	public boolean isPin() {
		return pin;
	}

	public void setPin(boolean pin) {
		this.pin = pin;
	}

	public boolean isJoin() {
		return join;
	}

	public void setJoin(boolean join) {
		this.join = join;
	}

	public boolean isNewEvent() {
		return newEvent;
	}

	public void setNewEvent(boolean newEvent) {
		this.newEvent = newEvent;
	}



}
