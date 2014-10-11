package com.tongji.activityinfo;

public class Activity {
	
	private String title;
	private String publisher;
	private String location;
	
	
	public Activity(){
		
	}
	
	public Activity(String title, String publisher, String location){
		this.title = title;
		this.publisher = publisher;
		this.location = location;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	
	
}
