package com.example.android.samplesync.platform;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.android.samplesync.Constants;
import com.example.android.samplesync.client.RawContact;
import com.example.android.samplesync.client.ScheduleEvent;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Colors;
import android.provider.CalendarContract.Events;
import android.provider.ContactsContract.Groups;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;

public class CalendarManager {

	public static final String CUSTOM_IM_PROTOCOL = "SampleSyncAdapter";

	private static final String TAG = "CalendarManager";

	public static final String SAMPLE_GROUP_NAME = "Sample Group";

	public static final String CALENDAR_NAME = "Test Calendar";

	public static final String CALENDAR_DISPLAY_NAME = "My XSchedule";

	public static long ensureCalendarExists(Context context, Account account) {
		Log.i(TAG, "Calling CalendarManager.ensureCalendarExists()");

		final ContentResolver resolver = context.getContentResolver();

		long calendarId = 0;

		final Cursor cursor = resolver.query(Calendars.CONTENT_URI,
				new String[] { Calendars._ID }, Calendars.ACCOUNT_NAME
						+ "=? AND " + Calendars.ACCOUNT_TYPE + "=? AND "
						+ Calendars.NAME + "=?", new String[] { account.name,
						account.type, CALENDAR_NAME }, null);
		if (cursor != null) {
			try {
				if (cursor.moveToFirst()) {
					calendarId = cursor.getLong(0);
				}
			} finally {
				cursor.close();
			}
		}

		if (calendarId == 0) {
			// Sample group doesn't exist yet, so create it
			final ContentValues contentValues = new ContentValues();
			contentValues.put(Calendars.ACCOUNT_NAME, account.name);
			contentValues.put(Calendars.ACCOUNT_TYPE, account.type);
			contentValues.put(Calendars.NAME, CALENDAR_NAME);
			contentValues.put(Calendars.OWNER_ACCOUNT, account.name);
			contentValues.put(Calendars.CALENDAR_DISPLAY_NAME,
					CALENDAR_DISPLAY_NAME);
			contentValues.put(Calendars.CALENDAR_COLOR, Color.RED);
			contentValues.put(Calendars.CALENDAR_ACCESS_LEVEL,
					Calendars.CAL_ACCESS_CONTRIBUTOR);
			contentValues.put(Calendars.SYNC_EVENTS, 1);

			final Uri newCalendarUri = resolver.insert(
					asSyncAdapter(Calendars.CONTENT_URI, account.name,
							account.type), contentValues);
			calendarId = ContentUris.parseId(newCalendarUri);
		}
		return calendarId;
	}

	static Uri asSyncAdapter(Uri uri, String account, String accountType) {
		return uri
				.buildUpon()
				.appendQueryParameter(
						android.provider.CalendarContract.CALLER_IS_SYNCADAPTER,
						"true")
				.appendQueryParameter(Calendars.ACCOUNT_NAME, account)
				.appendQueryParameter(Calendars.ACCOUNT_TYPE, accountType)
				.build();
	}

	static Uri asSyncAdapter(Uri uri) {
		return uri
				.buildUpon()
				.appendQueryParameter(
						android.provider.CalendarContract.CALLER_IS_SYNCADAPTER,
						"true").build();
	}

	public static List<ScheduleEvent> getDirtyEvents(Context context,
			Account account) {
		Log.i(TAG, "*** Looking for local dirty contacts");

		List<ScheduleEvent> dirtyEvents = new ArrayList<ScheduleEvent>();

		final ContentResolver resolver = context.getContentResolver();

		final Cursor c = resolver.query(DirtyQuery.CONTENT_URI,
				DirtyQuery.PROJECTION, DirtyQuery.SELECTION,
				new String[] { account.name }, null);
		try {
			while (c.moveToNext()) {
				final long clientId = c.getLong(DirtyQuery.COLUMN_CLIENT_ID);
				final long serverId = c.getLong(DirtyQuery.COLUMN_SERVER_ID);
				final boolean isDirty = "1".equals(c
						.getString(DirtyQuery.COLUMN_DIRTY));
				final boolean isDeleted = "1".equals(c
						.getString(DirtyQuery.COLUMN_DELETED));

				Log.i(TAG, "Dirty Event: " + Long.toString(clientId));

				if (isDeleted) {
					Log.i(TAG, "Event is marked for deletion");
					ScheduleEvent event = ScheduleEvent.createDeletedEvent(
							clientId, serverId);
					dirtyEvents.add(event);
				} else if (isDirty) {
					ScheduleEvent rawContact = new ScheduleEvent(clientId, serverId, isDeleted);
					
					dirtyEvents.add(rawContact);
				}
			}

		} finally {
			if (c != null) {
				c.close();
			}
		}
		return dirtyEvents;
	}

	public static Long updateEvents(Context context, String accountName,
			List<ScheduleEvent> updatedEvents, long calendarId,
			long lastSyncMarker) {
		long currentSyncMarker = lastSyncMarker;
		final ContentResolver resolver = context.getContentResolver();
		final BatchOperation batchOperation = new BatchOperation(context,
				resolver);

		Log.d(TAG, "In SyncEvents");

		for (final ScheduleEvent scheduleEvent : updatedEvents) {
			long syncMarker = scheduleEvent.getLastUpdateTime().getTime();
			if (syncMarker > currentSyncMarker) {
				currentSyncMarker = syncMarker;
			}

			if (scheduleEvent.isClientNew()) {
				updateEventServerId(context, scheduleEvent.getClientId(),
						scheduleEvent.getServerId(), true, batchOperation);
			} else if (scheduleEvent.isServerNew()) {
				if (scheduleEvent.isValid()) {
					addEvent(context, accountName, scheduleEvent, calendarId,
							true, batchOperation);
				}
			} else if (scheduleEvent.isValid()) {
				updateEvent(context, resolver, scheduleEvent, true,
						batchOperation);
			} else {
				deleteEvent(context, scheduleEvent.getClientId(),
						batchOperation);
			}

			if (batchOperation.size() >= 50) {
				batchOperation.excuteCalendar();
			}
		}

		batchOperation.excuteCalendar();

		return currentSyncMarker;
	}

	private static void updateEvent(Context context, ContentResolver resolver,
			ScheduleEvent scheduleEvent, boolean inSync,
			BatchOperation batchOperation) {

		CalendarOperations.updateExistingEvent(context, scheduleEvent, true,
				batchOperation);

	}

	private static void addEvent(Context context, String accountName,
			ScheduleEvent scheduleEvent, long calendarId, boolean inSync,
			BatchOperation batchOperation) {
		final CalendarOperations calendarOp = CalendarOperations
				.createNewEvent(context, calendarId, scheduleEvent,
						accountName, inSync, batchOperation);

		// TODO: add reminders, add attendees.

	}

	private static void addEvent2(Context context, String accountName,
			ScheduleEvent scheduleEvent, long calendarId, boolean inSync,
			BatchOperation batchOperation) {
		ContentValues values = new ContentValues();
		values.put(Events.DTSTART, scheduleEvent.getBeginTime().getTime());
		values.put(Events.DTEND, scheduleEvent.getEndTime().getTime());
		values.put(Events.TITLE, "Jazzercise");
		values.put(Events.DESCRIPTION, "Group workout");
		values.put(Events.CALENDAR_ID, calendarId);
		values.put(Events.EVENT_TIMEZONE, "America/Los_Angeles");
		ContentResolver cr = context.getContentResolver();
		Uri uri = cr.insert(Events.CONTENT_URI, values);

		long eventID = Long.parseLong(uri.getLastPathSegment());
		// TODO: add reminders, add attendees.

	}

	private static void updateEventServerId(Context context, long clientId,
			long serverId, boolean inSync, BatchOperation batchOperation) {
		final CalendarOperations calendarOp = CalendarOperations
				.updateExistingEvent(context, clientId, inSync, batchOperation);
		Uri uri = ContentUris.withAppendedId(Events.CONTENT_URI, clientId);
		calendarOp.updateServerId(serverId, uri);
	}

	private static long lookupEvent(ContentResolver resolver, long serverId) {
		// TODO Auto-generated method stub
		return 0;
	}

	private static void deleteEvent(Context context, long localId,
			BatchOperation batchOperation) {
		// TODO Auto-generated method stub
		batchOperation.add(CalendarOperations.newDeleteCpo(
				ContentUris.withAppendedId(Events.CONTENT_URI, localId), true,
				true).build());
	}

	public static void clearSyncFlags(Context mContext,
			List<ScheduleEvent> dirtyEvents) {
		// TODO Auto-generated method stub

	}

	final private static class DirtyQuery {

		private DirtyQuery() {
		}

		public final static String[] PROJECTION = new String[] { Events._ID,
				Events.DTSTART, Events.DTEND,
				// Events.DURATION,
				// Events.RRULE,
				// Events.RDATE,
				// Events.EVENT_TIMEZONE,
				// Events.CALENDAR_ID,
				Events.DIRTY, Events.DELETED, Events._SYNC_ID
		// RawContacts._ID,
		// RawContacts.SOURCE_ID,
		// RawContacts.DIRTY,
		// RawContacts.DELETED,
		// RawContacts.VERSION
		};

		public final static int COLUMN_CLIENT_ID = 0;
		public final static int COLUMN_DTSTART = 1;
		public final static int COLUMN_DTEND = 2;
		// public final static int COLUMN_DURATION = 3;
		// public final static int COLUMN_RRULE = 4;
		// public final static int COLUMN_RDATE = 5;
		// public final static int COLUMN_EVENT_TIMEZONE = 6;
		// public final static int COLUMN_CALENDAR_ID = 7;
		public final static int COLUMN_DIRTY = 4;
		public final static int COLUMN_DELETED = 5;
		public final static int COLUMN_SERVER_ID = 5;

		public static final Uri CONTENT_URI = Events.CONTENT_URI
				.buildUpon()
				.appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER,
						"true").build();

		public static final String SELECTION = Events.DIRTY + "=1 AND "
				+ Events.ACCOUNT_TYPE + "='" + Constants.ACCOUNT_TYPE
				+ "' AND " + Events.ACCOUNT_NAME + "=?";
	}
}
