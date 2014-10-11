package com.tongji.basicinfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

public class MyScheduleModel implements ScheduleModel {

	private List<ScheduleEvent> events;

	public MyScheduleModel() {
		events = new ArrayList<ScheduleEvent>();
	}

	public MyScheduleModel(List<ScheduleEvent> events) {
		this.events = events;
	}

	@Override
	public void addEvent(ScheduleEvent event) {
		//TODO: check duplication;
		getEvents().add(event);
	}

	@Override
	public void clear() {
		getEvents().clear();
	}

	@Override
	public boolean deleteEvent(ScheduleEvent event) {
		return getEvents().remove(event);
	}

	@Override
	public ScheduleEvent getEvent(String id) {
		for (ScheduleEvent event : events) {
			if (event.getId().equals(id))
				return event;
		}

		return null;

	}

	@Override
	public int getEventCount() {
		return getEvents().size();
	}

	@Override
	public List<ScheduleEvent> getEvents() {
		return this.events;
	}

	@Override
	public void updateEvent(ScheduleEvent event) {
		int index = -1;

		for (int i = 0; i < events.size(); i++) {
			if (events.get(i).getId().equals(event.getId())) {
				index = i;
				break;
			}
		}

		if (index >= 0) {
			events.set(index, event);
		}
	}

}
