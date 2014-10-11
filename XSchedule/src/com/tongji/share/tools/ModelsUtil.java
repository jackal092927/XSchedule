package com.tongji.share.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.tongji.basicinfo.MyScheduleEvent;
import com.tongji.jackal.webservice.server.android.ScheduleEvent4JSON;
import com.tongji.persistence.models.ActivityModel;
import com.tongji.persistence.models.RoleModel;
import com.tongji.persistence.models.UserEventClassifyModel;
import com.tongji.persistence.models.UserEventRelation;
import com.tongji.persistence.models.UserModel;
import com.tongji.share.tools.MyEnum.EventRelatedType;
import com.tongji.share.tools.MyEnum.FileType;
import com.tongji.share.view.EventFile;
import com.tongji.share.view.EventImgFile;

public class ModelsUtil {
	// public static MyScheduleEvent ActivityToEvent(ActivityModel activity) {
	// MyScheduleEvent event = new MyScheduleEvent();
	// event.setId(activity.getUuid());
	// // TODO: allday
	// event.setPkId(activity.getId());
	// event.setPublishTime(activity.getPublishTime());
	// event.setOwner(activity.getOwnerAccount());
	// event.setOwnerCategory(activity.getOwnerCategory());
	// event.setAllDay(false);
	// event.setLocation(activity.getLocation());
	// event.setTitle(activity.getTitle());
	// event.setStartDate(activity.getBeginTime());
	// event.setEndDate(activity.getEndTime());
	// event.setModelClass(ActivityModel.class.getName());
	// event.setImageDefault(activity.getImageDefault());
	// event.setImageCount(activity.getImageCount());
	// return event;
	// }
	//
	// public static ScheduleEvent CourseToEvent(CourseModel course) {
	// MyScheduleEvent event = new MyScheduleEvent();
	// event.setId(course.getUuid());
	// // TODO: allday
	// event.setPkId(course.getId());
	// // TODO: owner publishTime
	// event.setOwner(course.getOwnerAccount());
	// event.setAllDay(false);
	// event.setLocation(course.getLocation());
	// event.setTitle(course.getCourseName());
	// event.setStartDate(course.getBeginTime());
	// event.setEndDate(course.getEndTime());
	// event.setModelClass(CourseModel.class.getName());
	// return event;
	// }

	// public static ActivityModel eventToActivity(MyScheduleEvent event) {
	// ActivityModel activityModel = new ActivityModel();
	// activityModel.setUuid(event.getId());
	// activityModel.setId(event.getPkId());
	// activityModel.setTitle(event.getTitle());
	// activityModel.setBeginTime(event.getStartDate());
	// activityModel.setEndTime(event.getEndDate());
	// activityModel.setLocation(event.getLocation());
	// activityModel.setPublishTime(event.getPublishTime());
	// activityModel.setOwnerAccount(event.getOwner());
	// if (activityModel.getId() == 0) {
	// activityModel.setId(null);
	// }
	// activityModel.setOwnerCategory(event.getOwnerCategory());
	// activityModel.setImageDefault(event.getImageDefault());
	// activityModel.setImageCount(event.getImageCount());
	// return activityModel;
	// }
	//
	// public static CourseModel eventToCourse(MyScheduleEvent event) {
	// CourseModel courseModel = new CourseModel();
	// courseModel.setUuid(event.getId());
	// courseModel.setId(event.getPkId());
	// courseModel.setCourseName(event.getTitle());
	// courseModel.setBeginTime(event.getStartDate());
	// courseModel.setEndTime(event.getEndDate());
	// courseModel.setLocation(event.getLocation());
	// courseModel.setTime(event.getPublishTime());
	// courseModel.setOwnerAccount(event.getOwner());
	// if (courseModel.getId() == 0) {
	// courseModel.setId(null);
	// }
	// return courseModel;
	// }

	// public static ScheduleEvent relatedActivityToScheduleEvent(
	// UserEventRelation userEventRelation) {
	// MyScheduleEvent event = ActivityToEvent(userEventRelation
	// .getRelatedActivity());
	// event.setRelatedCategory(userEventRelation.getRelatedCategory());
	// event.setAuthorityList(userEventRelation.getAuthorityList());
	// return event;
	//
	// }

	public static User usermodelToSpringUserdetails(UserModel userModel) {

		String username = userModel.getAccount();
		String password = userModel.getPassword();
		// boolean enabled = true;
		// boolean accountNonExpired = true;
		// boolean credentialsNonExpired = true;
		// boolean accountNonLocked = true;

		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		for (RoleModel role : userModel.getRoles()) {
			authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
		}

		User user = new User(username, password, authorities);
		return user;
	}

	public static List setToList(Set set) {
		List list = new ArrayList();
		for (Object object : set) {
			list.add(object);
		}
		return list;
	}

	public static UserEventRelation usermodelToUserEventRelation(UserModel user) {
		UserEventRelation userEventRelation = new UserEventRelation();
		userEventRelation.setRelatedUserAccount(user.getAccount());
		return userEventRelation;
	}

	private static EventFile fileToEventFile(File file) {
		EventFile eventFile = new EventFile(file);
		return eventFile;
	}

	private static EventImgFile fileToEventImgFile(File file) {
		EventImgFile imgFile = new EventImgFile(file);
		return imgFile;
	}

	public static ActivityModel eventToActivity(MyScheduleEvent event) {
		// TODO Auto-generated method stub
		ActivityModel activityModel = event.getEvent();
		if (activityModel.getId() == null || activityModel.getId() == 0) {
			activityModel.setId(null);
		}
		return activityModel;
	}

	public static void ModelConstraintCheck(Object model) {
		if (model.getClass().equals(ActivityModel.class)) {
			ActivityModel activity = (ActivityModel) model;
			if (activity.getTitle() == null) {
				activity.setTitle("NEW TITLE");
			}
			if (activity.getOwnerCategory() == null
					|| activity.getOwnerCategory().length() < 1) {
				activity.setOwnerCategory("[unclassified]");
			}
			if (activity.getOwnerColorSetting() == null
					|| activity.getOwnerColorSetting().length() < 1) {
				activity.setOwnerColorSetting("DEFAULT");
			}
			if (activity.getLocation() == null || activity.getLocation().length() < 1) {
				activity.setLocation("NA");
			}
			if (activity.getPublishTime() == null) {
				activity.setPublishTime(new Date());
			}
			if (activity.getUuid() == null) {
				activity.setUuid(UUID.randomUUID().toString());
			}
		}
		if (model.getClass().equals(UserEventRelation.class)) {
			UserEventRelation userEventRelation = (UserEventRelation) model;
			String relatedColorSettingString = userEventRelation
					.getRelatedColorSetting();
			if (relatedColorSettingString == null
					|| relatedColorSettingString.length() < 1) {
				userEventRelation.setRelatedColorSetting("DEFAULT");
			}
		}
		if (model.getClass().equals(UserEventClassifyModel.class)) {
			UserEventClassifyModel userEventClassifyModel = (UserEventClassifyModel) model;
			String colorSetting = userEventClassifyModel.getColorSetting();
			String eventType = userEventClassifyModel.getEventType();
			if (colorSetting == null || colorSetting.length() < 1) {
				colorSetting = "DEFAULT";
			}
			if (eventType == null || eventType.length() < 1) {
				eventType = "private";
			}
		}

	}

	public static MyScheduleEvent eventModelToScheduleEvent(
			ActivityModel event, UserEventRelation userEventRelation) {
		MyScheduleEvent scheduleEvent = new MyScheduleEvent();
		scheduleEvent.setEvent(event);
		if (userEventRelation != null) {
			scheduleEvent.setPin(userEventRelation.isPin());
			scheduleEvent.setJoin(userEventRelation.isJoinin());
			if (userEventRelation.getEventRelatedType() != null) {
				if (userEventRelation.getEventRelatedType().equals("PRIVATE")) {
					scheduleEvent.setRelatedType(EventRelatedType.PRIVATE);
				} else if (userEventRelation.getEventRelatedType().equals(
						"PUBLIC")) {
					scheduleEvent.setRelatedType(EventRelatedType.PUBLIC);
				} else if (userEventRelation.getEventRelatedType().equals(
						"FRIEND")) {
					scheduleEvent.setRelatedType(EventRelatedType.FRIEND);
				}
			}
			scheduleEvent.setCategory(userEventRelation.getRelatedCategory());
			scheduleEvent.setCategory(userEventRelation
					.getRelatedColorSetting());
		}
		// scheduleEvent.setCategory(event.getOwnerCategory());
		// scheduleEvent.setCategorySetColor(event.getOwnerColorSetting());
		return scheduleEvent;

	}

	public static MyScheduleEvent userEventRelationToScheduleEvent(
			UserEventRelation relatedEventRelation) {
		MyScheduleEvent scheduleEvent = new MyScheduleEvent();
		scheduleEvent.setEvent(relatedEventRelation.getRelatedActivity());
		scheduleEvent.setPin(relatedEventRelation.isPin());
		scheduleEvent.setJoin(relatedEventRelation.isJoinin());
		if (relatedEventRelation.getEventRelatedType() != null) {
			if (relatedEventRelation.getEventRelatedType().equals("PRIVATE")) {
				scheduleEvent.setRelatedType(EventRelatedType.PRIVATE);
			} else if (relatedEventRelation.getEventRelatedType().equals(
					"PUBLIC")) {
				scheduleEvent.setRelatedType(EventRelatedType.PUBLIC);
			} else if (relatedEventRelation.getEventRelatedType().equals(
					"FRIEND")) {
				scheduleEvent.setRelatedType(EventRelatedType.FRIEND);
			}
		}
		scheduleEvent.setCategory(relatedEventRelation.getRelatedCategory());
		scheduleEvent
				.setCategory(relatedEventRelation.getRelatedColorSetting());

		return scheduleEvent;
	}

	public static EventFile getEventFile(FileType currentFileType) {
		switch (currentFileType) {
		case IMG:
			return new EventImgFile();
		case MATERIAL:
			return new EventFile();
		case OTHERS:
			return new EventFile();
		default:
			return null;
		}
	}

	public static EventFile fileToEventFile(File file, FileType currentFileType) {
		switch (currentFileType) {
		case IMG:
			return fileToEventImgFile(file);
		case MATERIAL:
			return fileToEventFile(file);
		case OTHERS:
			return fileToEventFile(file);
		default:
			return null;
		}
	}

	public static ScheduleEvent4JSON eventToEvent4JSON(ActivityModel event) {

		ScheduleEvent4JSON event4json = new ScheduleEvent4JSON();
		
		event4json.setServerId(event.getId());
		event4json.setBeginTime(event.getBeginTime());
		event4json.setEndTime(event.getEndTime());
		event4json.setTitle(event.getTitle());
		event4json.setLocation(event.getLocation());
		event4json.setOwner(event.getOwnerAccount());
		event4json.setPublishTime(event.getPublishTime());
		event4json.setLastUpdateTime(event.getLastUpdateTime());
		event4json.setAllDay(event.isAllDay());

		return event4json;
	}

	public static ActivityModel event4JsonToEvent(ScheduleEvent4JSON clientEvent) {
		// TODO Auto-generated method stub
		
		ActivityModel event = new ActivityModel();
		if (clientEvent.getServerId() > 0) {
			event.setId( new Long(clientEvent.getServerId()).intValue() );
		}
		event.setBeginTime(clientEvent.getBeginTime());
		event.setEndTime(clientEvent.getEndTime());
		event.setTitle(clientEvent.getTitle());
		event.setLocation(clientEvent.getLocation());
		event.setOwnerAccount(clientEvent.getOwner());
		event.setBeginTime(clientEvent.getBeginTime());
		event.setPublicity(clientEvent.getPublicity());
		event.setAllDay(clientEvent.isAllDay());
		
		return event;
	}

}
