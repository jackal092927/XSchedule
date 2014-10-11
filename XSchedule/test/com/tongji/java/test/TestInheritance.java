package com.tongji.java.test;

import org.junit.Test;

public class TestInheritance {
	@Test
	public void Test(){
		GrandFather son = new Son();
		
		System.out.println(son);
		
	}
	
	interface GrandFather{	}
	
	class Father implements GrandFather{
		@Override
		public String toString(){
			return "Father";
		}
	}
	
	class Son extends Father{
		public Son(){
			super();
		}
		
		@Override
		public String toString(){
			return "Son1";
		}
	}
	
	
}
