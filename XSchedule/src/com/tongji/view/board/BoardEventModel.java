package com.tongji.view.board;

import java.util.Date;

import com.tongji.persistence.models.ActivityModel;
import com.tongji.share.view.models.EventModel;

public class BoardEventModel extends EventModel {
	
	public BoardEventModel(){
		
	}
	
	public ActivityModel getEvent() {
		return event;
	}

	public void setEvent(ActivityModel event) {
		this.event = event;
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
}
