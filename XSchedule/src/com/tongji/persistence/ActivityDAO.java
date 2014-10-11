package com.tongji.persistence;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tongji.persistence.models.ActivityModel;
import com.tongji.persistence.models.CommentModel;
import com.tongji.persistence.models.UserEventRelation;
import com.tongji.share.tools.ModelsUtil;
import com.tongji.share.view.models.TransferItem;

@Repository("activityDAO")
public class ActivityDAO implements IActivityDAO, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3842894527203375553L;
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	@Transactional
	public int store(ActivityModel activityModel) {
		ModelsUtil.ModelConstraintCheck(activityModel);
		getSessionFactory().getCurrentSession().saveOrUpdate(activityModel);
		return activityModel.getId();
	}

	@Override
	@Transactional
	public void delete(ActivityModel activityModel) {
		getSessionFactory().getCurrentSession().delete(activityModel);
	}

	@Override
	@Transactional
	public void deleteById(int id) {
		delete(getById(id));
	}

	@Override
	@Transactional(readOnly = true)
	public ActivityModel getById(int id) {
		return (ActivityModel) getSessionFactory().getCurrentSession().get(
				ActivityModel.class, id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActivityModel> getAll() {
		return getSessionFactory().getCurrentSession()
				.createQuery("from ActivityModel").list();
	}

	@Override
	@Transactional(readOnly = true)
	public Set<UserEventRelation> getRelatedUsers(int activityId) {
		Session session = getSessionFactory().getCurrentSession();
		ActivityModel activity = new ActivityModel();
		session.load(activity, activityId);
		Hibernate.initialize(activity.getRelatedUsers());
		return activity.getRelatedUsers();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActivityModel> getPublicActivities() {
		Session session = getSessionFactory().getCurrentSession();
		Query query = session
				.createQuery("from ActivityModel as activity where activity.publicity = :publicity");
		query.setInteger("publicity", 1);
		return query.list();
	}

	@Override
	@Transactional
	public void storeEventWithRelatedUsers(ActivityModel event,
			List<UserEventRelation> relatedUsers) {
		Session session = getSessionFactory().getCurrentSession();
		// session.saveOrUpdate(event);
		store(event);
		session.flush();
		int activityId = event.getId();
		for (UserEventRelation userEventRelation : relatedUsers) {
			//
			ModelsUtil.ModelConstraintCheck(userEventRelation);
			//
			userEventRelation.setRelatedActivityId(activityId);
			event.getRelatedUsers().add(userEventRelation);
		}
		session.flush();
	}

	@Override
	@Transactional
	public void storeEventWithRelatedUsersList(ActivityModel event,
			List<TransferItem> diffList) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		// session.saveOrUpdate(event);
		store(event);
		session.flush();
		int activityId = event.getId();
		for (TransferItem transferItem : diffList) {
			if (transferItem.isAdd()) {
				UserEventRelation userEventRelation = (UserEventRelation) transferItem
						.getItem();
				userEventRelation.setRelatedActivityId(activityId);
				//
				ModelsUtil.ModelConstraintCheck(userEventRelation);
				//
				event.getRelatedUsers().add(userEventRelation);
			} else {
				UserEventRelation userEventRelation = (UserEventRelation) transferItem
						.getItem();
				session.load(userEventRelation, userEventRelation.getId());
				session.delete(userEventRelation);
			}
		}
		session.flush();
	}

	@Override
	@Transactional(readOnly = true)
	public List getRelatedCommentsByActivityId(int id) {
		Session session = getSessionFactory().getCurrentSession();
		ActivityModel activity = new ActivityModel();
		session.load(activity, id);
		Hibernate.initialize(activity.getComments());
		return ModelsUtil.setToList(activity.getComments());
	}

	@Override
	@Transactional
	public int storeComment(CommentModel comment) {
		Session session = getSessionFactory().getCurrentSession();
		session.saveOrUpdate(comment);
		return comment.getId();
	}

	@Override
	@Transactional
	public void deleteComment(int id) {
		Session session = getSessionFactory().getCurrentSession();
		session.delete(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActivityModel> getLimit(int limit, String orderByString) {
		if (!"publish_time".equals(orderByString)) {
			// order conditions
			orderByString = "publish_time";
			//
		}

		Session session = getSessionFactory().getCurrentSession();

		String queryString = "from ActivityModel order by :order asc";

		return session.createQuery(queryString)
				.setString("order", orderByString).setMaxResults(limit).list();

	}

	@Override
	@Transactional(readOnly = true)
	public List getLimitGeDate(Date date) {
		// TODO:
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public List getLimitWithSyncmark(long syncMark) {
		Timestamp timestamp = new Timestamp(syncMark);
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ActivityModel.class);
		criteria.add(Restrictions.ge("lastUpdateTime", timestamp));
		return criteria.list();
	}

	@Override
	@Transactional
	public void updateEvent(ActivityModel event) {
		ActivityModel oriModel = new ActivityModel();
		Session session = getSessionFactory().getCurrentSession();
		session.load(oriModel, event.getId());
		oriModel.setId(event.getId());
		oriModel.setBeginTime(event.getBeginTime());
		oriModel.setEndTime(event.getEndTime());
		oriModel.setTitle(event.getTitle());
		oriModel.setLocation(event.getLocation());
		oriModel.setPublicity(event.getPublicity());
		oriModel.setValid(event.isValid());
		ModelsUtil.ModelConstraintCheck(oriModel);
		session.update(oriModel);
	}

	@Override
	@Transactional(readOnly = true)
	public List getpublicEventsWithSyncmark(long syncMark) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ActivityModel.class);
		criteria.add(Restrictions.eq("publicity", 1));
		if (syncMark > 0) {
			criteria.add(Restrictions.ge("lastUpdateTime", syncMark));
		}

		return criteria.list();
	}

	@Override
	@Transactional(readOnly = true)
	public List getRelatedEventsWithSyncmark(String account, long syncMark) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public List getPrivateEventsWithSyncmark(String account, long syncMark) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ActivityModel.class);
		criteria.add(Restrictions.eq("ownerAccount", account));
		if (syncMark > 0) {
			criteria.add(Restrictions.ge("lastUpdateTime", syncMark));
		}

		return criteria.list();
	}

	@Override
	@Transactional
	public UserEventRelation getUserEventRelation(String account, int eventId) {
		UserEventRelation userEventRelation = null;
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(UserEventRelation.class);
		criteria.add(Restrictions.eq("relatedUserAccount", account));
		criteria.add(Restrictions.eq("relatedActivityId", eventId));
		List result = criteria.setMaxResults(1).list();
		if (result.size() > 0) {
			userEventRelation = (UserEventRelation) result.get(0);
		}
		return userEventRelation;
	}

	@Override
	@Transactional
	public void storeUserEventRelation(UserEventRelation userEventRelation) {
		Session session = getSessionFactory().getCurrentSession();
		UserEventRelation userEventRelation2 = getUserEventRelation(userEventRelation.getRelatedUserAccount(),
				userEventRelation.getRelatedActivityId());
		if (userEventRelation2 == null) {
			session.save(userEventRelation);
		}else {
			userEventRelation2.setRelatedCategory(userEventRelation.getRelatedCategory());
			session.update(userEventRelation2);
		}
	}

}
