package com.tongji.persistence.test;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Test;

import com.tongji.activityinfo.ActivityManagement;
import com.tongji.persistence.HibernateSessionFactory;
import com.tongji.persistence.models.ActivityModel;

public class GetFriendEventsCountGroupByOwner {

	@Test
	public void test() {
		Session session = HibernateSessionFactory.getSession();
		try {
			session.beginTransaction();
			String queryString = "select userEvent.relatedActivity.ownerAccount, count(userEvent) "
					+ "from UserEventRelation userEvent "
					+ "where userEvent.relatedUserAccount = ? " 
					+ "group by userEvent.relatedActivity.ownerAccount";
			
			Query query = session.createQuery(queryString);
			query.setParameter(0, "092927");
			Iterator it = query.list().iterator();
			while (it.hasNext()) {
				Object[] row = (Object[])it.next();
				String owner = (String)row[0];
				Long count = (Long)row[1];
				System.out.println(owner + "," + count.toString());
			}
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
		fail("Not yet implemented");
	}

}
