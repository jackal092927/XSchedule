package com.tongji.persistence.test;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;
import org.junit.Test;

import com.tongji.persistence.HibernateSessionFactory;
import com.tongji.persistence.models.ActivityModel;
import com.tongji.persistence.models.RoleModel;
import com.tongji.persistence.models.UserModel;

public class SaveUserRoles {

	@Test
	public void test() {
		Session session = HibernateSessionFactory.getSession();

		try {
			session.beginTransaction();
			RoleModel role = (RoleModel)session.load(RoleModel.class, 1);
			RoleModel role2 = (RoleModel)session.load(RoleModel.class, 2);
			
			Set<RoleModel> roles = new HashSet<RoleModel>();
			roles.add(role);
			roles.add(role2);
			
			UserModel user = (UserModel) session
					.load(UserModel.class, "092927");
			user.setRoles(roles);
			
			session.saveOrUpdate(user);
			
			session.getTransaction().commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			session.close();
		}

		fail("Not yet implemented");
	}

}
