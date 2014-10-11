package com.tongji.share.view;

import java.io.File;

public class EventFile {

	private File file;

	private String resourcePathName = "hello.txt";
	private int listIndex = -1;//
	private boolean marked = false;

	public EventFile() {

	}

	public EventFile(File file) {
		this.file = file;
	}

	public EventFile(EventFile eventFile) {
		file = eventFile.getFile();
		listIndex = eventFile.getListIndex();
	}

	public int getListIndex() {
		return listIndex;
	}

	public void setListIndex(int listIndex) {
		this.listIndex = listIndex;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getResourcePathName() {
		return resourcePathName;
	}

	public void setResourcePathName(String resourcePathName) {
		this.resourcePathName = resourcePathName;
	}

	public boolean isMarked() {
		return marked;
	}

	public void setMarked(boolean marked) {
		this.marked = marked;
	}

	public String getUrl() {
		String url = "localhost:8080/XSchedule/resources/events/"
				+ resourcePathName;
		return url;
	}

	public String getRelatedUrl() {
		String relatedUrl = "../resources/events/" + resourcePathName;
		return relatedUrl;
	}

}
