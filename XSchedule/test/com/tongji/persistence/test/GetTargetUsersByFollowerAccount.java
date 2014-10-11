package com.tongji.persistence.test;

import java.util.Set;

import org.hibernate.Session;
import org.junit.Test;

import com.tongji.persistence.HibernateSessionFactory;
import com.tongji.persistence.models.UserModel;

public class GetTargetUsersByFollowerAccount {
	@Test
	public void Test(){
		Session session = HibernateSessionFactory.getSession()	;
		Set<UserModel> set = ((UserModel)session.load(UserModel.class, "092927")).getFollowTargets();
		for (UserModel userModel : set) {
			System.out.println(userModel	);
			
		}
	}
}
