package com.tongji.share.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.model.UploadedFile;

public class FileManager {

	public FileManager() {

	}

	public boolean storeUploadedFile(UploadedFile uploadedFile,
			String pathString, String fileName) throws IOException {
		File f = new File(pathString, fileName);

		if (!f.exists()) {
			if (!makeFile(f)) {
				return false;
			}
		}

		InputStream inStream = uploadedFile.getInputstream();
		OutputStream outStream = new FileOutputStream(f);

		int readCount = 0;
		byte[] bytes = new byte[1024];

		while ((readCount = inStream.read(bytes)) != -1) {
			outStream.write(bytes, 0, readCount);
		}

		inStream.close();
		outStream.flush();
		outStream.close();

		return true;
	}

	public File storeUploadedFile(UploadedFile uploadedFile,
			String fileFullName) throws IOException {
		File f = new File(fileFullName);

		if (!f.exists()) {
			if (!makeFile(f)) {
				return null;
			}
		}

		InputStream inStream = uploadedFile.getInputstream();
		OutputStream outStream = new FileOutputStream(f);

		int readCount = 0;
		byte[] bytes = new byte[1024];

		while ((readCount = inStream.read(bytes)) != -1) {
			outStream.write(bytes, 0, readCount);
		}

		inStream.close();
		outStream.flush();
		outStream.close();

		return f;
	}

	private boolean makeFile(File f) throws IOException {
		if (!f.getParentFile().exists()) {
			if (!f.getParentFile().mkdirs()) {
				return false;
			}
		}
		return f.createNewFile();
	}

	public static String read(String fileFullName, String encoding) {
		StringBuffer fileContent = new StringBuffer();
		try {
			FileInputStream fis = new FileInputStream(fileFullName);
			InputStreamReader isr = new InputStreamReader(fis, encoding);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				fileContent.append(line);
				fileContent.append(System.getProperty("line.separator"));
			}
			br.close();
			isr.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileContent.toString();
	}

	public static void write(String fileContent, String fileFullName,
			String encoding) {
		try {
			FileOutputStream fos = new FileOutputStream(fileFullName);
			OutputStreamWriter osw = new OutputStreamWriter(fos, encoding);

			osw.write(fileContent);
			osw.flush();
			osw.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean moveFile(String srcFileName, String destFileName,
			boolean overlay) {
		File srcFile = new File(srcFileName);
		if (!srcFile.exists()) {
			System.out.println("movefile fail: src" + srcFileName
					+ "no exists!");
			return false;
		} else if (!srcFile.isFile()) {
			System.out.println("move file fail: src" + srcFileName
					+ "is not file!");
			return false;
		}

		File destFile = new File(destFileName);
		if (destFile.exists()) {
			if (overlay) {
				// delete file or dir
				if (delete(destFile.getAbsolutePath())) {
					System.out.println("move file fail: delete target"
							+ destFileName + "fail");
					return false;
				}
			} else {
				// TODO: rename?
			}
		} else if (!destFile.getParentFile().exists()) {
			if (!destFile.getParentFile().mkdirs()) {
				System.out.println("move file fail, mkdirs fail!");
				return false;
			}
		}

		// move源文件 to 目标文件
		if (srcFile.renameTo(destFile)) {
			System.out.println("move file" + srcFileName + " to "
					+ destFileName + "success!");
			return true;
		} else {
			System.out.println("move file" + srcFileName + " to "
					+ destFileName + "fail! ");
			return false;
		}
	}

	public boolean moveFiles(String srcDirName, String destDirName,
			boolean overlay) {
		// 判断源 dir 是否存在
		File srcDir = new File(srcDirName);
		if (!srcDir.exists()) {
			System.out.println("move dir  [FAIL] ：源 dir " + srcDirName + "不存在!");
			return false;
		} else if (!srcDir.isDirectory()) {
			System.out.println("move dir  [FAIL] ：" + srcDirName + "不是 dir !");
			return false;
		}
		// 如果目标 dir 名不易文件分隔符结束，添加文件分隔符
		if (!destDirName.endsWith(File.separator))
			destDirName = destDirName + File.separator;

		File destDir = new File(destDirName);
		// // 如果目标文件夹存在
		// if (destDir.exists()) {
		// // 如果允许覆盖， delete 已存在的目标 dir 
		// if (overlay) {
		// System.out.println("目标 dir 已存在，准备 delete 。。。。");
		// if (destDir.delete()) {
		// System.out.println("move dir  [FAIL] ： delete 目标 dir " + destDirName + " [FAIL] ！");
		// return false;
		// }
		// } else {
		// System.out.println("move dir  [FAIL] ：目标 dir " + destDirName + "已存在！");
		// return false;
		// }
		// }
		if (!destDir.exists()) {
			// 创建目标 dir 
			System.out.println("目标 dir 不存在，准备创建。。。。");
			if (!destDir.mkdirs()) {
				System.out.println("move dir  [FAIL] ：创建目标 dir  [FAIL] ！");
				return false;
			}
		}

		boolean flag = true;
		// move源 dir 下的文件和子 dir 到目标 dir 下
		File[] files = srcDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			// move子文件
			if (files[i].isFile()) {
				flag = moveFile(files[i].getAbsolutePath(), destDirName
						+ files[i].getName(), overlay);
				if (!flag)
					break;
			}
			// move子 dir 
			if (files[i].isDirectory()) {
				flag = moveFiles(files[i].getAbsolutePath(), destDirName
						+ files[i].getName(), overlay);
				if (!flag)
					break;
			}
		}

		if (!flag) {
			System.out.println("move dir " + srcDirName + " to " + destDirName + " [FAIL] ！");
			return false;
		}

		//  delete 源 dir 
		if (delete(srcDirName)) {
			System.out.println("move dir " + srcDirName + " to " + destDirName + " [successful]！");
			return true;
		} else {
			System.out.println("move dir " + srcDirName + " to " + destDirName + " [FAIL] ！");
			return false;
		}
	}

	public boolean moveFilesWithDir(String srcDirName, String destDirName,
			boolean overlay) {
		// 判断源 dir 是否存在
		File srcDir = new File(srcDirName);
		if (!srcDir.exists()) {
			System.out.println("move dir  [FAIL] ：源 dir " + srcDirName + "不存在!");
			return false;
		} else if (!srcDir.isDirectory()) {
			System.out.println("move dir  [FAIL] ：" + srcDirName + "不是 dir !");
			return false;
		}
		// 如果目标 dir 名不易文件分隔符结束，添加文件分隔符
		if (!destDirName.endsWith(File.separator))
			destDirName = destDirName + File.separator;

		File destDir = new File(destDirName);
		// // 如果目标文件夹存在
		// if (destDir.exists()) {
		// // 如果允许覆盖， delete 已存在的目标 dir 
		// if (overlay) {
		// System.out.println("目标 dir 已存在，准备 delete 。。。。");
		// if (destDir.delete()) {
		// System.out.println("move dir  [FAIL] ： delete 目标 dir " + destDirName + " [FAIL] ！");
		// return false;
		// }
		// } else {
		// System.out.println("move dir  [FAIL] ：目标 dir " + destDirName + "已存在！");
		// return false;
		// }
		// }
		if (!destDir.exists()) {
			// 创建目标 dir 
			System.out.println("目标 dir 不存在，准备创建。。。。");
			if (!destDir.mkdirs()) {
				System.out.println("move dir  [FAIL] ：创建目标 dir  [FAIL] ！");
				return false;
			}
		}

		boolean flag = true;
		// move源 dir 下的文件和子 dir 到目标 dir 下
		File[] files = srcDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			// move子文件
			if (files[i].isFile()) {
				flag = moveFile(files[i].getAbsolutePath(), destDirName
						+ files[i].getName(), overlay);
				if (!flag)
					break;
			}
			// move子 dir 
			if (files[i].isDirectory()) {
				flag = moveFiles(files[i].getAbsolutePath(), destDirName
						+ files[i].getName(), overlay);
				if (!flag)
					break;
			}
		}

		if (!flag) {
			System.out.println("move dir " + srcDirName + " to " + destDirName + " [FAIL] ！");
			return false;
		}

		//  delete 源 dir 
		if (delete(srcDirName)) {
			System.out.println("move dir " + srcDirName + " to " + destDirName + " [successful]！");
			return true;
		} else {
			System.out.println("move dir " + srcDirName + " to " + destDirName + " [FAIL] ！");
			return false;
		}
	}

	private boolean deleteDir(String dirName) {
		// 如果dirName不以文件分隔符结尾，自动添加文件分隔符
		if (!dirName.endsWith(File.separator)) {
			dirName = dirName + File.separator;
		}
		File file = new File(dirName);
		if (!file.isDirectory() || !file.exists()) {
			System.out.println("delete dir fail: " + dirName
					+ " dir not exists!");
			return false;
		}
		//  delete 文件夹下的所有文件(包括子 dir )
		boolean flag = true;
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			//  delete 子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag) {
					break;
				}
			}
			//  delete 子 dir 
			else {
				flag = deleteDir(files[i].getAbsolutePath());
				if (!flag) {
					break;
				}
			}
		}

		if (!flag) {
			System.out.println("delete dir fail");
			return false;
		}

		//  delete 当前 dir 
		if (file.delete()) {
			System.out.println("delete dir " + file + "success!");
			return true;
		} else {
			System.out.println("delete dir " + file + "fail!");
			return false;
		}
	}

	private boolean deleteFile(String fileName) {
		File file = new File(fileName);
		return deleteFile(file);
	}
	
	private boolean deleteFile(File file){
		if (file.isFile() && file.exists()) {
			file.delete();
			System.out.println("delete file" + file.getAbsolutePath() + "success!");
			return true;
		}
		System.out.println("delete file" + file.getAbsolutePath() + "fail!");
		return false;
	}

	public boolean delete(String fullOrDirPath) {
		File file = new File(fullOrDirPath);
		return delete(file);
	}
	
	public boolean delete(File fileOrDir){
		if (!fileOrDir.exists()) {
			System.out.println("delete file or dir fail: " + fileOrDir.getAbsolutePath()
					+ "not exist!");
			return false;
		} else {
			if (fileOrDir.isFile()) {
				return deleteFile(fileOrDir);
			} else {
				return deleteDir(fileOrDir.getAbsolutePath());
			}
		}
	}

	public List<String> loadFilesName(String fileDirName) {
		// TODO Auto-generated method stub
		List<String> fileNameList = new ArrayList<String>();
		if (!fileDirName.endsWith(File.separator)) {
			fileDirName = fileDirName + File.separator;
		}
		File dir = new File(fileDirName);
		if (!dir.exists()) {
			return null;
		}
		if (dir.isFile()) {
			return null;
		}
		if (!dir.isDirectory()) {
			return null;
		}
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				fileNameList.add(file.getName());
			}else {
				fileNameList.addAll(loadFilesName(file.getAbsolutePath()));
			}
		}

		return fileNameList;
	}

	public List<File> loadFiles(String fileDirName){
		List<File> fileList = new ArrayList<File>();
		if (!fileDirName.endsWith(File.separator)) {
			fileDirName = fileDirName + File.separator;
		}
		File dir = new File(fileDirName);
		if (!dir.exists()) {
			return null;
		}
		if (dir.isFile()) {
			return null;
		}
		if (!dir.isDirectory()) {
			return null;
		}
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				fileList.add(file);
			}else {
				fileList.addAll(loadFiles(file.getAbsolutePath()));
			}
		}

		return fileList;
		
	}
}
