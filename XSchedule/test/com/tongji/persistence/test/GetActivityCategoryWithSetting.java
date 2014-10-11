package com.tongji.persistence.test;

import org.hibernate.Session;
import org.junit.Test;

import com.tongji.persistence.HibernateSessionFactory;
import com.tongji.persistence.models.ActivityModel;
import com.tongji.persistence.models.UserEventClassifyModel;

public class GetActivityCategoryWithSetting {
	@Test
	public void Test(){
		Session session = HibernateSessionFactory.getSession();
		try {
			ActivityModel activity = new ActivityModel();
			session.load(activity, 1);
			UserEventClassifyModel eventCategory = activity.getOwnerCategoryWithSetting();
			System.out.println(eventCategory + eventCategory.getColor());
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			session.close();
		}

	}
}
