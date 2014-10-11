package com.tongji.persistence;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tongji.persistence.models.CourseModel;

@Repository("courseDAO")
public class CourseDAO implements ICourseDAO, Serializable{
	
	private SessionFactory sessionFactory;

	@Override
	@Transactional
	public void store(CourseModel courseModel) {
		getSessionFactory().getCurrentSession().saveOrUpdate(courseModel);
	}

	@Override
	@Transactional
	public void delete(CourseModel courseModel) {
		getSessionFactory().getCurrentSession().delete(courseModel);
	}
	
	@Override
	@Transactional
	public void deleteById(int id) {
		delete(getById(id));
		
	}

	@Override
	@Transactional(readOnly=true)
	public CourseModel getById(int id) {
		return (CourseModel) getSessionFactory().getCurrentSession().get(CourseModel.class, id);
	}

	@Override
	@Transactional(readOnly=true)
	public List<CourseModel> getAll() {
		 return getSessionFactory().getCurrentSession().createQuery("from CourseModel").list();
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	
}
