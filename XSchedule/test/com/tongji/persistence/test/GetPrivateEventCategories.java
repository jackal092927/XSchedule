package com.tongji.persistence.test;

import java.util.List;

import org.hibernate.Session;
import org.junit.Test;

import com.tongji.persistence.HibernateSessionFactory;
import com.tongji.persistence.models.UserEventClassifyModel;
import com.tongji.persistence.models.UserModel;

public class GetPrivateEventCategories {
	@Test
	public void Test() {
		Session session = HibernateSessionFactory.getSession();
		UserModel user = new UserModel();
		session.load(user, "092927");
		List<UserEventClassifyModel> categoryList = user.getClassifications();
		System.out.println(categoryList.size());
		for (UserEventClassifyModel privateEventCategoryModel : categoryList) {
			System.out.println(privateEventCategoryModel);
		}
		
		session.close();
	}
}
