package com.tongji.share.beans;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.tongji.share.view.EventFile;

public class FileListModel extends ListDataModel<EventFile> implements SelectableDataModel<EventFile>{
	
	public FileListModel(){
		
	}
	
	public FileListModel(List<EventFile> fileList){
		super(fileList);
	}

	@Override
	public Object getRowKey(EventFile object) {
		return object.getListIndex();
	}

	@Override
	public EventFile getRowData(String rowKey) {
		List<EventFile> fileList = (List)getWrappedData();
		return fileList.get(Integer.valueOf(rowKey));
		// TODO Auto-generated method stub
	}
	
	

}
