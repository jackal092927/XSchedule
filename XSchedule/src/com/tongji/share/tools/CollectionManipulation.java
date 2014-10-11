package com.tongji.share.tools;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.tongji.persistence.models.UserEventRelation;
import com.tongji.share.view.models.TransferItem;

public class CollectionManipulation {
	public static void ListRemoveSetWithKey(List<UserEventRelation> source, Set<UserEventRelation> target, int eventId){
		
		Iterator<UserEventRelation> it = source.iterator();
		while (it.hasNext()) {
			UserEventRelation temp = it.next();
			temp.setRelatedActivityId(eventId);
			if(target.contains(temp)){
				it.remove();
			}
		}
	}

	
}
