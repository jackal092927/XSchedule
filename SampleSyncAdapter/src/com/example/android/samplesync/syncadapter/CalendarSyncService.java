package com.example.android.samplesync.syncadapter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CalendarSyncService extends Service {

	private static final Object sSyncAdapterLock = new Object();

	private static CalendarSyncAdapter sSyncAdapter = null;

	@Override
	public void onCreate() {
		synchronized (sSyncAdapterLock) {
			if (sSyncAdapter == null) {
				sSyncAdapter = new CalendarSyncAdapter(getApplicationContext(), true);
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return sSyncAdapter.getSyncAdapterBinder();
	}

}
