package com.example.android.samplesync.syncadapter;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.apache.http.ParseException;
import org.apache.http.auth.AuthenticationException;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONException;

import com.example.android.samplesync.Constants;
import com.example.android.samplesync.client.NetworkUtilities;
import com.example.android.samplesync.client.ScheduleEvent;
import com.example.android.samplesync.platform.CalendarManager;
import com.example.android.samplesync.platform.ContactManager;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.text.TextUtils;
import android.util.Log;

public class CalendarSyncAdapter extends AbstractThreadedSyncAdapter {

	private static final String TAG = "SyncAdapter";
	private static final String SYNC_MARKER_KEY = "com.example.android.calendarsync.marker";
	private static final boolean NOTIFY_AUTH_FAILURE = true;

	private final AccountManager mAccountManager;

	public static final String[] EVENT_PROJECTION = new String[] {
			Calendars._ID, // 0
			Calendars.ACCOUNT_NAME, // 1
			Calendars.CALENDAR_DISPLAY_NAME, // 2
			Calendars.OWNER_ACCOUNT // 3
	};

	// The indices for the projection array above.
	private static final int PROJECTION_ID_INDEX = 0;
	private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
	private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
	private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

	private final Context mContext;

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

	public CalendarSyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
		mContext = context;
		mAccountManager = AccountManager.get(context);

	}

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {

		Log.i(TAG, "Jackal: call CalendarSyncAdapter.onPerformSync()");

		try {

			long lastSyncMarker = getServerSyncMarker(account);

			if (lastSyncMarker == 0) {
				// TODO:
			}

			List<ScheduleEvent> dirtyEvents;
			List<ScheduleEvent> updatedEvents;

			final String authtoken = mAccountManager.blockingGetAuthToken(
					account, Constants.AUTHTOKEN_TYPE, NOTIFY_AUTH_FAILURE);

			final long calendarId = CalendarManager.ensureCalendarExists(
					mContext, account);

			dirtyEvents = CalendarManager.getDirtyEvents(mContext, account);

			updatedEvents = NetworkUtilities.syncEvents(account, authtoken,
					lastSyncMarker, dirtyEvents);

			Log.d(TAG, "Jackal: Call CalendarManager's sync contacts");
			long newSyncState = CalendarManager.updateEvents(mContext,
					account.name, updatedEvents, calendarId, lastSyncMarker);

			setServerSyncMarker(account, newSyncState);

			if (dirtyEvents != null && dirtyEvents.size() > 0) {
				CalendarManager.clearSyncFlags(mContext, dirtyEvents);

			}

		} catch (final AuthenticatorException e) {
			Log.e(TAG, "AuthenticatorException", e);
			syncResult.stats.numParseExceptions++;
		} catch (final OperationCanceledException e) {
			Log.e(TAG, "OperationCanceledExcetpion", e);
		} catch (final JsonMappingException e) {
			Log.e(TAG, "JSONException", e);
			syncResult.stats.numParseExceptions++;
		} catch (final JsonGenerationException e) {
			Log.e(TAG, "JSONException", e);
			syncResult.stats.numParseExceptions++;
		} catch (final AuthenticationException e) {
			Log.e(TAG, "AuthenticationException", e);
			syncResult.stats.numAuthExceptions++;
		} catch (final ParseException e) {
			Log.e(TAG, "ParseException", e);
			syncResult.stats.numParseExceptions++;
		} catch (final IOException e) {
			Log.e(TAG, "IOException", e);
			syncResult.stats.numIoExceptions++;
		}
	}

	/**
	 * 
	 * @param account
	 * @param newSyncState
	 */
	private void setServerSyncMarker(Account account, long marker) {
		mAccountManager.setUserData(account, SYNC_MARKER_KEY,
				Long.toString(marker));
	}

	/**
	 * 
	 * @param account
	 * @return
	 */
	private long getServerSyncMarker(Account account) {
		String markerString = mAccountManager.getUserData(account,
				SYNC_MARKER_KEY);
		if (!TextUtils.isEmpty(markerString)) {
			return Long.parseLong(markerString);
		}
		return 0;
	}

}
