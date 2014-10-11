package com.tongji.persistence.test;

import static org.junit.Assert.fail;

import org.hibernate.Session;
import org.junit.Test;

import com.tongji.persistence.HibernateSQLQueryUtils;
import com.tongji.persistence.HibernateSessionFactory;
import com.tongji.persistence.models.UserModel;

public class AddFollowerBySql {
	@Test
	public void test() {
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
//			UserModel userModel = (UserModel)session.get(UserModel.class, "092927");
//			userModel.getFollowers().add((UserModel)session.get(UserModel.class, "092926"));
			String sqlString = "insert into user_follower(user_account, follower_account) values(?,?)";
			Object[] values = {"092926", "092927"};
			int result = HibernateSQLQueryUtils.updateBySQL(session, sqlString, values);
			System.out.println(result);
			session.getTransaction().commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		fail("Not yet implemented");
	}
}
