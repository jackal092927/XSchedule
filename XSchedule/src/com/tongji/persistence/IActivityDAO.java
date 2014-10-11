package com.tongji.persistence;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.tongji.persistence.models.ActivityModel;
import com.tongji.persistence.models.CommentModel;
import com.tongji.persistence.models.UserEventRelation;
import com.tongji.share.view.models.TransferItem;

public interface IActivityDAO {
	
	public int store(ActivityModel activityModel);
	
	public void delete(ActivityModel activityModel);
	
	public ActivityModel getById(int id );
	
	public List<ActivityModel> getAll();
	
	public List<ActivityModel> getLimit(int limit, String orderByString);
	
	public void deleteById(int id);
	
	public Set<UserEventRelation> getRelatedUsers(int activityId);
	
	public List<ActivityModel> getPublicActivities();
	
	public void storeEventWithRelatedUsers(ActivityModel event,
			List<UserEventRelation> relatedUserList);
	
	public void storeEventWithRelatedUsersList(ActivityModel relatedEvent,
			List<TransferItem> diffList);
	
	public List getRelatedCommentsByActivityId(int id);
	
	public int storeComment(CommentModel comment);
	
	public void deleteComment(int id);

	public List getLimitGeDate(Date date);

	public List getLimitWithSyncmark(long syncMark);

	public void updateEvent(ActivityModel event);

	public List getpublicEventsWithSyncmark(long syncMark);

	public List getRelatedEventsWithSyncmark(String account, long syncMark);

	public List getPrivateEventsWithSyncmark(String account, long syncMark);

	public void storeUserEventRelation(UserEventRelation userEventRelation);

	UserEventRelation getUserEventRelation(String account, int eventId);
	
}
