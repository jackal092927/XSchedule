package com.tongji.basicinfo;

import java.util.List;
import java.util.Set;

import com.tongji.persistence.models.ActivityModel;
import com.tongji.persistence.models.UserEventRelation;
import com.tongji.persistence.models.UserModel;

public interface IUserEventRelationService {

	void store(UserEventRelation userEventRelation);

	public void storeEventWithRelatedUsers(ActivityModel event, List userList);

	public void storeEventWithRelatedUsersDiff(ActivityModel relatedEvent, List diffList);

	public void storeUserEventRelation(String account, Integer id,
			String selectedCategory);


}
