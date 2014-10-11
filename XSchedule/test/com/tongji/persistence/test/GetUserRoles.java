package com.tongji.persistence.test;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.Session;
import org.junit.Test;

import com.tongji.persistence.HibernateSessionFactory;
import com.tongji.persistence.models.ActivityModel;
import com.tongji.persistence.models.RoleModel;
import com.tongji.persistence.models.UserModel;

public class GetUserRoles {

	@Test
	public void test() {
		
		Session session = HibernateSessionFactory.getSession();
		try {
			
			UserModel user = (UserModel) session
					.load(UserModel.class, "092927");
			Set<RoleModel> roles = user.getRoles();
			for (RoleModel roleModel : roles) {
				System.out.println(roleModel.getRoleName());
			}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally{
			session.close();
		}
		
		fail("Not yet implemented");
	}

}
