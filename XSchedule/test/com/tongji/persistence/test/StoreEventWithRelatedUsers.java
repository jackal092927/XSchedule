package com.tongji.persistence.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Session;
import org.junit.Test;

import com.tongji.persistence.HibernateSessionFactory;
import com.tongji.persistence.models.ActivityModel;
import com.tongji.persistence.models.UserEventRelation;
import com.tongji.share.tools.ModelsUtil;

public class StoreEventWithRelatedUsers {

	@Test
	public void Test() {
		Session session = HibernateSessionFactory.getSession();
		session.beginTransaction();
		ActivityModel event = new ActivityModel();
		event.setOwnerAccount("092927");
		event.setBeginTime(new Date());
		event.setEndTime(new Date());
		event.setTitle("Test Event and Related Users Again");
		event.setPublishTime(new Date());
		event.setUuid(UUID.randomUUID().toString());
		try {
			session.save(event);
			session.flush();
			List<UserEventRelation> relatedUserList = new ArrayList<UserEventRelation>();
			List<UserEventRelation> users = new ArrayList<UserEventRelation>();

			UserEventRelation userEventRelation = new UserEventRelation();
			userEventRelation.setRelatedUserAccount("092926");
			userEventRelation.setRelatedActivityId(event.getId());
			users.add(userEventRelation);

			relatedUserList.addAll(users);

			session.flush();
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: handle exception
			session.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

	}
}
