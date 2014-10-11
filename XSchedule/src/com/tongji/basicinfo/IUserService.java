package com.tongji.basicinfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.TreeNode;
import com.tongji.persistence.models.ActivityModel;
import com.tongji.persistence.models.CourseModel;
import com.tongji.persistence.models.UserEventClassifyModel;
import com.tongji.persistence.models.UserEventRelation;
import com.tongji.persistence.models.UserModel;
import com.tongji.share.tools.MyEnum.EventRelatedType;
import com.tongji.share.view.MyTreeNode;

public interface IUserService {

	public void addUser(UserModel user);
	
	public void updateUser(UserModel user);
    /**
     * Delete User
     *
     * @param  User user
     */
    public void deleteUser(UserModel user);
    /**
     * Get User
     *
     * @param  String account
     */
    public UserModel getUserById(String account);
    /**
     * Get User and his eventSet(activities and courses)
     * 
     * @param String account
     * @return UserModel with Sets
     */
    public UserModel getUserWithEventsById(String account);
    /**
     * Get User List
     *
     * @return List - User list
     */
    public List<UserModel> getUsers();

	public boolean validateUser(UserModel user);
	
	public Set<ActivityModel> getUserActivities(String account);
	
	public Set<CourseModel> getUserCourses(String account);

	public int followerAddTargetuser(String follower, String targetuser);
	
	public int userAddFollower(String user, String follower);
	
	public Set<UserModel> userGetFollowers(String user);
	
	public Set<UserModel> followerGetTargetusers(String follower);

	public void storeUserEventRelation(UserEventRelation userEvent);

	public void removeUserEventRelation(UserEventRelation userEvent);
	
	public TreeNode getPublicEventsRoot(MyTreeNode baseNode, String account, Date startTime, Date endTime);
	
	public TreeNode getPrivateEventsRoot(MyTreeNode baseNode,String account, Date startTime, Date endTime);
	
	public TreeNode getFriendsEventsRoot(MyTreeNode baseNode,String account, Date startTime, Date endTime);

	public List<UserEventClassifyModel> getEventClassifyModel(String account);

	public List<UserModel> getFriends(String account);

	public List<UserModel> getFollowers(String account);

	public List<UserModel> getFollowTargets(String account);

	public void storeUser(UserModel userModel);

	public int getPinEventCount(String account);

	public int getJoinEventCount(String account);

	public void pinEvent(String account, Integer eventId, boolean pin);
	
	public void joinEvent(String account, Integer eventId, boolean join);
	
	public List getUsers(String grade, String major, int pageNo, int pageSize);

//	public void buildRootNode(MyTreeNode root,
//			ArrayList<Map<String, String>> countList, List events,
//			Map categoryMap, String eventOwnerType);
//	
//	public ArrayList<ScheduleEvent> convertEvents(List eventList,
//			EventRelatedType relatedType, String userAccount);
//
//	public UserEventRelation getUserEventRelation(String userAccount, int activityId);
	
	public void updatePassword(String account, String password);

	public boolean ifFriends(String accountA, String accountB);

	public void aFollowb(String a, String b);

	public boolean ifFollowed(String accountA, String accountB);

	public void aNotFollowb(String accountA, String accountB);

}
