package com.tongji.share.view;

import java.util.ArrayList;
import java.util.List;

import com.tongji.basicinfo.MyScheduleEvent;

public class MyTreeNodeData {
	private List eventData;
	private String displayString;
	private int count;

	public MyTreeNodeData() {
		eventData = new ArrayList<MyScheduleEvent>();
	}

	public MyTreeNodeData(List eventData) {
		this.eventData = eventData;
	}

	public List getEventData() {
		return eventData;
	}

	public void setEventData(List eventData) {
		this.eventData = eventData;
	}

	public String getDisplayString() {
		return displayString;
	}

	public void setDisplayString(String displayString) {
		this.displayString = displayString;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	@Override
	public String toString(){
		return this.displayString + "[" + count + "]";
	}
}
