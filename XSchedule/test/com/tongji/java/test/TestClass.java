package com.tongji.java.test;

import org.junit.Test;

public class TestClass {
	@Test
	public void Test(){
		Object obj1 = new Integer(1);
		Integer obj2 = new Integer(2);
		System.out.println(obj1.getClass());
		System.out.println(obj2.getClass());
		System.out.println(obj2.getClass() == obj1.getClass());
		System.out.println(obj2.getClass().equals(obj1.getClass()));
		System.out.println(obj2.getClass().equals(Integer.class));
		
	}
}
