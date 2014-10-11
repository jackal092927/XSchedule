package com.tongji.persistence.test;

import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.junit.Test;

import com.tongji.persistence.HibernateSessionFactory;
import com.tongji.persistence.models.ActivityModel;
import com.tongji.persistence.models.UserEventRelation;

public class UpdateEventWithRelatedUsers {
	@Test
	public void Test() {
		Session session = HibernateSessionFactory.getSession();
		Set<UserEventRelation> set;
		try {
			session.beginTransaction();

			ActivityModel activity = new ActivityModel();
			session.load(activity, 24);
			set = activity.getRelatedUsers();
			UserEventRelation temp = new UserEventRelation();
			temp.setRelatedUserAccount("092926");
			temp.setRelatedActivityId(24);
			temp.setRelatedColorSetting("BLACK_Liu");
			set.add(temp);
			activity.getRelatedUsers().clear();
			activity.setRelatedUsers(null);

			session.saveOrUpdate(activity);
			session.flush();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}
}
