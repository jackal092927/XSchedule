package com.tongji.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.constraints.Null;

import org.apache.commons.io.filefilter.FalseFileFilter;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.object.SqlQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;
import com.sun.org.apache.bcel.internal.generic.IF_ICMPNE;
import com.tongji.persistence.models.ActivityModel;
import com.tongji.persistence.models.CourseModel;
import com.tongji.persistence.models.UserEventClassifyModel;
import com.tongji.persistence.models.UserEventRelation;
import com.tongji.persistence.models.UserModel;
import com.tongji.share.tools.ModelsUtil;

@Repository("userDAO")
public class UserDAO implements IUserDAO, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8129676840919494817L;
	private SessionFactory sessionFactory;

	/**
	 * Add User
	 * 
	 * @param User
	 *            user
	 */
	@Override
	@Transactional
	public void store(UserModel user) {
		getSessionFactory().getCurrentSession().saveOrUpdate(user);
	}

	/**
	 * Delete User
	 * 
	 * @param User
	 *            user
	 */
	@Override
	@Transactional
	public void delete(UserModel user) {
		getSessionFactory().getCurrentSession().delete(user);
	}

	/**
	 * Delete User by Id(account)
	 * 
	 * @param String
	 *            account
	 */
	@Transactional
	public void deletebyId(String account) {
		delete(getById(account));
	}

	/**
	 * Get User
	 * 
	 * @param int User Id
	 * @return User
	 */
	@Override
	@Transactional(readOnly = true)
	public UserModel getById(String account) {
		// List list = getSessionFactory().getCurrentSession()
		// .createQuery("from UserModel where account=?")
		// .setParameter(0, account).list();
		return (UserModel) getSessionFactory().getCurrentSession().get(
				UserModel.class, account);
	}

	/**
	 * Get User List
	 * 
	 * @return List - User list
	 */
	@Override
	@Transactional(readOnly = true)
	public List<UserModel> getAll() {
		return getSessionFactory().getCurrentSession()
				.createQuery("from UserModel").list();

	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	@Transactional
	public Set<ActivityModel> getUserActivitiesById(String account) {
		UserModel user = (UserModel) getSessionFactory().getCurrentSession()
				.load(UserModel.class, account);
		Hibernate.initialize(user.getActivities());
		Set<ActivityModel> activitySet = user.getActivities();
		return activitySet;

	}

	@Override
	@Transactional(readOnly = true)
	public Set<CourseModel> getUserCoursesById(String account) {
		UserModel user = (UserModel) getSessionFactory().getCurrentSession()
				.load(UserModel.class, account);
		Hibernate.initialize(user.getCourses());
		Set<CourseModel> courseSet = user.getCourses();
		return courseSet;
	}

	@Override
	@Transactional(readOnly = true)
	public Set<UserEventRelation> getRelatedActivitiesById(String account) {
		UserModel user = (UserModel) getSessionFactory().getCurrentSession()
				.load(UserModel.class, account);
		Hibernate.initialize(user.getRelatedActivities());
		Set<UserEventRelation> activitySet = user.getRelatedActivities();
		return activitySet;
	}

	@Override
	@Transactional(readOnly = true)
	public Set<UserModel> getFollowersByUserAccount(String userAccount) {
		UserModel user = (UserModel) getSessionFactory().getCurrentSession()
				.load(UserModel.class, userAccount);
		Hibernate.initialize(user.getFollowers());
		return user.getFollowers();

	}

	@Override
	@Transactional(readOnly = true)
	public Set<UserModel> getTargetUsersByFollowerAccount(String followerAccount) {

		UserModel user = ((UserModel) getSessionFactory().getCurrentSession()
				.get(UserModel.class, followerAccount));
		Hibernate.initialize(user.getFollowTargets());
		Set<UserModel> set = user.getFollowTargets();
		return set;
	}

	@Override
	@Transactional
	public int addFollower(String user, String follower) {
		String sqlString = "insert into xcheduledb.user_follower(user_account, follower_account) values(?,?)";
		Object[] values = { user, follower };
		int result = HibernateSQLQueryUtils.updateBySQL(getSessionFactory()
				.getCurrentSession(), sqlString, values);
		return result;
	}

	@Override
	@Transactional
	public int followerAddTargetuser(String follower, String targetuser) {
		String sqlString = "insert into xcheduledb.user_follower(user_account, follower_account) values(?,?)";
		Object[] values = { targetuser, follower };
		int result = HibernateSQLQueryUtils.updateBySQL(getSessionFactory()
				.getCurrentSession(), sqlString, values);
		return result;
	}

	@Override
	@Transactional
	public void storeUserEventRelation(UserEventRelation userEvent) {
		ModelsUtil.ModelConstraintCheck(userEvent);
		getSessionFactory().getCurrentSession().saveOrUpdate(userEvent);
	}

	@Override
	@Transactional
	public void removeUserEventRelation(UserEventRelation userEvent) {
		getSessionFactory().getCurrentSession().delete(userEvent);
	}

	@Override
	@Transactional(readOnly = true)
	public ArrayList<Map<String, String>> getPublicEventsCountGroupByOwner(
			String account, Date startTime, Date endTime) {
		ArrayList<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		Session session = getSessionFactory().getCurrentSession();
		String queryString = "select activity.ownerAccount, count(activity) "
				+ "from ActivityModel activity "
				+ "where activity.publicity = 1 " + "group by activity.owner";
		List qureyList = session.createQuery(queryString).list();
		resultList = querylistToMaplist(qureyList);
		return resultList;
	}

	@Override
	@Transactional(readOnly = true)
	public ArrayList<Map<String, String>> getPrivateEventsCountGroupByOwnercategory(
			String account, Date startTime, Date endTime) {
		ArrayList<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		Session session = getSessionFactory().getCurrentSession();
		String queryString = "select activity.ownerCategory, count(activity.ownerAccount) "
				+ "from ActivityModel activity "
				+ "where activity.ownerAccount = ? "
				+ "group by activity.ownerCategory";
		List queryList = session.createQuery(queryString).setString(0, account)
				.list();
		resultList = querylistToMaplist(queryList);
		return resultList;
	}

	@Override
	@Transactional(readOnly = true)
	public ArrayList<Map<String, String>> getFriendEventsCountGroupByOwner(
			String account, Date startTime, Date endTime) {
		ArrayList<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		Session session = getSessionFactory().getCurrentSession();
		String queryString = "select userEvent.relatedActivity.ownerAccount, count(userEvent) "
				+ "from UserEventRelation userEvent "
				+ "where userEvent.relatedUserAccount = ? and userEvent.eventRelatedType = 'FRIEND'"
				+ "group by userEvent.relatedActivity.ownerAccount";
		List queryList = session.createQuery(queryString).setString(0, account)
				.list();

		resultList = querylistToMaplist(queryList);

		return resultList;
	}

	@Override
	@Transactional(readOnly = true)
	public List getPublicEvents(String account, Date startTime, Date endTime) {
		List list = new ArrayList<ActivityModel>();
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ActivityModel.class);
		criteria.add(Restrictions.eq("publicity", 1));
		if (startTime != null) {
			criteria.add(Restrictions.ge("beginTime", startTime));
		}
		if (endTime != null) {
			criteria.add(Restrictions.le("endTime", endTime));
		}
		return criteria.list();
	}

	@Override
	@Transactional(readOnly = true)
	public List getPrivateEvents(String account, Date startTime, Date endTime) {
		// TODO: time problem
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ActivityModel.class);
		criteria.add(Restrictions.eq("ownerAccount", account));
		if (startTime != null) {
			criteria.add(Restrictions.ge("beginTime", startTime));
		}
		if (endTime != null) {
			criteria.add(Restrictions.le("endTime", endTime));
		}

		return criteria.list();
		//
		// UserModel user = new UserModel();
		// session.load(user, account);
		// Set<ActivityModel> set = user.getActivities();
		// List list = new ArrayList<ActivityModel>();
		// for (ActivityModel activity : set) {
		// list.add(activity);
		// }
		// return list;
	}

	@Override
	@Transactional(readOnly = true)
	public List getFriendEvents(String account, Date startTime, Date endTime) {
		// TODO:can be optimized;
		Session session = getSessionFactory().getCurrentSession();
		UserModel user = new UserModel();
		session.load(user, account);
		Set<UserEventRelation> set = user.getRelatedActivities();
		List<UserEventRelation> list = new ArrayList<UserEventRelation>();

		for (UserEventRelation userEventRelation : set) {
			boolean flag = true;

			String userEventRelatedType = userEventRelation
					.getEventRelatedType();
			if (userEventRelatedType == null
					|| !userEventRelatedType.equals("FRIEND")) {
				flag = false;
			}
			ActivityModel activityModel = userEventRelation
					.getRelatedActivity();
			if (flag && startTime != null) {
				if (activityModel.getBeginTime().before(startTime)) {
					flag = false;
				}
			}
			if (flag && endTime != null) {
				if (activityModel.getEndTime().after(endTime)) {
					flag = false;
				}
			}
			if (flag) {
				list.add(userEventRelation);
			}
		}
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public List getRelatedEvents(String account, Date startTime, Date endTime) {
		Session session = getSessionFactory().getCurrentSession();
		UserModel user = new UserModel();
		session.load(user, account);
		Set<UserEventRelation> set = user.getRelatedActivities();
		List<UserEventRelation> list = new ArrayList<UserEventRelation>();
		for (UserEventRelation userEventRelation : set) {
			Hibernate.initialize(userEventRelation.getRelatedActivity());
			list.add(userEventRelation);
		}
		return list;
		// List<ActivityModel> list = new ArrayList<ActivityModel>();
		// for (UserEventRelation relatedActivity : set) {
		// list.add(relatedActivity.getRelatedActivity());
		// }
		// return list;
	}

	private ArrayList<Map<String, String>> querylistToMaplist(List queryList) {
		ArrayList<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
		Iterator it = queryList.iterator();
		while (it.hasNext()) {
			Object[] row = (Object[]) it.next();
			Map<String, String> map = new HashMap<String, String>();
			if (row[0] == null || ((String) row[0]).length() < 1) {
				map.put("title", "[unclassified]");
			} else {
				map.put("title", (String) row[0]);
			}
			map.put("count", String.valueOf(row[1]));
			mapList.add(map);
		}

		return mapList;
	}

	private ArrayList<Map<String, String>> querylistToMaplist(List queryList,
			List<String> keyList) {
		ArrayList<Map<String, String>> mapList = new ArrayList<Map<String, String>>();

		return mapList;
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserEventClassifyModel> getClassificationsByUseraccount(
			String account) {
		List<UserEventClassifyModel> classifications = new ArrayList<UserEventClassifyModel>();
		Session session = getSessionFactory().getCurrentSession();
		UserModel user = new UserModel();
		session.load(user, account);
		Hibernate.initialize(user.getClassifications());
		classifications = user.getClassifications();
		return classifications;
	}

	@Override
	@Transactional
	public UserEventRelation getUserEventRelation(String userAccount,
			int activityId) {
		Session session = getSessionFactory().getCurrentSession();
		String queryString = "from UserEventRelation where relatedUserAccount=:userAccount and relatedActivityId=:activityId";
		List result = session.createQuery(queryString)
				.setString("userAccount", userAccount)
				.setInteger("activityId", activityId).setMaxResults(1).list();
		if (result.size() == 0) {
			return null;
		} else {
			return (UserEventRelation) result.get(0);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public int getPinEventCount(String account) {
		Session session = getSessionFactory().getCurrentSession();
		String queryString = "select count(relatedUserAccount) from UserEventRelation where relatedUserAccount=:account and pin = true";
		List result = session.createQuery(queryString)
				.setString("account", account).list();
		return ((Long) result.get(0)).intValue();
	}

	@Override
	@Transactional(readOnly = true)
	public int getJoinEventCount(String account) {
		Session session = getSessionFactory().getCurrentSession();
		String queryString = "select count(relatedUserAccount) from UserEventRelation where relatedUserAccount=:account and joinin = true";
		List result = session.createQuery(queryString)
				.setString("account", account).list();
		return ((Long) result.get(0)).intValue();
	}

	@Override
	@Transactional
	public void pinEvent(String account, Integer eventId, boolean pin) {
		Session session = getSessionFactory().getCurrentSession();
		UserEventRelation userEventRelation = getUserEventRelation(account,
				eventId);
		if (userEventRelation != null) {
			userEventRelation.setPin(pin);
			session.saveOrUpdate(userEventRelation);
		} else {
			userEventRelation = new UserEventRelation();
			userEventRelation.setRelatedUserAccount(account);
			userEventRelation.setRelatedActivityId(eventId);
			userEventRelation.setPin(pin);
			session.saveOrUpdate(userEventRelation);
		}

	}

	@Override
	@Transactional
	public void updateEventPin(String account, Integer eventId, boolean join) {
		Session session = getSessionFactory().getCurrentSession();
		// UserEventRelation userEventRelation = getUserEventRelation(account,
		// eventId);
		UserEventRelation userEventRelation;
		String queryString = "from UserEventRelation where relatedUserAccount=:userAccount and relatedActivityId=:activityId";
		List result = session.createQuery(queryString)
				.setString("userAccount", account)
				.setInteger("activityId", eventId).setMaxResults(1).list();
		if (result.size() == 0) {
			userEventRelation = new UserEventRelation();
			userEventRelation.setRelatedUserAccount(account);
			userEventRelation.setRelatedActivityId(eventId);
			userEventRelation.setJoinin(join);
			session.saveOrUpdate(userEventRelation);
			session.flush();
		} else {
			userEventRelation = (UserEventRelation) result.get(0);
			userEventRelation.setJoinin(join);
			session.saveOrUpdate(userEventRelation);
			session.flush();
		}
	}

	/**
	 * 
	 * @param grade
	 * @param major
	 * @param pageNo
	 * @param pageSize
	 * @return List<Object>result null:pageNo error; empty: no result;
	 */
	@Override
	@Transactional(readOnly = true)
	public List getUsers(String grade, String major, int pageNo, int pageSize) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(UserModel.class);
		if (grade != null && grade.length() > 0) {
			criteria.add(Restrictions.eq("grade", grade));
		}
		if (major != null && major.length() > 0) {
			criteria.add(Restrictions.eq("major", major));
		}
		if (pageNo != 0) {
			criteria.setFirstResult((pageNo - 1) * pageSize);
		}
		if (pageSize != 0) {
			criteria.setMaxResults(pageSize);
		}
		List result = null;
		result = criteria.list();
		return result;

	}

	@Override
	@Transactional
	public void updatePassword(String account, String password) {
		Session session = getSessionFactory().getCurrentSession();
		String queryString = "update  UserModel set password=:password where acount=:account";
		Query query = session.createQuery(queryString);
		query.setString("password", password);
		query.setString("account", account);
		query.executeUpdate();

	}

	@Override
	@Transactional(readOnly = true)
	public boolean ifFriends(String accountA, String accountB) {
		Session session = getSessionFactory().getCurrentSession();
		String queryString = "select friend from user_follower where user_account=:accountA and follower_account=:accountB and friend=true";
		SQLQuery sqlQuery = session.createSQLQuery(queryString);
		sqlQuery.setString("accountA", accountA);
		sqlQuery.setString("accountB", accountB);
		return sqlQuery.list().size() > 0;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean ifFollowed(String accountA, String accountB) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		String queryString = "select friend from user_follower where user_account=:accountA and follower_account=:accountB";
		SQLQuery sqlQuery = session.createSQLQuery(queryString);
		sqlQuery.setString("accountA", accountA);
		sqlQuery.setString("accountB", accountB);
		return sqlQuery.list().size() > 0;
	}

	@Override
	@Transactional
	public void aFollowb(String accountA, String accountB) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		boolean friend = ifFollowed(accountB, accountA);
		if (friend) {
			updateFriend(accountB, accountA, true);
		}
		String queryString = "insert into user_follower(user_account, follower_account, friend) "
				+ "values(?, ?, ?)";
		SQLQuery sqlQuery = session.createSQLQuery(queryString);
		sqlQuery.setString(0, accountA);
		sqlQuery.setString(1, accountB);
		sqlQuery.setBoolean(2, friend);
		sqlQuery.executeUpdate();
	}

	@Override
	@Transactional
	public void updateFriend(String accountA, String accountB, boolean friend) {
		Session session = getSessionFactory().getCurrentSession();
		String queryString = "update user_follower set friend=:friend "
				+ "where user_account=:accountA and follower_account=:accountB";
		SQLQuery sqlQuery = session.createSQLQuery(queryString);
		sqlQuery.setBoolean("friend", friend);
		sqlQuery.setString("accountA", accountA);
		sqlQuery.setString("accountB", accountB);
		sqlQuery.executeUpdate();
	}

	@Override
	@Transactional
	public void aNotFollowb(String accountA, String accountB) {
		Session session = getSessionFactory().getCurrentSession();
		boolean friend = ifFriends(accountB, accountA);
		if (friend) {
			updateFriend(accountB, accountA, false);
		}
		String queryString = "delete from user_follower " +
				"where user_account=:accountA and follower_account=:accountB";
		SQLQuery sqlQuery = session.createSQLQuery(queryString);
		sqlQuery.setString("accountA", accountA);
		sqlQuery.setString("accountB", accountB);
		sqlQuery.executeUpdate();
	}
}
