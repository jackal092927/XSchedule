package com.tongji.persistence;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tongji.persistence.models.CommentModel;

@Repository("commentDAO")
public class CommentDAO implements ICommentDAO{
	
	private SessionFactory sessionFactory;

	@Override
	@Transactional
	public void store(CommentModel commentModel) {
		getSessionFactory().getCurrentSession().saveOrUpdate(commentModel);
	}

	@Override
	@Transactional
	public void delete(CommentModel commentModel) {
		getSessionFactory().getCurrentSession().delete(commentModel);
	}

	@Override
	@Transactional(readOnly=true)
	public CommentModel getById(int id) {
		return (CommentModel) getSessionFactory().getCurrentSession().get(CommentModel.class, id);
	}

	@Override
	@Transactional(readOnly=true)
	public List<CommentModel> getAll() {
		 return getSessionFactory().getCurrentSession().createQuery("from CommentModel").list();
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
