package com.tongji.basicinfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.tongji.persistence.IActivityDAO;
import com.tongji.persistence.models.ActivityModel;
import com.tongji.persistence.models.UserEventRelation;
import com.tongji.persistence.models.UserModel;



public class UserEventRelationService implements IUserEventRelationService{
	
	private IActivityDAO activityDAO;
	
	@Override
	public void store(UserEventRelation userEventRelation){
		
	}

	@Override
	public void storeEventWithRelatedUsers(ActivityModel event, List userList){
		activityDAO.storeEventWithRelatedUsers(event, userList);
	}

	public IActivityDAO getActivityDAO() {
		return activityDAO;
	}

	public void setActivityDAO(IActivityDAO activityDAO) {
		this.activityDAO = activityDAO;
	}

	@Override
	public void storeEventWithRelatedUsersDiff(ActivityModel relatedEvent,
			List diffList) {
		// TODO Auto-generated method stub
		activityDAO.storeEventWithRelatedUsersList(relatedEvent, diffList);
	}

	@Override
	public void storeUserEventRelation(String account, Integer id,
			String relatedCategory) {
		UserEventRelation userEventRelation = new UserEventRelation();
		userEventRelation.setRelatedUserAccount(account);
		userEventRelation.setRelatedActivityId(id);
		userEventRelation.setRelatedCategory(relatedCategory);
		activityDAO.storeUserEventRelation(userEventRelation);
	}
}
