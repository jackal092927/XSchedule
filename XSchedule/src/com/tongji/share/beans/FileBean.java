package com.tongji.share.beans;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultUploadedFile;
import org.primefaces.model.UploadedFile;

import com.tongji.basicinfo.MyScheduleEvent;
import com.tongji.share.tools.FileManager;
import com.tongji.share.tools.ModelsUtil;
import com.tongji.share.tools.MyEnum.FileType;
import com.tongji.share.tools.MyFacesContextUtils;
import com.tongji.share.view.EventFile;
import com.tongji.share.view.EventImgFile;

public class FileBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3247758223689548604L;
	// private UploadedFile file = new DefaultUploadedFile();
	private final String tempId = UUID.randomUUID().toString();
	private String uuid;
	private String defaultImgName;
	// private List<String> fileNames = new ArrayList<String>();
	// private List<EventFile> files = new ArrayList<EventFile>();
	private EventImgFile defaultImg;
	private EventFile currentFile = new EventFile();

	private List<EventFile> imgList = new ArrayList<EventFile>();
	private List<EventFile> materialList = new ArrayList<EventFile>();
	private List<EventFile> othersList = new ArrayList<EventFile>();

	private FileListModel imgListModel;
	private FileListModel materialListModel;
	private FileListModel othersListModel;

	private List<EventFile> selectedFiles = new ArrayList<EventFile>();

	private FileType currentFileType = FileType.IMG;

	private int fileCount;

	public FileBean() {

	}

	public FileBean(MyScheduleEvent event) {
		uuid = event.getUuid();
		defaultImgName = event.getEvent().getImageDefault();

		loadFiles();
	}

	public int getCurrentIndex() {
		return currentFile.getListIndex() + 1;
	}

	public String getCurrentFilePath() {
		File file = currentFile.getFile();
		if (file != null) {
			return file.getAbsolutePath();
		} else {
			return null;
		}
	}

	public void remove() {
		// if (getListSize() >= 1) {
		// int currentIndex = currentFile.getListIndex();
		// currentIndex = ++currentIndex % getListSize();
		//
		// }
		getCurrentFile();
	}

	// public void loadFilesName(FileType fileType) {
	// switch (fileType) {
	// case IMG:
	// if (this.uuid != null) {
	// String fileDirName = MyFacesContextUtils.getEventImgsDir(uuid);
	// FileManager fileManager = new FileManager();
	// List<String> fileNameList = fileManager
	// .loadFilesName(fileDirName);
	// if (fileNameList != null) {
	// for (String fileName : fileNameList) {
	// fileNames.add(uuid + "/imgs/" + fileName);
	// }
	// }
	//
	// }
	// if (this.tempId != null) {
	// String fileDirName = MyFacesContextUtils
	// .getEventTempDir(tempId);
	// FileManager fileManager = new FileManager();
	// List<String> fileNameList = fileManager
	// .loadFilesName(fileDirName);
	// if (fileNameList != null) {
	// for (String fileName : fileNameList) {
	// fileNames.add("share/temp/" + tempId + "/" + fileName);
	//
	// }
	// }
	// }
	// break;
	//
	// default:
	// break;
	// }
	// }

	public void loadFiles() {
		loadFiles(FileType.IMG);
		loadFiles(FileType.MATERIAL);
		loadFiles(FileType.OTHERS);
	}

	public void loadFiles(FileType fileType) {
		String typeString = File.separator + fileType.toString()
				+ File.separator;
		List eventFileList = getfileList(fileType);

		if (this.uuid != null) {
			String fileDirName = MyFacesContextUtils.getResourcesEventDir()
					+ uuid + typeString;
			FileManager fileManager = new FileManager();
			List<File> fileList = fileManager.loadFiles(fileDirName);
			if (fileList != null) {
				for (File file : fileList) {
					EventFile eventFile = ModelsUtil.fileToEventFile(file,
							fileType);
					if (fileType.equals(FileType.IMG) && file.getName().equals(defaultImgName)) {
						eventFile.setMarked(true);
						defaultImg = (EventImgFile) eventFile;
					}
					eventFile.setResourcePathName(uuid + typeString
							+ file.getName());
					eventFile.setListIndex(eventFileList.size());

					eventFileList.add(eventFile);

				}
			}

		}
		if (this.tempId != null) {
			String fileDirName = MyFacesContextUtils.getEventTempDir(tempId)
					+ typeString;
			FileManager fileManager = new FileManager();
			List<File> fileList = fileManager.loadFiles(fileDirName);
			if (fileList != null) {
				for (File file : fileList) {
					EventFile eventFile = ModelsUtil.fileToEventFile(file,
							fileType);
					if (file.getName().equals(defaultImgName)) {
						eventFile.setMarked(true);
						defaultImg = (EventImgFile) eventFile;
					}
					eventFile.setResourcePathName("share/temp/" + tempId
							+ typeString + file.getName());
					eventFile.setListIndex(eventFileList.size());
					eventFileList.add(eventFile);
				}
			}
		}

		// //
		// if (files.size() > 0) {
		// currentFile = files.get(0);
		// }
		// //

	}

	private void loadDefaultImg() {
		// TODO:
	}

	private List<EventFile> getfileList(FileType fileType) {
		switch (fileType) {
		case IMG:
			return imgList;

		case MATERIAL:
			return materialList;

		case OTHERS:
			return othersList;

		default:
			return null;
		}
	}

	private List<EventFile> getCurrentFileList() {
		return getfileList(currentFileType);
	}

	private FileListModel getFileListModel(FileType fileType) {
		switch (fileType) {
		case IMG:
			return imgListModel;

		case MATERIAL:
			return materialListModel;

		case OTHERS:
			return othersListModel;

		default:
			return null;
		}
	}

	/**
	 * 
	 * @param event
	 * @throws IOException
	 */
	public void handleFileUpload(FileUploadEvent event) throws IOException {
		UploadedFile uploadedFile = event.getFile();
		// setFile(event.getFile());
		String typeString = File.separator + currentFileType.toString()
				+ File.separator;
		List fileList = getCurrentFileList();

		try {
			if (uploadedFile != null) {
				// String tempId = fileBean.getTempId();
				// if (tempId == null) {
				// tempId = UUID.randomUUID().toString();
				// }

				String dirString = MyFacesContextUtils.getEventTempDir(tempId)
						+ typeString;
				File file = storeFile(dirString, uploadedFile);

				// String uuid = this.event.getUuid();
				// if (uuid != null) {
				// // TODO:
				// ServletContext ctx = (ServletContext) FacesContext
				// .getCurrentInstance().getExternalContext()
				// .getContext();
				// String pathString = ctx.getRealPath("/")
				// + "resources/events/" + uuid + "/imgs/";
				// fileBean.storeFile(pathString);
				// } else {
				// // TODO:
				// String pathString = "temp";
				// fileBean.storeFile(pathString);
				// }

				// this.event.setImageDefault(fileBean.getFile().getFileName());
				// TODO: to be simplified;
				// currentFile = new EventFile(file);
				EventFile eventFile = ModelsUtil.fileToEventFile(file,
						currentFileType);
				eventFile.setListIndex(getCurrentFileList().size());
				String absolutePath = eventFile.getFile().getAbsolutePath();
				String searchSubString = "resources\\events\\";
				int beginIndex = absolutePath.lastIndexOf(searchSubString)
						+ searchSubString.length();
				String resourcePath = absolutePath.substring(beginIndex);
				// eventFile.setResourcePathName("share/temp/" + tempId
				// + typeString + file.getName());
				eventFile.setResourcePathName(resourcePath);
				fileList.add(eventFile);
				getFileListModel(currentFileType).setWrappedData(fileList);

				// updateCount();
				// fileCount++;
			}
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
			FacesMessage msg = new FacesMessage("Fail",
					uploadedFile.getFileName() + " upload fail.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public String getCurrentFileResourcePathName() {
		return currentFile.getResourcePathName();
	}

	public void moveNext() {
		int currentIndex = currentFile.getListIndex();
		currentIndex = ++currentIndex % getListSize();
		currentFile = (EventFile) getCurrentFileList().get(currentIndex);
	}

	public void movePre() {
		int currentIndex = currentFile.getListIndex();

		if (--currentIndex < 0) {
			currentIndex = getListSize();
		}
		currentFile = (EventFile) getCurrentFileList().get(currentIndex);
	}

	// public File storeFile(String pathString) throws IOException {
	// // TODO:
	// String fileFullName = pathString + file.getFileName();
	// FileManager fileManager = new FileManager();
	// File file = fileManager.storeUploadedFile(this.file, fileFullName);
	// return file;
	// }

	private File storeFile(String pathString, UploadedFile uploadedFile)
			throws IOException {
		String fileFullName = pathString + uploadedFile.getFileName();
		FileManager fileManager = new FileManager();
		File file = fileManager.storeUploadedFile(uploadedFile, fileFullName);
		return file;
	}

	public boolean mergeFile(String srcFileName, String destFileName,
			boolean overlay) {
		// TODO Auto-generated method stub
		FileManager fileManager = new FileManager();
		return fileManager.moveFiles(srcFileName, destFileName, overlay);
	}

	/**
	 * 
	 * @param fileFullName
	 *            : file.getAbsolutePath() or dir
	 * @return
	 */
	public boolean removeFileOrDir(String fileOrDirAbsolutePath) {
		FileManager fileManager = new FileManager();
		return fileManager.delete(fileOrDirAbsolutePath);
	}

	public void onFileTypeChange(TabChangeEvent event) {
		String typeString = event.getTab().getTitle();
		if ("Images".equals(typeString)) {
			currentFileType = FileType.IMG;
		}else if("Materials".equals(typeString)){
			currentFileType = FileType.MATERIAL;
		}else if ("Others".equals(typeString)) {
			currentFileType = FileType.OTHERS;
		}
	}

	public void delete() {
		if (selectedFiles != null && selectedFiles.size() > 0) {
			for (EventFile eventFile : selectedFiles) {
				removeFileOrDir(eventFile.getFile().getAbsolutePath());
				List fileList = getCurrentFileList();
				int removeIndex = eventFile.getListIndex();
				fileList.remove(removeIndex);
				updateListAfterRemove(fileList, removeIndex);
				if (eventFile.isMarked()) {
					defaultImg = null;
				}
			}
			selectedFiles = new ArrayList<EventFile>();
			getFileListModel(currentFileType).setWrappedData(
					getCurrentFileList());
			if (defaultImg == null && getCurrentFileList().size() > 0) {
				defaultImg = (EventImgFile) getCurrentFileList().get(0);
				defaultImg.setMarked(true);
			}
		}
	}

	private void updateListAfterRemove(List<EventFile> list, int index) {
		for (int i = index; i < list.size(); i++) {
			list.get(i).setListIndex(i);
		}
	}

	public void view() {
		int i = 0;
		i++;
	}

	public void mark(int rowKey) {
		if (rowKey != defaultImg.getListIndex()) {
			defaultImg.setMarked(false);
			defaultImg = (EventImgFile) getCurrentFileList().get(rowKey);
			defaultImg.setMarked(true);
		}
	}

	// ///SET GET/////
	// public UploadedFile getFile() {
	// return file;
	// }
	//
	// public void setFile(UploadedFile file) {
	// this.file = file;
	// }

	public int getFileCount() {
		return fileCount;
	}

	public void setFileCount(int fileCount) {
		this.fileCount = fileCount;
	}

	public String getTempId() {
		return tempId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void updateCount() {
		this.fileCount++;
	}

	// public List<String> getFileNames() {
	// return fileNames;
	// }
	//
	// public void setFileNames(List<String> files) {
	// this.fileNames = files;
	// }

	public EventFile getCurrentFile() {
		return currentFile;
	}

	public void setCurrentFileName(EventFile currentFile) {
		this.currentFile = currentFile;
	}

	// public List<EventFile> getFiles() {
	// return files;
	// }
	//
	// public void setFiles(List<EventFile> files) {
	// this.files = files;
	// }

	public void setCurrentFile(EventFile currentFile) {
		this.currentFile = currentFile;
	}

	public FileType getCurrentFileType() {
		return currentFileType;
	}

	public void setCurrentFileType(FileType currentFileType) {
		this.currentFileType = currentFileType;
	}

	public EventImgFile getDefaultImg() {
		return defaultImg;
	}

	public void setDefaultImg(EventImgFile defaultImg) {
		this.defaultImg = defaultImg;
	}

	public List<EventFile> getImgList() {
		return imgList;
	}

	public void setImgList(List<EventFile> imgList) {
		this.imgList = imgList;
	}

	public List<EventFile> getMaterialList() {
		return materialList;
	}

	public void setMaterialList(List<EventFile> materialList) {
		this.materialList = materialList;
	}

	public List<EventFile> getOthersList() {
		return othersList;
	}

	public void setOthersList(List<EventFile> othersList) {
		this.othersList = othersList;
	}

	public List<EventFile> getSelectedFiles() {
		return selectedFiles;
	}

	public void setSelectedFiles(List<EventFile> selectedFiles) {
		this.selectedFiles = selectedFiles;
	}
	
	public EventFile getSelectedFile(int row){
		return getCurrentFileList().get(row);
	}

	public void setSelectedFile(int row) {
		selectedFiles = new ArrayList<EventFile>();
		selectedFiles.add(getCurrentFileList().get(row));
	}

	public FileListModel getImgListModel() {
		if (imgListModel == null) {
			imgListModel = new FileListModel(imgList);
		}
		return imgListModel;
	}

	public void setImgListModel(FileListModel imgListModel) {
		this.imgListModel = imgListModel;
	}

	public FileListModel getMaterialListModel() {
		if (materialListModel == null) {
			materialListModel = new FileListModel(materialList);
		}
		return materialListModel;
	}

	public void setMaterialListModel(FileListModel materialListModel) {
		this.materialListModel = materialListModel;
	}

	public FileListModel getOthersListModel() {
		if (othersListModel == null) {
			othersListModel = new FileListModel(othersList);
		}
		return othersListModel;
	}

	public void setOthersListModel(FileListModel othersListModel) {
		this.othersListModel = othersListModel;
	}

	public String getDefaultImgName() {
		return defaultImgName;
	}

	public void setDefaultImgName(String defaultImgName) {
		this.defaultImgName = defaultImgName;
	}

	///// my set get /////
	public int getListSize(){
		return getCurrentFileList().size();
	}
}
