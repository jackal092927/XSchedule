package com.tongji.activityinfo;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class ActivityDataModel extends ListDataModel<Activity> implements SelectableDataModel<Activity> {

	
	public ActivityDataModel(){
		
	}
	
	public ActivityDataModel(List<Activity> data) {  
        super(data);  
    }  
	
	
	@Override
	public Activity getRowData(String rowKey) {
		List<Activity> activities = (List<Activity>) getWrappedData();  
        
        for(Activity activity : activities) {  
            if(activity.getTitle().equals(rowKey))  
                return activity;  
        }  
          
        return null; 
	}

	@Override
	public Object getRowKey(Activity arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
