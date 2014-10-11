package com.tongji.basicinfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ManagedProperty;

import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import com.tongji.activityinfo.IActivityService;
import com.tongji.courseinfo.ICourseService;
import com.tongji.persistence.IActivityDAO;
import com.tongji.persistence.models.ActivityModel;
import com.tongji.persistence.models.CourseModel;
import com.tongji.persistence.models.UserModel;
import com.tongji.share.tools.ModelsUtil;
import com.tongji.share.view.models.TransferItem;

public class ScheduleService implements IScheduleService {

	@ManagedProperty(value = "#{userService}")
	private IUserService userService;

	@ManagedProperty(value = "#{activityService}")
	private IActivityService activityService;

	@ManagedProperty(value = "#{courseService}")
	private ICourseService courseService;

	private IUserEventRelationService userEventRelationService;

//	@Override
//	public ScheduleModel getScheduleModel(UserBean currentUser) {
//		try {
//			List<ScheduleEvent> events = new ArrayList<ScheduleEvent>();
//			Set<ActivityModel> activitySet = userService
//					.getUserActivities(currentUser.getAccount());
//			// Set<CourseModel> courseSet =
//			// userService.getUserCourses(currentUser.getAccount());
//
//			for (ActivityModel activityModel : activitySet) {
//				events.add(ModelsUtil.ActivityToEvent(activityModel));
//			}
//
//			// for (CourseModel courseModel : courseSet) {
//			// events.add(ModelsUtil.CourseToEvent(courseModel));
//			// }
//			//
//			ScheduleModel scheduleModel = new MyScheduleModel(events);
//			return scheduleModel;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//
//	}

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	@Override
	public void storeEvent(MyScheduleEvent event) {
		if (event.getModelClass().equals(ActivityModel.class.getName())) {
			ActivityModel activityModel = ModelsUtil.eventToActivity(event);
			activityService.store(activityModel);
		} else if (event.getModelClass().equals(CourseModel.class.getName())) {
//			CourseModel courseModel = ModelsUtil.eventToCourse(event);
//			courseService.store(courseModel);
		}
	}

	public IActivityService getActivityService() {
		return activityService;
	}

	public void setActivityService(IActivityService activityService) {
		this.activityService = activityService;
	}

	public ICourseService getCourseService() {
		return courseService;
	}

	public void setCourseService(ICourseService courseService) {
		this.courseService = courseService;
	}

	@Override
	public void storeEventWithRelatedUsers(MyScheduleEvent event,
			List relatedUserList) {

		if (event.getModelClass().equals(ActivityModel.class.getName())) {
			ActivityModel relatedEvent = ModelsUtil.eventToActivity(event);
			userEventRelationService.storeEventWithRelatedUsers(relatedEvent,
					relatedUserList);
		} else if (event.getModelClass().equals(CourseModel.class.getName())) {
			// TODO:
		}

	}

	public IUserEventRelationService getUserEventRelationService() {
		return userEventRelationService;
	}

	public void setUserEventRelationService(
			IUserEventRelationService userEventRelationService) {
		this.userEventRelationService = userEventRelationService;
	}

	@Override
	public void storeEventWithRelatedUsersDiff(MyScheduleEvent event,
			Set diffSet) {
		// TODO Auto-generated method stub
		if (event.getModelClass().equals(ActivityModel.class.getName())) {
			ActivityModel relatedEvent = ModelsUtil.eventToActivity(event);
			List<TransferItem> diffList = ModelsUtil.setToList(diffSet);
			userEventRelationService.storeEventWithRelatedUsersDiff(
					relatedEvent, diffList);
		} else if (event.getModelClass().equals(CourseModel.class.getName())) {
			// TODO:
		}

	}

}
