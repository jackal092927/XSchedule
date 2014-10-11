package com.tongji.java.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.tongji.share.tools.PropertiesEditor;

public class TestPropertiesEditor {

	@Test
	public void test() {
		String str = PropertiesEditor.getProperty("activityAccessAuthority","hello");
		System.out.println(str);
		fail("Not yet implemented");
	}

}
