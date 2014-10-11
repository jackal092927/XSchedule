package com.tongji.persistence.test;

import static org.junit.Assert.fail;

import org.hibernate.Session;
import org.junit.Test;

import com.tongji.persistence.HibernateSessionFactory;
import com.tongji.persistence.models.UserEventRelation;
import com.tongji.persistence.models.UserModel;

public class AddRelatedActivities {

	@Test
	public void test() {
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			UserModel user = (UserModel)session.get(UserModel.class, "092927");
			UserEventRelation userEventRelation = new UserEventRelation();
			userEventRelation.setRelatedActivityId(3);
			userEventRelation.setRelatedUserAccount("092927");
			user.getRelatedActivities().add(userEventRelation);
			session.getTransaction().commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		fail("Not yet implemented");
	}

}
