package com.tongji.activityinfo;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.tongji.basicinfo.MyScheduleEvent;
import com.tongji.persistence.models.ActivityModel;
import com.tongji.persistence.models.CommentModel;

public interface IActivityService {
	public int store(ActivityModel activityModel);

	public void delete(ActivityModel activityModel);
	
	public void deleteById(int id);
	
	public ActivityModel getById(int id);

	public Set getRelatedUsers(MyScheduleEvent event);
	
	public List getRelatedComments(int id);
	
	public int storeComment(CommentModel comment);
	
	public void deleteComment(int id);
	
	public List getLimit(int limit, String orderByString);
	
	public List getLimitWithSyncmark(long syncMark);

	public int addEvent(ActivityModel event);

	public void updateEvent(ActivityModel event);

	public List getUsereventsWithSyncmark(String account, long syncMark);

	List getPublicEventsWithSyncmark(long syncMark);

	List getPrivateEventsWithSyncmark(String account, long syncMark);

	List getRelatedEventsWithSyncmark(String account, long syncMark);

	public void storeUserEventRelation(String account, Integer id,
			String selectedCategory);
	
}
