package com.tongji.jackal.webservice.server.android;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.tongji.activityinfo.IActivityService;
import com.tongji.persistence.models.ActivityModel;
import com.tongji.share.tools.ModelsUtil;

public class CalendarSyncService implements ISyncService {

	private IActivityService activityService ;

	public List exchangeDataList(List<ScheduleEvent4JSON> clientList,
			List<ScheduleEvent4JSON> serverList) {

		Collections.sort(clientList, new Comparator<ScheduleEvent4JSON>() {
			public int compare(ScheduleEvent4JSON event1,
					ScheduleEvent4JSON event2) {
				return (int) (event1.getServerId() - event2.getServerId());
			}
		});

		Collections.sort(serverList, new Comparator<ScheduleEvent4JSON>() {
			public int compare(ScheduleEvent4JSON event1,
					ScheduleEvent4JSON event2) {
				return (int) (event1.getServerId() - event2.getServerId());
			}
		});

		List<ScheduleEvent4JSON> uList = new ArrayList<ScheduleEvent4JSON>();

		// Iterator<ScheduleEvent4JSON> clientIt = clientList.iterator();
		// Iterator<ScheduleEvent4JSON> serverIt = serverList.iterator();

		ScheduleEvent4JSON clientEvent;
		ScheduleEvent4JSON serverEvent;

		int clientIndex = 0;
		int serverIndex = 0;
		int clientSize = clientList.size();
		int serverSize = serverList.size();

		boolean flag = true;
		while (flag && clientIndex < clientSize) {
			clientEvent = clientList.get(clientIndex);
			if (clientEvent.getServerId() == -1
					|| clientEvent.getServerId() == 0) {
				clientUpdateServerId(clientEvent);
				uList.add(clientEvent);
				clientIndex ++;
			} else {
				flag = false;
			}
		}

		// flag = !flag;// flag==false: clientIt!=null; flag==true:
		// clientIt==null
		/*
		 * while (clientIndex < clientSize && serverIndex < serverSize) {
		 * clientEvent = clientList.get(clientIndex); serverEvent =
		 * serverList.get(serverIndex); while (clientIndex < clientSize &&
		 * (clientEvent = clientList.get(clientIndex)) .getServerId() <
		 * serverEvent.getServerId()) { // : client update server clientIndex++;
		 * } while (clientIndex < clientSize && serverIndex < serverSize &&
		 * (clientEvent = clientList.get(clientIndex)) .getServerId() ==
		 * serverEvent.getServerId()) { // : compare timestamp clientIndex++; }
		 * if (clientIndex < clientSize) { clientEvent =
		 * clientList.get(clientIndex); while (serverIndex < clientIndex &&
		 * (serverEvent = serverList.get(serverIndex)) .getServerId() <
		 * clientEvent.getServerId()) { // : uList.add(serverEvent);
		 * serverIndex++;
		 * 
		 * } } }
		 */

		while (clientIndex < clientSize || serverIndex < serverSize) {

			flag = true;
			while (flag && clientIndex < clientSize)/*
													 * && (clientEvent =
													 * clientList
													 * .get(clientIndex))
													 * .getServerId() <
													 * serverEvent
													 * .getServerId())
													 */{
				clientEvent = clientList.get(clientIndex);
				if (serverIndex < serverSize) {
					serverEvent = serverList.get(serverIndex);
					if (clientEvent.getServerId() < serverEvent.getServerId()) {
						clientUpdateServer(clientEvent);
						clientIndex++;
					} else {
						flag = false;
					}
				} else {
					clientUpdateServer(clientEvent);
					clientIndex++;
				}
			}

			flag = true;
			while (flag && clientIndex < clientSize
			/*
			 * && serverIndex < serverSize && (clientEvent =
			 * clientList.get(clientIndex)) .getServerId() ==
			 * serverEvent.getServerId()
			 */
			) {
				clientEvent = clientList.get(clientIndex);
				if (serverIndex < serverSize) {
					serverEvent = serverList.get(serverIndex);
					if (clientEvent.getServerId() == serverEvent.getServerId()) {
						if (mergeEvents(clientEvent, serverEvent)) {
							uList.add(clientEvent);
						}
						clientIndex++;
					} else {
						flag = false;
					}
				}
			}
			flag = true;
			while (flag && serverIndex < serverSize) {
				serverEvent = serverList.get(serverIndex);
				if (clientIndex < clientSize) {
					clientEvent = clientList.get(clientIndex);
					if (serverEvent.getServerId() < clientEvent.getServerId()) {
						serverUpdateClient(serverEvent);
						uList.add(serverEvent);
						serverIndex++;
					} else {
						flag = false;
					}
				} else {
					serverUpdateClient(serverEvent);
					uList.add(serverEvent);
					serverIndex++;
				}
			}
		}


		return uList;
	}

	private void serverUpdateClient(ScheduleEvent4JSON serverEvent) {
		/*
		 * if ( serverEvent.isValid() ) { //update event }else { //set valid
		 * flase }
		 */
		// just add to uList...
	}

	private boolean mergeEvents(ScheduleEvent4JSON clientEvent,
			ScheduleEvent4JSON serverEvent) {
		if (clientEvent.getLastUpdateTime().after(
				serverEvent.getLastUpdateTime())) {
			clientUpdateServer(clientEvent);
			return false;
		}
		serverUpdateClient(serverEvent);
		return true;
	}

	private void clientUpdateServerId(ScheduleEvent4JSON clientEvent) {
		// store event;
		// get id
		if (clientEvent.isValid()) {
			ActivityModel eventModel = ModelsUtil
					.event4JsonToEvent(clientEvent);
			clientEvent.setServerId(activityService.addEvent(eventModel));
			clientEvent.setLastUpdateTime(eventModel.getLastUpdateTime());
			clientEvent.setClientNew(true);
		}

	}

	private void clientUpdateServer(ScheduleEvent4JSON clientEvent) {
		if (clientEvent.isValid()) {
			// update event;
			activityService.updateEvent(ModelsUtil
					.event4JsonToEvent(clientEvent));
		} else {
			// set valid false
			int id = new Long(clientEvent.getServerId()).intValue();
			activityService.deleteById(id);
		}
	}

	public IActivityService getActivityService() {
		return activityService;
	}

	public void setActivityService(IActivityService activityService) {
		this.activityService = activityService;
	}

}
