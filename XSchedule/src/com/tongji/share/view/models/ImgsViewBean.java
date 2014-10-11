package com.tongji.share.view.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.tongji.share.beans.FileBean;
import com.tongji.share.view.EventFile;


@ManagedBean
@SessionScoped
public class ImgsViewBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9219869056102670591L;

	private List imgList; 
	
	public ImgsViewBean(){
		
	}

	public List getImgList() {
		return imgList;
	}

	public void setImgList(List imgList) {
		this.imgList = imgList;
	}
	
	public void setImgList(EventFile img){
		this.imgList.clear();
		imgList.add(img);
//		imgList.add(img);
	}
	
	public void loadImgsFrom(Object srcObject){
		if (srcObject instanceof FileBean) {
			
			
		}
		
	}
}
