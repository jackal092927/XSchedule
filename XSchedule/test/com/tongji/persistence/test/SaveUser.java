package com.tongji.persistence.test;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.Test;

import com.tongji.persistence.HibernateSessionFactory;
import com.tongji.persistence.models.UserModel;

public class SaveUser {

	@Test
	public void test() {
		
//		UserModel userModel = new UserModel();
//		Configuration cfg = new Configuration();
//		cfg.configure();
//
//		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
//				.applySettings(cfg.getProperties()).buildServiceRegistry();
//
//		List list;
//		try {
//			SessionFactory factory = cfg.configure().buildSessionFactory(
//					serviceRegistry);
//
//			Session session = factory.openSession();
//			// Query query = session.createQuery("from UserModel");
//			// list = query.list();
//			// Iterator it2 = list.iterator();
//			// while (it2.hasNext()) {
//			// userModel = (UserModel) it2.next();
//
//			userModel.setAccount("testA");
//			userModel.setPassword("111111");
//			userModel.setUsername("testa");
//			session.save(userModel);
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
		
		
		
		Session session = HibernateSessionFactory.getSession();
		session.beginTransaction();
		UserModel userModel = new UserModel();
		userModel.setAccount("testA");
		userModel.setPassword("111111");
		userModel.setUsername("testa");
		session.save(userModel);
		
		session.getTransaction().commit();
		
		
		fail("Not yet implemented");
	}

}
