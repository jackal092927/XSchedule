package com.tongji.persistence.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.junit.Test;

import com.tongji.persistence.HibernateSessionFactory;

public class GetPrivateEventsGroupByOwnerCategory {

	@Test
	public void Test() {
		String account = "092927";
		Session session = HibernateSessionFactory.getSession();
		try {
			ArrayList<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
			
			String queryString = "select activity.ownerCategory, count(activity) "
					+ "from ActivityModel activity "
					+ "where activity.ownerAccount = ? "
					+ "group by activity.ownerCategory";
			List queryList = session.createQuery(queryString)
					.setString(0, account).list();

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			session.close();
		}

	}
}
