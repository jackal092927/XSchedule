package com.example.android.samplesync.platform;

import java.util.TimeZone;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Attendees;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.text.TextUtils;

import com.example.android.samplesync.Constants;
import com.example.android.samplesync.R;
import com.example.android.samplesync.client.NetworkUtilities;
import com.example.android.samplesync.client.ScheduleEvent;

public class CalendarOperations {
	private ContentValues mValues;
	private final BatchOperation mBatchOperation;
	private final Context mContext;
	private boolean mIsSyncOperation;
	private long mEventId;
	private int mBackReference; // what for? count?
	private boolean mIsNewEvent;
	private String accountName;

	/**
	 * Since we're sending a lot of contact provider operations in a single
	 * batched operation, we want to make sure that we "yield" periodically so
	 * that the Contact Provider can write changes to the DB, and can open a new
	 * transaction. This prevents ANR (application not responding) errors. The
	 * recommended time to specify that a yield is permitted is with the first
	 * operation on a particular contact. So if we're updating multiple fields
	 * for a single contact, we make sure that we call withYieldAllowed(true) on
	 * the first field that we update. We use mIsYieldAllowed to keep track of
	 * what value we should pass to withYieldAllowed().
	 */
	private boolean mIsYieldAllowed;

	public CalendarOperations(Context context, boolean isSyncOperation,
			BatchOperation batchOperation) {
		setmValues(new ContentValues());
		mIsYieldAllowed = true;
		mIsSyncOperation = isSyncOperation;
		mContext = context;
		mBatchOperation = batchOperation;

	}

	// public CalendarOperations(Context context, long serverId,
	// String accountName, boolean isSyncOperation,
	// BatchOperation batchOperation) {
	// this(context, isSyncOperation, batchOperation);
	// mBackReference = mBatchOperation.size();
	// mIsNewEvent = true;
	// mValues.put(Events._SYNC_ID, String.valueOf(serverId));
	// // mValues.put(Events.ACCOUNT_TYPE, Constants.ACCOUNT_TYPE);
	// // mValues.put(Events.ACCOUNT_NAME, accountName);
	// ContentProviderOperation.Builder builder = newInsertCpo(
	// Events.CONTENT_URI, mIsSyncOperation, true).withValues(mValues);
	// mBatchOperation.add(builder.build());
	// }

	public CalendarOperations(Context context, long calendarId,
			ScheduleEvent event, String accountName, boolean isSyncOperation,
			BatchOperation batchOpeartion) {
		this(context, isSyncOperation, batchOpeartion);
		mBackReference = mBatchOperation.size();
		this.accountName = accountName;
		mIsNewEvent = true;
		// mValues.put(Calendars.ACCOUNT_NAME, accountName);
		// mValues.put(Calendars.ACCOUNT_TYPE, Constants.ACCOUNT_TYPE);
		getmValues().put(Events.CALENDAR_ID, calendarId);// TODO: to be changed
		getmValues().put(Events._SYNC_ID, event.getServerId());
		getmValues().put(Events.TITLE, event.getTitle());
		getmValues().put(Events.EVENT_LOCATION, event.getLocation());
		getmValues().put(Events.ORGANIZER, event.getOwner());
		getmValues().put(Events.DTSTART, event.getBeginTime().getTime());
		getmValues().put(Events.DTEND, event.getEndTime().getTime());
		getmValues().put(Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
		ContentProviderOperation.Builder builder = newInsertCpo(
				Events.CONTENT_URI, accountName, mIsSyncOperation, true).withValues(getmValues());
		mBatchOperation.add(builder.build());
	}

	public CalendarOperations(Context context, long clientId,
			boolean isSyncOperation, BatchOperation batchOperation) {
		this(context, isSyncOperation, batchOperation);
		mIsNewEvent = false;
		mEventId = clientId;
	}

	public CalendarOperations(Context context, ScheduleEvent event,
			boolean isSyncOperation, BatchOperation batchOperation) {
		this(context, isSyncOperation, batchOperation);
		mIsNewEvent = false;
		mEventId = event.getClientId();
		getmValues().put(Events._SYNC_ID, event.getServerId());
		getmValues().put(Events.TITLE, event.getTitle());
		getmValues().put(Events.EVENT_LOCATION, event.getLocation());
		getmValues().put(Events.ORGANIZER, event.getOwner());
		getmValues().put(Events.DTSTART, event.getBeginTime().getTime());
		getmValues().put(Events.DTEND, event.getEndTime().getTime());
		ContentProviderOperation.Builder builder = newUpdateCpo(
				Events.CONTENT_URI, mIsSyncOperation, true).withValues(getmValues());
		mBatchOperation.add(builder.build());
	}

	/**
	 * Returns an instance of CalendarOperations instance for adding new contact
	 * to the platform contacts provider.
	 * 
	 * @param context
	 *            the Authenticator Activity context
	 * @param userId
	 *            the userId of the sample SyncAdapter user object
	 * @param accountName
	 *            the username for the SyncAdapter account
	 * @param isSyncOperation
	 *            are we executing this as part of a sync operation?
	 * @return instance of CalendarOperations
	 */
	public static CalendarOperations createNewEvent(Context context,
			long userId, String accountName, boolean isSyncOperation,
			BatchOperation batchOperation) {

		return null;
	}

	public static CalendarOperations createNewEvent(Context context,
			long calendarId, ScheduleEvent event, String accountName,
			boolean isSyncOperation, BatchOperation batchOpeartion) {
		return new CalendarOperations(context, calendarId, event, accountName,
				isSyncOperation, batchOpeartion);
	}

	/**
	 * Returns an instance of CalendarOperations for updating existing contact
	 * in the platform contacts provider.
	 * 
	 * @param context
	 *            the Authenticator Activity context
	 * @param rawContactId
	 *            the unique Id of the existing rawContact
	 * @param isSyncOperation
	 *            are we executing this as part of a sync operation?
	 * @return instance of CalendarOperations
	 */
	public static CalendarOperations updateExistingEvent(Context context,
			long eventId, boolean isSyncOperation, BatchOperation batchOperation) {
		return new CalendarOperations(context, eventId, isSyncOperation,
				batchOperation);
	}

	public static CalendarOperations updateExistingEvent(Context context,
			ScheduleEvent event, boolean isSyncOperation,
			BatchOperation batchOperation) {
		return new CalendarOperations(context, event, isSyncOperation,
				batchOperation);
	}

	/**
	 * Adds a contact name. We can take either a full name ("Bob Smith") or
	 * separated first-name and last-name ("Bob" and "Smith").
	 * 
	 * @param fullName
	 *            The full name of the contact - typically from an edit form Can
	 *            be null if firstName/lastName are specified.
	 * @param firstName
	 *            The first name of the contact - can be null if fullName is
	 *            specified.
	 * @param lastName
	 *            The last name of the contact - can be null if fullName is
	 *            specified.
	 * @return instance of CalendarOperations
	 */
	public CalendarOperations addName(String fullName, String firstName,
			String lastName) {
		getmValues().clear();

		if (!TextUtils.isEmpty(fullName)) {
			getmValues().put(StructuredName.DISPLAY_NAME, fullName);
			getmValues().put(StructuredName.MIMETYPE,
					StructuredName.CONTENT_ITEM_TYPE);
		} else {
			if (!TextUtils.isEmpty(firstName)) {
				getmValues().put(StructuredName.GIVEN_NAME, firstName);
				getmValues().put(StructuredName.MIMETYPE,
						StructuredName.CONTENT_ITEM_TYPE);
			}
			if (!TextUtils.isEmpty(lastName)) {
				getmValues().put(StructuredName.FAMILY_NAME, lastName);
				getmValues().put(StructuredName.MIMETYPE,
						StructuredName.CONTENT_ITEM_TYPE);
			}
		}
		if (getmValues().size() > 0) {
			addInsertOp();
		}
		return this;
	}

	/**
	 * Adds an email
	 * 
	 * @param the
	 *            email address we're adding
	 * @return instance of CalendarOperations
	 */
	public CalendarOperations addEmail(String email) {
		getmValues().clear();
		if (!TextUtils.isEmpty(email)) {
			getmValues().put(Email.DATA, email);
			getmValues().put(Email.TYPE, Email.TYPE_OTHER);
			getmValues().put(Email.MIMETYPE, Email.CONTENT_ITEM_TYPE);
			addInsertOp();
		}
		return this;
	}

	/**
	 * Adds a phone number
	 * 
	 * @param phone
	 *            new phone number for the contact
	 * @param phoneType
	 *            the type: cell, home, etc.
	 * @return instance of CalendarOperations
	 */
	public CalendarOperations addPhone(String phone, int phoneType) {
		getmValues().clear();
		if (!TextUtils.isEmpty(phone)) {
			getmValues().put(Phone.NUMBER, phone);
			getmValues().put(Phone.TYPE, phoneType);
			getmValues().put(Phone.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
			addInsertOp();
		}
		return this;
	}

	/**
	 * Adds a group membership
	 * 
	 * @param id
	 *            The id of the group to assign
	 * @return instance of CalendarOperations
	 */
	public CalendarOperations addGroupMembership(long groupId) {
		getmValues().clear();
		getmValues().put(GroupMembership.GROUP_ROW_ID, groupId);
		getmValues().put(GroupMembership.MIMETYPE, GroupMembership.CONTENT_ITEM_TYPE);
		addInsertOp();
		return this;
	}

	public CalendarOperations addAvatar(String avatarUrl) {
		if (avatarUrl != null) {
			byte[] avatarBuffer = NetworkUtilities.downloadAvatar(avatarUrl);
			if (avatarBuffer != null) {
				getmValues().clear();
				getmValues().put(Photo.PHOTO, avatarBuffer);
				getmValues().put(Photo.MIMETYPE, Photo.CONTENT_ITEM_TYPE);
				addInsertOp();
			}
		}
		return this;
	}

	/**
	 * Adds a profile action
	 * 
	 * @param userId
	 *            the userId of the sample SyncAdapter user object
	 * @return instance of CalendarOperations
	 */
	public CalendarOperations addProfileAction(long userId) {
		getmValues().clear();
		if (userId != 0) {
			getmValues().put(SampleSyncAdapterColumns.DATA_PID, userId);
			getmValues().put(SampleSyncAdapterColumns.DATA_SUMMARY,
					mContext.getString(R.string.profile_action));
			getmValues().put(SampleSyncAdapterColumns.DATA_DETAIL,
					mContext.getString(R.string.view_profile));
			getmValues().put(Data.MIMETYPE, SampleSyncAdapterColumns.MIME_PROFILE);
			addInsertOp();
		}
		return this;
	}

	/**
	 * Updates contact's serverId
	 * 
	 * @param serverId
	 *            the serverId for this contact
	 * @param uri
	 *            Uri for the existing raw contact to be updated
	 * @return instance of CalendarOperations
	 */
	public CalendarOperations updateServerId(long serverId, Uri uri) {
		getmValues().clear();
		getmValues().put(Events._SYNC_ID, serverId);
		addUpdateOp(uri);
		return this;
	}

	/**
	 * Updates contact's email
	 * 
	 * @param email
	 *            email id of the sample SyncAdapter user
	 * @param uri
	 *            Uri for the existing raw contact to be updated
	 * @return instance of CalendarOperations
	 */
	public CalendarOperations updateEmail(String email, String existingEmail,
			Uri uri) {
		if (!TextUtils.equals(existingEmail, email)) {
			getmValues().clear();
			getmValues().put(Email.DATA, email);
			addUpdateOp(uri);
		}
		return this;
	}

	/**
	 * Updates contact's name. The caller can either provide first-name and
	 * last-name fields or a full-name field.
	 * 
	 * @param uri
	 *            Uri for the existing raw contact to be updated
	 * @param existingFirstName
	 *            the first name stored in provider
	 * @param existingLastName
	 *            the last name stored in provider
	 * @param existingFullName
	 *            the full name stored in provider
	 * @param firstName
	 *            the new first name to store
	 * @param lastName
	 *            the new last name to store
	 * @param fullName
	 *            the new full name to store
	 * @return instance of CalendarOperations
	 */
	public CalendarOperations updateName(Uri uri, String existingFirstName,
			String existingLastName, String existingFullName, String firstName,
			String lastName, String fullName) {

		getmValues().clear();
		if (TextUtils.isEmpty(fullName)) {
			if (!TextUtils.equals(existingFirstName, firstName)) {
				getmValues().put(StructuredName.GIVEN_NAME, firstName);
			}
			if (!TextUtils.equals(existingLastName, lastName)) {
				getmValues().put(StructuredName.FAMILY_NAME, lastName);
			}
		} else {
			if (!TextUtils.equals(existingFullName, fullName)) {
				getmValues().put(StructuredName.DISPLAY_NAME, fullName);
			}
		}
		if (getmValues().size() > 0) {
			addUpdateOp(uri);
		}
		return this;
	}

	public CalendarOperations updateDirtyFlag(boolean isDirty, Uri uri) {
		int isDirtyValue = isDirty ? 1 : 0;
		getmValues().clear();
		getmValues().put(Events.DIRTY, isDirtyValue);
		addUpdateOp(uri);
		return this;
	}

	/**
	 * Updates contact's phone
	 * 
	 * @param existingNumber
	 *            phone number stored in contacts provider
	 * @param phone
	 *            new phone number for the contact
	 * @param uri
	 *            Uri for the existing raw contact to be updated
	 * @return instance of CalendarOperations
	 */
	public CalendarOperations updatePhone(String existingNumber, String phone,
			Uri uri) {
		if (!TextUtils.equals(phone, existingNumber)) {
			getmValues().clear();
			getmValues().put(Phone.NUMBER, phone);
			addUpdateOp(uri);
		}
		return this;
	}

	public CalendarOperations updateAvatar(String avatarUrl, Uri uri) {
		if (avatarUrl != null) {
			byte[] avatarBuffer = NetworkUtilities.downloadAvatar(avatarUrl);
			if (avatarBuffer != null) {
				getmValues().clear();
				getmValues().put(Photo.PHOTO, avatarBuffer);
				getmValues().put(Photo.MIMETYPE, Photo.CONTENT_ITEM_TYPE);
				addUpdateOp(uri);
			}
		}
		return this;
	}

	/**
	 * Updates contact's profile action
	 * 
	 * @param userId
	 *            sample SyncAdapter user id
	 * @param uri
	 *            Uri for the existing raw contact to be updated
	 * @return instance of CalendarOperations
	 */
	public CalendarOperations updateProfileAction(Integer userId, Uri uri) {
		getmValues().clear();
		getmValues().put(SampleSyncAdapterColumns.DATA_PID, userId);
		addUpdateOp(uri);
		return this;
	}

	/**
	 * Adds an insert operation into the batch
	 */
	private void addInsertOp() {

		if (!mIsNewEvent) {
			getmValues().put(Reminders.EVENT_ID, mEventId);
			getmValues().put(Attendees.EVENT_ID, mEventId);
		}
		ContentProviderOperation.Builder builder = newInsertCpo(
				Data.CONTENT_URI, accountName, mIsSyncOperation, mIsYieldAllowed);
		builder.withValues(getmValues());
		if (mIsNewEvent) {
			builder.withValueBackReference(Data.RAW_CONTACT_ID, mBackReference);
		}
		mIsYieldAllowed = false;
		mBatchOperation.add(builder.build());
	}

	/**
	 * Adds an update operation into the batch
	 */
	private void addUpdateOp(Uri uri) {
		ContentProviderOperation.Builder builder = newUpdateCpo(uri,
				mIsSyncOperation, mIsYieldAllowed).withValues(getmValues());
		mIsYieldAllowed = false;
		mBatchOperation.add(builder.build());
	}

	public static ContentProviderOperation.Builder newInsertCpo(Uri uri, String accountName,
			boolean isSyncOperation, boolean isYieldAllowed) {
		return ContentProviderOperation.newInsert(
				addCallerIsSyncAdapterParameter(uri, isSyncOperation, accountName))
				.withYieldAllowed(isYieldAllowed);
	}

	public static ContentProviderOperation.Builder newUpdateCpo(Uri uri,
			boolean isSyncOperation, boolean isYieldAllowed) {
		return ContentProviderOperation.newUpdate(
				addCallerIsSyncAdapterParameter(uri, isSyncOperation)).withYieldAllowed(isYieldAllowed);
	}

	public static ContentProviderOperation.Builder newDeleteCpo(Uri uri,
			boolean isSyncOperation, boolean isYieldAllowed) {
		return ContentProviderOperation.newDelete(
				addCallerIsSyncAdapterParameter(uri, isSyncOperation))
				.withYieldAllowed(isYieldAllowed);
	}

	private static Uri addCallerIsSyncAdapterParameter(Uri uri,
			boolean isSyncOperation) {
		if (isSyncOperation) {
			// If we're in the middle of a real sync-adapter operation, then go
			// ahead
			// and tell the Contacts provider that we're the sync adapter. That
			// gives us some special permissions - like the ability to really
			// delete a contact, and the ability to clear the dirty flag.
			//
			// If we're not in the middle of a sync operation (for example, we
			// just
			// locally created/edited a new contact), then we don't want to use
			// the special permissions, and the system will automagically mark
			// the contact as 'dirty' for us!
			return uri
					.buildUpon()
					.appendQueryParameter(
							CalendarContract.CALLER_IS_SYNCADAPTER, "true")
					.build();
		}
		return uri;
	}

	private static Uri addCallerIsSyncAdapterParameter(Uri uri,
			boolean isSyncOperation, String accountName) {
		if (isSyncOperation) {
			// If we're in the middle of a real sync-adapter operation, then go
			// ahead
			// and tell the Contacts provider that we're the sync adapter. That
			// gives us some special permissions - like the ability to really
			// delete a contact, and the ability to clear the dirty flag.
			//
			// If we're not in the middle of a sync operation (for example, we
			// just
			// locally created/edited a new contact), then we don't want to use
			// the special permissions, and the system will automagically mark
			// the contact as 'dirty' for us!
			return uri
					.buildUpon()
					.appendQueryParameter(
							CalendarContract.CALLER_IS_SYNCADAPTER, "true")
					.appendQueryParameter(Calendars.ACCOUNT_NAME, accountName)
					.appendQueryParameter(Calendars.ACCOUNT_TYPE,
							Constants.ACCOUNT_TYPE).build();
		}
		return uri;
	}

	public CalendarOperations addTitle(String title) {
		// TODO Auto-generated method stub
		getmValues().clear();
		if (!TextUtils.isEmpty(title)) {
			getmValues().put(Events.TITLE, title);
			addInsertOp();
		}
		return this;
	}

	public ContentValues getmValues() {
		return mValues;
	}

	public void setmValues(ContentValues mValues) {
		this.mValues = mValues;
	}
}
