package com.tongji.persistence.test;

import static org.junit.Assert.fail;

import java.util.Iterator;

import org.hibernate.Session;
import org.junit.Test;

import com.tongji.persistence.HibernateSessionFactory;

public class GetPublicEventsCountGroupByOwner {

	@Test
	public void test() {
		try {
			Session session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			String queryString = 
					"select activity.ownerAccount, count(activity) " +
					"from ActivityModel activity " +
					"where activity.publicity = 1 " +
					"group by activity.owner";
			Iterator iterator = session.createQuery(queryString).list().iterator();
			while (iterator.hasNext()) {
				Object[] row = (Object[])iterator.next();
				System.out.println((String)row[0]+" " + row[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		fail("Not yet implemented");
	}
}
