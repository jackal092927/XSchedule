package com.tongji.activityinfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;


@Named
@SessionScoped
public class TableBean implements Serializable {
	 
        String[] title = new String[10]; 
        
  
        String[] publisher = new String[10];  
        
        
        String[] location = new String[10];
        
   
  
//    private final static String[] titles;  
//    private final static String[] publishers;   
//    private final static String[] locations;  
  
    
        
    private List<Activity> activitiesSmall;  
      
    private Activity selectedActivity;  
  
    private ActivityDataModel mediumActivitiesModel;  
  
    public TableBean() {  
    	ini();
    	
    	activitiesSmall = new ArrayList<Activity>();  
          
    	populateActivitiesSmall(activitiesSmall, 50);
        //populateRandomCars(activitiesSmall, 50);  
  
        mediumActivitiesModel = new ActivityDataModel(activitiesSmall);  
    }  
    
    private void ini(){
    	title[0] = "Black";  
        title[1] = "White";  
        title[2] = "Green";  
        title[3] = "Red";  
        title[4] = "Blue";  
        title[5] = "Orange";  
        title[6] = "Silver";  
        title[7] = "Yellow";  
        title[8] = "Brown";  
        title[9] = "Maroon";  
        
        publisher[0] = "Mercedes";  
        publisher[1] = "BMW";  
        publisher[2] = "Volvo";  
        publisher[3] = "Audi";  
        publisher[4] = "Renault";  
        publisher[5] = "Opel";  
        publisher[6] = "Volkswagen";  
        publisher[7] = "Chrysler";  
        publisher[8] = "Ferrari";  
        publisher[9] = "Ford";  
        
        location[0] = "Tongji";
        location[1] = "Tongji";
        location[2] = "Tongji";
        location[3] = "Tongji";
        location[4] = "Tongji";
        location[5] = "Tongji";
        location[6] = "Tongji";
        location[7] = "Tongji";
        location[8] = "Tongji";
        location[9] = "Tongji";
        
    }
    
    private void populateActivitiesSmall(List<Activity> list, int size) {
    	for (int i = 0; i < size; i++) {
			list.add(new Activity(title[i%title.length], publisher[i%publisher.length], location[i%location.length]));
		}
    }
  
//    private void populateRandomCars(List<Car> list, int size) {  
//        for(int i = 0 ; i < size ; i++)  
//            list.add(new Activity(getRandomModel(), getRandomYear(), getRandomManufacturer(), getRandomColor()));  
//    }  
  
    public Activity getSelectedActivity() {  
        return selectedActivity;  
    }  
    public void setSelectedActivity(Activity selectedActivity) {  
        this.selectedActivity = selectedActivity;  
    }  
  
    public ActivityDataModel getMediumActivitiesModel() {  
        return mediumActivitiesModel;  
    }  
  
    public void onRowSelect(SelectEvent event) {  
    	FacesMessage msg = new FacesMessage("Activity Selected", ((Activity) event.getObject()).getTitle());  
  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    }  
  
    public void onRowUnselect(UnselectEvent event) {  
        FacesMessage msg = new FacesMessage("Activity Unselected", ((Activity) event.getObject()).getTitle());  
  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    } 
}
