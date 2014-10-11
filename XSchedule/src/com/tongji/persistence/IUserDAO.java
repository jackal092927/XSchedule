package com.tongji.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tongji.persistence.models.ActivityModel;
import com.tongji.persistence.models.CourseModel;
import com.tongji.persistence.models.UserEventClassifyModel;
import com.tongji.persistence.models.UserEventRelation;
import com.tongji.persistence.models.UserModel;

public interface IUserDAO {

	/**
	 * Add User
	 * 
	 * @param User
	 *            user
	 */
	public void store(UserModel userModel);

	/**
	 * Delete User
	 * 
	 * @param User
	 *            user
	 */
	public void delete(UserModel userModel);

	/**
	 * Get User
	 * 
	 * @param String
	 *            accountString
	 */
	public UserModel getById(String accountString);

	/**
	 * Get User List
	 * 
	 */
	public List<UserModel> getAll();

	public Set<ActivityModel> getUserActivitiesById(String account);

	public Set<CourseModel> getUserCoursesById(String account);

	void deletebyId(String account);

	public Set<UserEventRelation> getRelatedActivitiesById(String account);

	public Set<UserModel> getFollowersByUserAccount(String userAccount);

	public Set<UserModel> getTargetUsersByFollowerAccount(String followerAccount);

	public int addFollower(String user, String follower);

	// TODO:...it seems that no needs for two methods...
	public int followerAddTargetuser(String follower, String targetuser);

	public void storeUserEventRelation(UserEventRelation userEvent);

	public void removeUserEventRelation(UserEventRelation userEvent);

	public ArrayList<Map<String, String>> getPublicEventsCountGroupByOwner(
			String account, Date startTime, Date endTime);

	public List getPublicEvents(String account, Date startTime, Date endTime);

	public ArrayList<Map<String, String>> getPrivateEventsCountGroupByOwnercategory(
			String account, Date startTime, Date endTime);

	public List getPrivateEvents(String account, Date startTime, Date endTime);

	public ArrayList<Map<String, String>> getFriendEventsCountGroupByOwner(
			String account, Date startTime, Date endTime);

	public List getFriendEvents(String account, Date startTime, Date endTime);

	public List<UserEventClassifyModel> getClassificationsByUseraccount(
			String account);

	public UserEventRelation getUserEventRelation(String userAccount,
			int activityId);

	public List getRelatedEvents(String account, Date startTime, Date endTime);

	public int getPinEventCount(String account);

	public int getJoinEventCount(String account);

	public void pinEvent(String account, Integer eventId, boolean pin);
	
	public void updateEventPin(String account, Integer eventId, boolean join);

	public List getUsers(String grade, String major, int pageNo, int pageSize);

	public void updatePassword(String account, String password);

	public boolean ifFriends(String accountA, String accountB);

	public boolean ifFollowed(String accountA, String accountB);

	public void aFollowb(String accountA, String accountB);

	public void aNotFollowb(String accountA, String accountB);

	public void updateFriend(String accountA, String accountB, boolean friend);
}
