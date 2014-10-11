package com.tongji.share.view.schedule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;

import com.tongji.activityinfo.ActivityService;
import com.tongji.activityinfo.IActivityService;
import com.tongji.basicinfo.UserBean;
import com.tongji.persistence.models.CommentModel;
import com.tongji.share.tools.MyApplicationContextUtils;

public class CommentsBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6090671657839268439L;
	
	private static final String INI_WORD = "You may add your comments!";

	private UserBean currentUser;

	private int eventId;

	private IActivityService activityService;
	
	private List<CommentModel> commentList = new ArrayList<CommentModel>();
	
	private String commentContext = INI_WORD;
	
	public CommentsBean() {

	}

	public CommentsBean(UserBean currentUser) {
		this.currentUser = currentUser;
		activityService = (ActivityService) MyApplicationContextUtils
				.getBean("activityService");
		
		
	}

	public CommentsBean(UserBean currentUser, int eventId) {
		this.currentUser = currentUser;
		activityService = (ActivityService) MyApplicationContextUtils
				.getBean("activityService");
		if (eventId != 0) {
			this.eventId = eventId;
			commentList = activityService.getRelatedComments(eventId);
		}
		
	}
	
	public void addComment(){
		if (commentContext!=null || commentContext.length() > 0) {
			CommentModel comment = new CommentModel();
			comment.setOwnerAccount(currentUser.getAccount());
			comment.setActivity_id(eventId);
			comment.setContext(commentContext);
			comment.setPublishTime(new Date());
			
			int id = storeComment(comment);
			if (id!=0) {
				comment.setId(id);
			}
			commentList.add(comment);
			
			commentContext = INI_WORD;
		}
	}
	
	private int storeComment(CommentModel comment){
		activityService.storeComment(comment);
		return 0;
	}
	

	public UserBean getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(UserBean currentUser) {
		this.currentUser = currentUser;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public List getCommentList() {
		return commentList;
	}

	public void setCommentList(List commentList) {
		this.commentList = commentList;
	}

	public String getCommentContext() {
		return commentContext;
	}

	public void setCommentContext(String commentContext) {
		this.commentContext = commentContext;
	}
	
	

}
