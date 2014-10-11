package com.tongji.java.test;

import java.io.File;

import org.junit.Test;

import com.tongji.share.tools.FileManager;

public class TestFileManager {
	@Test
	public void Test(){
		FileManager fileManager = new FileManager();
		
		String srcDirName = "E:/test/hello";
		String destDirName = "E:/test/world";
		
		File f = new File(destDirName);
		
//		fileManager.moveFiles(srcDirName, destDirName, true);
		
	}
}
