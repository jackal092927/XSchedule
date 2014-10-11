package com.tongji.activityinfo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ManagedProperty;

import org.primefaces.model.ScheduleEvent;

import com.tongji.basicinfo.MyScheduleEvent;
import com.tongji.basicinfo.UserService;
import com.tongji.persistence.ActivityDAO;
import com.tongji.persistence.IActivityDAO;
import com.tongji.persistence.models.ActivityModel;
import com.tongji.persistence.models.CommentModel;
import com.tongji.share.tools.ModelsUtil;

public class ActivityService implements IActivityService, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 355119588235819544L;
	
	@ManagedProperty(value = "#{activityDAO}")
	private IActivityDAO activityDAO;

	
	public IActivityDAO getActivityDAO() {
		return activityDAO;
	}

	public void setActivityDAO(IActivityDAO activityDAO) {
		this.activityDAO = activityDAO;
	}

	@Override
	public int store(ActivityModel activityModel) {
		ModelsUtil.ModelConstraintCheck(activityModel);
		return activityDAO.store(activityModel);
	}

	@Override
	public void delete(ActivityModel activityModel) {
		activityDAO.delete(activityModel);
	}

	@Override
	public void deleteById(int id) {
		activityDAO.deleteById(id);
	}
	
	@Override
	public ActivityModel getById(int id) {
		return activityDAO.getById(id);
	}

	@Override
	public Set getRelatedUsers(MyScheduleEvent event) {
		if (event.getModelClass().equals(ActivityModel.class.getName())) {
			return activityDAO.getRelatedUsers(event.getPkId());
		}else {
			return null;
		}
	}

	@Override
	public List getRelatedComments(int id) {
		return activityDAO.getRelatedCommentsByActivityId(id);
	}

	@Override
	public int storeComment(CommentModel comment) {
		return activityDAO.storeComment(comment);
	}

	@Override
	public void deleteComment(int id) {
		activityDAO.deleteComment(id);
	}

	@Override
	public List getLimit(int limit, String orderByString) {
		return activityDAO.getLimit(limit, orderByString);
	}

	@Override
	public List<ActivityModel> getLimitWithSyncmark(long syncMark) {
		List<ActivityModel> eventList = activityDAO.getLimitWithSyncmark(syncMark);
		return eventList;
	}

	@Override
	public int addEvent(ActivityModel event) {
		return store(event);
	}

	@Override
	public void updateEvent(ActivityModel event) {
		// TODO Auto-generated method stub
		activityDAO.updateEvent(event);
	}
	
	@Override
	public List getPublicEventsWithSyncmark(long syncMark){
		return activityDAO.getpublicEventsWithSyncmark(syncMark);
	}
	
	@Override
	public List getPrivateEventsWithSyncmark(String account, long syncMark){
		return activityDAO.getPrivateEventsWithSyncmark(account, syncMark);
	}
	
	@Override
	public List getRelatedEventsWithSyncmark(String account, long syncMark){
		return activityDAO.getRelatedEventsWithSyncmark(account, syncMark);
	}
	
	@Override
	public List getUsereventsWithSyncmark(String account, long syncMark){
		
		return getPrivateEventsWithSyncmark(account, syncMark);
	}

	@Override
	public void storeUserEventRelation(String account, Integer id,
			String selectedCategory) {
	}
	
	
}
