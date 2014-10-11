package com.tongji.persistence.test;

import static org.junit.Assert.fail;

import java.util.Set;

import org.hibernate.Session;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tongji.persistence.HibernateSessionFactory;
import com.tongji.persistence.UserDAO;
import com.tongji.persistence.models.ActivityModel;
import com.tongji.persistence.models.UserModel;

public class UserActivities {

	@Test
	public void test() {
//		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
//		
//		UserDAO userDAO = (UserDAO)context.getBean("userDAO", UserDAO.class);
//		Set<ActivityModel> activitySet = userDAO.getUserActivitiesById("092927");
//		for (ActivityModel activityModel : activitySet) {
//			System.out.println(activityModel);
//		}
		
		
		Session session = HibernateSessionFactory.getSession();
		
		try {
			session.beginTransaction();
			UserModel user = (UserModel)session.load(UserModel.class, "092927");
			System.out.println(user.getActivities());
			Set<ActivityModel> activities = user.getActivities();
			
			for (ActivityModel activityModel : activities) {
				System.out.println(activityModel);
			}
			
			ActivityModel activity = (ActivityModel) session.load(ActivityModel.class, 1);
			System.out.println(activity.getOwner().getUsername());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		fail("Not yet implemented");
	}

}
