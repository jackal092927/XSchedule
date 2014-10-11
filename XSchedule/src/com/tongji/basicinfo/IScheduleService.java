package com.tongji.basicinfo;

import java.util.List;
import java.util.Set;

import org.primefaces.model.ScheduleModel;

public interface IScheduleService {

	
//	public ScheduleModel getScheduleModel(UserBean currentUser);

	public void storeEvent(MyScheduleEvent event);

	public void storeEventWithRelatedUsers(MyScheduleEvent event,
			List relatedUserList);

	public void storeEventWithRelatedUsersDiff(MyScheduleEvent event, Set diffSet);

	


}
