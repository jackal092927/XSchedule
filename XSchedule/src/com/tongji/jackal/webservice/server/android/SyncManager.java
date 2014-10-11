package com.tongji.jackal.webservice.server.android;

import java.util.ArrayList;
import java.util.List;

import com.tongji.activityinfo.ActivityService;
import com.tongji.activityinfo.IActivityService;
import com.tongji.persistence.models.ActivityModel;
import com.tongji.share.tools.ModelsUtil;
import com.tongji.share.tools.MyApplicationContextUtils;

public class SyncManager {

	private List clientDirtyList;

	private List serverDirtyList;

	private String account;

	private String authToken;// default: "helloworld"

	private long syncMark;

	private CalendarSyncService calendarSyncService;

	public SyncManager() {

	}

	public SyncManager(String account, String authToken, long syncMark) {
		this.account = account;
		this.authToken = authToken;
		this.syncMark = syncMark;
	}

	public SyncManager(String account, String authToken, long syncMark,
			List<ScheduleEvent4JSON> clientDirtyList) {
		this(account, authToken, syncMark);
		this.clientDirtyList = clientDirtyList;
	}

	public boolean authorize() {
		return true;
	}

	private void loadServerDirtyList(long syncMark) {
		IActivityService activityService = (ActivityService) MyApplicationContextUtils
				.getBean("activityService");

		List<ActivityModel> eventList = activityService
				.getUsereventsWithSyncmark(account, syncMark);

		if (serverDirtyList == null) {
			serverDirtyList = new ArrayList<ScheduleEvent4JSON>();
		}

		for (ActivityModel event : eventList) {
			serverDirtyList.add(ModelsUtil.eventToEvent4JSON(event));
		}
	}

	public void sync() {
		calendarSyncService = (CalendarSyncService) MyApplicationContextUtils
				.getBean("calendarSyncService");
		loadServerDirtyList(syncMark);
		serverDirtyList = calendarSyncService.exchangeDataList(clientDirtyList,
				serverDirtyList);
	}

	public List getClientDirtyList() {
		return clientDirtyList;
	}

	public void setClientDirtyList(List clientDirtyList) {
		this.clientDirtyList = clientDirtyList;
	}

	public List getServerDirtyList() {
		return serverDirtyList;
	}

	public void setServerDirtyList(List serverDirtyList) {
		this.serverDirtyList = serverDirtyList;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public long getSyncMark() {
		return syncMark;
	}

	public void setSyncMark(long syncMark) {
		this.syncMark = syncMark;
	}

	public CalendarSyncService getCalendarSyncService() {
		return calendarSyncService;
	}

	public void setCalendarSyncService(CalendarSyncService calendarSyncService) {
		this.calendarSyncService = calendarSyncService;
	}

}
