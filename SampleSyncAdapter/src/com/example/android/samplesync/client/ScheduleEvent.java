package com.example.android.samplesync.client;

import java.util.Date;
import java.util.UUID;

import org.json.JSONObject;

public class ScheduleEvent {

	private static final String TAG = "ScheduleEvent";

	private long clientId = -1;

	private long serverId = -1;

	private String title = "NO TITLE";

	private String location;

	private String owner;

	private Date beginTime;

	private Date endTime;

	private Date publishTime = new Date();// not found yet

	private int publicity = 0;// not found yet

	private String uuid;// not found yet

	private Date lastUpdateTime = new Date();// not found yet

	private boolean valid = true; // not found yet

	private boolean clientNew;

	private boolean serverNew;

	private int reminderMinutes;// default 0;

	private int reminderMethod;// default -1;

	public ScheduleEvent() {

	}

	public ScheduleEvent(long serverId, String title, String location,
			String owner, Date beginTime, Date endTime, Date lastUpdateTime) {
		this.clientId = 0;
		this.serverId = serverId;
		this.title = title;
		this.location = location;
		this.owner = owner;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.publishTime = new Date();
		this.publicity = 0;
		this.uuid = UUID.randomUUID().toString();
		this.lastUpdateTime = lastUpdateTime;
		this.valid = true;
		this.clientNew = false;
		this.serverNew = true;
		this.reminderMinutes = 0;
		this.reminderMethod = 0;
	}

	public ScheduleEvent(long clientId, long serverId) {// deleted
		this.clientId = clientId;
		this.serverId = serverId;
		this.title = null;
		this.location = null;
		this.owner = null;
		this.beginTime = null;
		this.endTime = null;
		this.publishTime = null;
		this.publicity = 0;
		this.uuid = null;
		this.lastUpdateTime = new Date();
		this.valid = true;
		this.clientNew = false;
		this.serverNew = false;
		this.reminderMinutes = 0;
		this.reminderMethod = 0;

	}

	public ScheduleEvent(long clientId, long serverId, boolean isDeleted) {
		this.clientId = clientId;
		this.serverId = serverId;
		this.title = null;
		this.location = null;
		this.owner = null;
		this.beginTime = null;
		this.endTime = null;
		this.publishTime = null;
		this.publicity = 0;
		this.uuid = null;
		this.lastUpdateTime = null;
		this.valid = isDeleted;
		this.clientNew = false;
		this.serverNew = false;
		this.reminderMinutes = 0;
		this.reminderMethod = 0;
	}

	public static String getTag() {
		return TAG;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	public void setServerId(long serverId) {
		this.serverId = serverId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	public void setPublicity(int publicity) {
		this.publicity = publicity;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public void setClientNew(boolean clientNew) {
		this.clientNew = clientNew;
	}

	public void setServerNew(boolean serverNew) {
		this.serverNew = serverNew;
	}

	public void setReminderMinutes(int reminderMinutes) {
		this.reminderMinutes = reminderMinutes;
	}

	public void setReminderMethod(int reminderMethod) {
		this.reminderMethod = reminderMethod;
	}

	public ScheduleEvent(long localId, long serverId, String title,
			String location, String owner, Date beginTime, Date endTime,
			Date publishTime, int publicity, String uuid, Date lastUpdateTime,
			boolean valid, boolean clientNew, boolean serverNew,
			int reminderMinutes, int reminderMethod) {
		this.clientId = localId;
		this.serverId = serverId;
		this.title = title;
		this.location = location;
		this.owner = owner;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.publishTime = publishTime;
		this.publicity = publicity;
		this.uuid = uuid;
		this.lastUpdateTime = lastUpdateTime;
		this.valid = valid;
		this.clientNew = clientNew;
		this.serverNew = serverNew;
		this.reminderMinutes = reminderMinutes;
		this.reminderMethod = reminderMethod;
	}

	public String getLocation() {
		return location;
	}

	public String getOwner() {
		return owner;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public Date getPublishTime() {
		return publishTime;
	}

	public int getPublicity() {
		return publicity;
	}

	public String getUuid() {
		return uuid;
	}

	public String getTitle() {
		return title;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public boolean isValid() {
		return valid;
	}

	public long getClientId() {
		return clientId;
	}

	public long getServerId() {
		return serverId;
	}

	public boolean isClientNew() {
		return clientNew;
	}

	public boolean isServerNew() {
		return serverNew;
	}

	public int getReminderMinutes() {
		return reminderMinutes;
	}

	public int getReminderMethod() {
		return reminderMethod;
	}

	public static ScheduleEvent createDeletedEvent(long clientId,
			long serverId) {
		return new ScheduleEvent(clientId, serverId);
	}
	
}
