package com.tongji.persistence.test;

import static org.junit.Assert.fail;

import org.hibernate.Session;
import org.junit.Test;

import com.tongji.persistence.HibernateSQLQueryUtils;
import com.tongji.persistence.HibernateSessionFactory;

public class GetRelatedActivities {

	@Test
	public void test() {
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();

			session.getTransaction().commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		fail("Not yet implemented");
	}
}
