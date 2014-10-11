package com.tongji.persistence.test;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Test;

import com.tongji.persistence.HibernateSessionFactory;
import com.tongji.persistence.models.ActivityModel;


public class ShowActivities {

	@Test
	public void test() {
		
		
		
		
		Session session = HibernateSessionFactory.getSession();
		String queryString = "from ActivityModel";
		List result;
		ArrayList<ActivityModel> users = new ArrayList<ActivityModel>();
		
		try {
			session.beginTransaction();		
			Query query = session.createQuery(queryString);
			result = query.list();
			session.getTransaction().commit();
			Iterator it  = result.iterator();
			while (it.hasNext()) {
				users.add((ActivityModel)it.next());			
			}
			
			for (ActivityModel userModel : users) {
				System.out.println(userModel);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		fail("Not yet implemented");
	}

}
