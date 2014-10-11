package com.tongji.persistence.test;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Test;

import com.tongji.persistence.HibernateSessionFactory;
import com.tongji.persistence.models.UserModel;

public class ShowUsers {

	@Test
	public void test() {
		Session session = HibernateSessionFactory.getSession();
		String queryString = "from UserModel";
		List result;
		ArrayList<UserModel> users = new ArrayList<UserModel>();
		
		try {
			session.beginTransaction();		
			Query query = session.createQuery(queryString);
			result = query.list();
			session.getTransaction().commit();
			Iterator it  = result.iterator();
			while (it.hasNext()) {
				users.add((UserModel)it.next());			
			}
			
			for (UserModel userModel : users) {
				System.out.println(userModel);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		fail("Not yet implemented");
	}

}
