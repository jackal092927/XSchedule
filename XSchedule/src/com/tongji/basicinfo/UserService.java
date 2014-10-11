package com.tongji.basicinfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.TreeNode;
import org.springframework.transaction.annotation.Transactional;

import com.tongji.persistence.IUserDAO;
import com.tongji.persistence.models.ActivityModel;
import com.tongji.persistence.models.CourseModel;
import com.tongji.persistence.models.UserEventClassifyModel;
import com.tongji.persistence.models.UserEventRelation;
import com.tongji.persistence.models.UserModel;
import com.tongji.share.tools.ModelsUtil;
import com.tongji.share.tools.MyEnum.EventRelatedType;
import com.tongji.share.view.MyTreeNode;
import com.tongji.share.view.MyTreeNodeData;
import com.tongji.share.view.NavigationScheduleService;

//@Transactional(readOnly = true) keng!die!!!
public class UserService implements IUserService, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4966583344419209306L;
	// UserDAO is injected...
	private IUserDAO userDAO;


	/**
	 * Add User
	 * 
	 * @param User
	 *            user
	 */
	@Transactional(readOnly = false)
	@Override
	public void addUser(UserModel user) {
		getUserDAO().store(user);
	}

	/**
	 * Update User
	 * 
	 * @param User
	 *            user
	 */
	@Transactional(readOnly = false)
	@Override
	public void updateUser(UserModel user) {
		getUserDAO().store(user);
	}

	/**
	 * Delete User
	 * 
	 * @param User
	 *            user
	 */
	@Transactional(readOnly = false)
	@Override
	public void deleteUser(UserModel user) {
		getUserDAO().delete(user);
	}

	/**
	 * Get User
	 * 
	 * @param int User Id
	 */
	@Override
	public UserModel getUserById(String account) {
		return getUserDAO().getById(account);
	}

	/**
	 * Get User List
	 * 
	 */
	@Override
	public List<UserModel> getUsers() {
		return getUserDAO().getAll();
	}

	/**
	 * Get User DAO
	 * 
	 * @return IUserDAO - User DAO
	 */
	public IUserDAO getUserDAO() {
		return userDAO;
	}

	/**
	 * Set User DAO
	 * 
	 * @param IUserDAO
	 *            - User DAO
	 */
	public void setUserDAO(IUserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Override
	public boolean validateUser(UserModel user) {

		UserModel userModel;

		if ((userModel = getUserDAO().getById(user.getAccount())) == null) {
			return false;
		}
		if (!userModel.getPassword().equals(user.getPassword())) {
			return false;
		}

		return true;
	}

	@Override
	public UserModel getUserWithEventsById(String account) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ActivityModel> getUserActivities(String account) {
		return getUserDAO().getUserActivitiesById(account);
	}

	@Override
	public Set<CourseModel> getUserCourses(String account) {
		return getUserDAO().getUserCoursesById(account);
	}

	@Override
	public int followerAddTargetuser(String follower, String targetuser) {
		return userDAO.followerAddTargetuser(follower, targetuser);
	}

	@Override
	public int userAddFollower(String user, String follower) {
		return userDAO.addFollower(user, follower);
	}

	@Override
	public Set<UserModel> userGetFollowers(String user) {
		Set<UserModel> followers = userDAO.getFollowersByUserAccount(user);
		return followers;
	}

	@Override
	public Set<UserModel> followerGetTargetusers(String follower) {
		Set<UserModel> targetUsers = userDAO
				.getTargetUsersByFollowerAccount(follower);
		return targetUsers;
	}

	@Override
	public void storeUserEventRelation(UserEventRelation userEvent) {
		userDAO.storeUserEventRelation(userEvent);
	}

	@Override
	public void removeUserEventRelation(UserEventRelation userEvent) {
		userDAO.removeUserEventRelation(userEvent);
	}

	@Override
	public TreeNode getPublicEventsRoot(MyTreeNode baseRoot, String account,
			Date startTime, Date endTime) {
		// get node title and count group by category;
		ArrayList<Map<String, String>> countList = userDAO
				.getPublicEventsCountGroupByOwner(account, null, null);
		// get event(activity or event or relatedevent) list
		List eventList = userDAO.getPublicEvents(account, startTime, endTime);
		// convert to scheduleEvent List
		List events = convertEvents(eventList, EventRelatedType.PUBLIC, account);
		// construct first level child node;
		MyTreeNode root = new MyTreeNode(new MyTreeNodeData(), null);
		root.setDisplayString("Public");
		root.setParent(baseRoot);
		
		// binding the category name with the data list, for future adding data;
		Map eventsCategoryMap = new HashMap<String, ArrayList<MyScheduleEvent>>();
		// constuct tree;
		buildRootNode(root, countList, events, eventsCategoryMap, "PUBLIC");

		
		return root;
	}

	@Override
	public TreeNode getPrivateEventsRoot(MyTreeNode baseRoot, String account,
			Date startTime, Date endTime) {
		ArrayList<Map<String, String>> countList = userDAO
				.getPrivateEventsCountGroupByOwnercategory(account, startTime,
						endTime);

		List eventList = userDAO.getPrivateEvents(account, startTime, endTime);
		List events = convertEvents(eventList, EventRelatedType.PRIVATE, account);

		MyTreeNode root = new MyTreeNode(new MyTreeNodeData(), null);
		root.setDisplayString("Private");
		root.setParent(baseRoot);
		
		Map eventsCategoryMap = new HashMap<String, ArrayList<MyScheduleEvent>>();

		buildRootNode(root, countList, events, eventsCategoryMap, "PRIVATE");

		
		return root;
	}

	@Override
	public TreeNode getFriendsEventsRoot(MyTreeNode baseRoot, String account,
			Date startTime, Date endTime) {
		ArrayList<Map<String, String>> countList = userDAO
				.getFriendEventsCountGroupByOwner(account, startTime, endTime);

		List<UserEventRelation> eventList = userDAO.getFriendEvents(account,
				startTime, endTime);
		List events = convertEvents(eventList, EventRelatedType.FRIEND, account);

		MyTreeNode root = new MyTreeNode(new MyTreeNodeData(), null);
		root.setDisplayString("Friend");
		root.setParent(baseRoot);

		Map eventsCategoryMap = new HashMap<String, ArrayList<MyScheduleEvent>>();

		buildRootNode(root, countList, events, eventsCategoryMap, "FRIEND");

		
		return root;
	}

	private void buildRootNode(MyTreeNode root,
			ArrayList<Map<String, String>> countList, List events,
			Map categoryMap, String eventOwnerType) {
		for (Map<String, String> map : countList) {
			MyTreeNode node = new MyTreeNode(new MyTreeNodeData(), root);
			node.setDisplayString(map.get("title"));
			node.setCount(Integer.valueOf(map.get("count")));
			root.setCount(root.getCount() + node.getCount());
			categoryMap.put(map.get("title"),
					((MyTreeNodeData) node.getData()).getEventData());
		}

		// Iterator resultIt = countList.iterator();
		// //construct 1st level children of root
		// while (resultIt.hasNext()) {
		// Object[] row = (Object[]) resultIt.next();
		// //create 1st level child of root, named as category;
		// TreeNode node = new MyTreeNode(new MyTreeNodeData((String)row[0]),
		// root);
		// //binding the category name with the data list, for future adding
		// data;
		// categoryMap.put(row[0],
		// ((MyTreeNodeData)node.getData()).getEventData());
		// }
		// put in nodedata:event
		TreeNode pinNode = (TreeNode)root.getParent().getChildren().get(NavigationScheduleService.PIN_INDEX);
		TreeNode joinNode = (TreeNode)root.getParent().getChildren().get(NavigationScheduleService.JOIN_INDEX);
		
		for (Object event : events) {
			MyScheduleEvent scheduleEvent = (MyScheduleEvent) event;
			if (scheduleEvent.isPin()) {
				((MyTreeNodeData)pinNode.getData()).getEventData().add(scheduleEvent);
			}
			if (scheduleEvent.isJoin()) {
				((MyTreeNodeData)joinNode.getData()).getEventData().add(scheduleEvent);
			}
			
			String categoryMapKey = "";
			if (eventOwnerType.equals("PUBLIC") || eventOwnerType.equals("FRIEND")) {
				categoryMapKey = scheduleEvent.getOwner();
			} else if (eventOwnerType.equals("PRIVATE")) {
				categoryMapKey = scheduleEvent.getCategory();
				if (categoryMapKey == null) {
					categoryMapKey = "[unclassified]";
				}
			}
			try {
				List list = (ArrayList<MyScheduleEvent>) categoryMap
						.get(categoryMapKey);
				list.add(scheduleEvent);
			} catch (NullPointerException e) {
				// TODO: handle exception
				e.printStackTrace();
				StringBuffer strBuffer = new StringBuffer();
				strBuffer.append("categoryMapKey: " + categoryMapKey).append(" ");
				strBuffer.append("Event Title: " + scheduleEvent.getEvent().getTitle()).append(" ");
				strBuffer.append("Event Id: " + scheduleEvent.getEvent().getId()).append(" ");
				strBuffer.append("Event OwnerCat: " + scheduleEvent.getEvent().getOwnerCategory()).append(" ");
				strBuffer.append("Event RelatedCategory: " + scheduleEvent.getCategory()).append(" ");
				System.err.println(strBuffer.toString());
			}
			
		}
	}

	private ArrayList<ScheduleEvent> convertEvents(List eventList,
			EventRelatedType relatedType, String userAccount) {
		ArrayList<ScheduleEvent> scheduleEventList = new ArrayList<ScheduleEvent>();
		for (Object object : eventList) {
			MyScheduleEvent scheduleEvent = new MyScheduleEvent();
			// if (event.getClass().equals(ActivityModel.class)) {
			// scheduleEvent = ModelsUtil
			// .ActivityToEvent((ActivityModel) event);
			// }
			// if (event.getClass().equals(UserEventRelation.class)) {
			// scheduleEvent = ModelsUtil
			// .relatedActivityToScheduleEvent((UserEventRelation) event);
			// }
			switch (relatedType) {
			case PUBLIC:
				ActivityModel publicEvent = (ActivityModel) object;
				UserEventRelation publicEventRelation = userDAO
						.getUserEventRelation(userAccount, publicEvent.getId());
				scheduleEvent = ModelsUtil.eventModelToScheduleEvent(
						(ActivityModel) object, publicEventRelation);
				scheduleEvent.setRelatedType(relatedType);
				// scheduleEvent.setCategorySetColor("DEFAULT");
				break;
			case PRIVATE:
				ActivityModel privateEvent = (ActivityModel)object;
				UserEventRelation privateEventRelation = userDAO
						.getUserEventRelation(userAccount, privateEvent.getId());
				scheduleEvent = ModelsUtil
						.eventModelToScheduleEvent(privateEvent, privateEventRelation);
				scheduleEvent.setRelatedType(relatedType);
				scheduleEvent.setEditable(true);
				// scheduleEvent.setCategorySetColor(((ActivityModel)object).getOwnerColorSetting());
				break;
			case FRIEND:
				UserEventRelation relatedEventRelation = (UserEventRelation) object;
				scheduleEvent = ModelsUtil.userEventRelationToScheduleEvent(relatedEventRelation);
				// scheduleEvent.setEvent(event.getRelatedActivity());
				// scheduleEvent.setRelatedType();
				// scheduleEvent.setCategory(event.getOwnerAccount());
				// scheduleEvent.setCategorySetColor("DEFAULT");
				break;

			default:
				break;
			}
			scheduleEventList.add(scheduleEvent);
		}

		return scheduleEventList;
	}

	@Override
	public List<UserEventClassifyModel> getEventClassifyModel(String account) {
		return userDAO.getClassificationsByUseraccount(account);
	}

	@Override
	public List<UserModel> getFriends(String account) {

		return this.getFollowTargets(account);
	}

	@Override
	public List<UserModel> getFollowers(String account) {
		return ModelsUtil.setToList(userDAO.getFollowersByUserAccount(account));
	}

	@Override
	public List<UserModel> getFollowTargets(String account) {
		return ModelsUtil.setToList(userDAO
				.getTargetUsersByFollowerAccount(account));
	}

	@Override
	public void storeUser(UserModel userModel) {
		userDAO.store(userModel);
	}

	private UserEventRelation getUserEventRelation(String userAccount,
			int activityId) {
		return userDAO.getUserEventRelation(userAccount, activityId);
	}

	@Override
	public int getPinEventCount(String account) {
		return userDAO.getPinEventCount(account);
	}

	@Override
	public int getJoinEventCount(String account) {
		return userDAO.getJoinEventCount(account);
	}

	@Override
	public void pinEvent(String account, Integer eventId, boolean pin) {
		// TODO Auto-generated method stub
		userDAO.pinEvent(account, eventId, pin);
		
	}

	@Override
	public void joinEvent(String account, Integer eventId, boolean join) {
		// TODO Auto-generated method stub
		userDAO.updateEventPin(account, eventId, join);
	}

	@Override
	public List getUsers(String grade, String major, int pageNo, int pageSize) {
		return userDAO.getUsers(grade, major, pageNo, pageSize);
	}

	@Override
	public void updatePassword(String account, String password) {
		userDAO.updatePassword(account, password);
	}

	@Override
	public boolean ifFriends(String accountA, String accountB) {
		return userDAO.ifFriends(accountA, accountB);
	}
	
	@Override
	public boolean ifFollowed(String accountA, String accountB){
		return userDAO.ifFollowed(accountA, accountB);
	}

	@Override
	public void aFollowb(String accountA, String accountB) {
		userDAO.aFollowb(accountA, accountB);
	}
	
	@Override
	public void aNotFollowb(String accountA, String accountB){
		userDAO.aNotFollowb(accountA, accountB);
	}

}
