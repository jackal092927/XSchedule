package com.tongji.persistence.test;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.UUID;

import org.hibernate.Session;
import org.junit.Test;

import com.tongji.persistence.HibernateSessionFactory;
import com.tongji.persistence.models.ActivityModel;
import com.tongji.persistence.models.UserModel;

public class SaveActivity {

	@Test
	public void test() {
		
		Session session = HibernateSessionFactory.getSession();
		try {
			session.beginTransaction();
			ActivityModel activityModel = new ActivityModel();
			activityModel.setUuid(UUID.randomUUID().toString());
			activityModel.setBeginTime(new Date());
			activityModel.setEndTime(new Date());
			activityModel.setOwnerAccount("092927");
			activityModel.setPublishTime(new Date());
			activityModel.setTitle("testowner");
			session.save(activityModel);
			session.getTransaction().commit();
			session.close();
			//UserModel user = (UserModel)session.load(UserModel.class, "091917");
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		fail("Not yet implemented");
	}

}
