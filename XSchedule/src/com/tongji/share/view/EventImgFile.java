package com.tongji.share.view;

import java.io.File;

public class EventImgFile extends EventFile{
	
	public EventImgFile(){
		
	}
	
	public EventImgFile(File file){
		super(file);
	}
	
	public EventImgFile(EventFile eventFile){
		super(eventFile);
	}
	
}
