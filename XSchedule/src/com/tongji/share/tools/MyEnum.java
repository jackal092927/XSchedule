package com.tongji.share.tools;

public class MyEnum {
	public enum EventRelatedType {
		PUBLIC, PRIVATE, FRIEND
	}

	public enum FileType {
		IMG("imgs"), MATERIAL("materials"), OTHERS("others");
		
		private String typeString;
		private FileType(String typeString){
			this.typeString = typeString;
		}
		
		@Override
		public String toString(){
			return this.typeString;
		}
		
	}

}
