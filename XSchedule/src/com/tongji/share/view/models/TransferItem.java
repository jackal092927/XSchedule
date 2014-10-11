package com.tongji.share.view.models;

public class TransferItem {
	private Object item;
	private boolean add;
	
	public TransferItem(){
		
	}
	
	public TransferItem(Object item, boolean add){
		this.item = item;
		this.add = add;
	}
	 
	@Override
	public int hashCode() {
		if (item != null) {
			return item.hashCode();
		}
		return super.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if(obj==null){
			return false;
		}
		if (!(obj instanceof TransferItem)) {
			return false;
		}
		return item.equals(((TransferItem)obj).getItem());
	}
	
	
	public Object getItem() {
		return item;
	}
	public void setItem(Object item) {
		this.item = item;
	}
	public boolean isAdd() {
		return add;
	}
	public void setAdd(boolean add) {
		this.add = add;
	}
	
	
}
