package com.tongji.java.test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.junit.Test;

import com.tongji.persistence.ActivityDAO;
import com.tongji.persistence.HibernateSessionFactory;
import com.tongji.persistence.models.ActivityModel;
import com.tongji.persistence.models.UserEventRelation;

public class TestHashSetContains {
	@Test
	public void Test() {

		List<UserEventRelation> list = new LinkedList<UserEventRelation>();
		Set<UserEventRelation> set = new HashSet<UserEventRelation>();
		// for (int i = 0; i < 10; i++) {
		// UserEventRelation temp = new UserEventRelation();
		// temp.setRelatedUserAccount("user" + i);
		// list.add(temp);
		// UserEventRelation temp2 = new UserEventRelation();
		// temp2.setRelatedUserAccount("user"+ 2*i);
		// set.add(temp2);
		// }
		// Iterator<UserEventRelation> it = list.iterator();

		UserEventRelation user1 = new UserEventRelation();
		UserEventRelation user2 = new UserEventRelation();
		UserEventRelation user3 = new UserEventRelation();
		UserEventRelation user4 = new UserEventRelation();
		user1.setRelatedUserAccount("092927");
		user2.setRelatedUserAccount("092926");
		user3.setRelatedUserAccount("test1");
		user4.setRelatedUserAccount("org1");
		
		user1.setRelatedActivityId(24);
		user2.setRelatedActivityId(24);
		user3.setRelatedActivityId(24);
		user4.setRelatedActivityId(24);

		list.add(user1);
		list.add(user2);
		list.add(user3);
		list.add(user4);

		Session session = HibernateSessionFactory.getSession();
		try {

			session.beginTransaction();
			ActivityModel activity = new ActivityModel();
			session.load(activity, 24);
			Hibernate.initialize(activity.getRelatedUsers());
			set = activity.getRelatedUsers();

			session.getTransaction().commit();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			session.close();
		}

		Iterator<UserEventRelation> it = list.iterator();

		while (it.hasNext()) {
			UserEventRelation temp = it.next();
			if (set.contains(temp)) {
				it.remove();
			}
		}

		it = list.iterator();
		while (it.hasNext()) {
			System.out.println(it.next().getRelatedUserAccount());
		}

	}
}
