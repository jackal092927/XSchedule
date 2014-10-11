package com.tongji.share.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.model.TreeNode;

public class MyTreeNode implements TreeNode, Serializable {

	private int count = 0;

	private String displayString;

	public static final String DEFAULT_TYPE = "default";

	private String type;

	private Object data;

	private List<TreeNode> children;

	private TreeNode parent;

	private boolean expanded;

	private boolean selected;

	private boolean selectable = true;

	public MyTreeNode() {
		this.type = DEFAULT_TYPE;
		children = new ArrayList<TreeNode>();
	}

	public MyTreeNode(Object data, TreeNode parent) {
		this.type = DEFAULT_TYPE;
		this.data = data;
		children = new ArrayList<TreeNode>();
		this.parent = parent;
		if (this.parent != null)
			this.parent.getChildren().add(this);
	}

	public MyTreeNode(String type, Object data, TreeNode parent) {
		this.type = type;
		this.data = data;
		children = new ArrayList<TreeNode>();
		this.parent = parent;
		if (this.parent != null)
			this.parent.getChildren().add(this);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public TreeNode getParent() {
		return parent;
	}

	public void setParent(TreeNode parent) {
		if (this.parent != null) {
			this.parent.getChildren().remove(this);
		}

		this.parent = parent;

		if (this.parent != null) {
			this.parent.getChildren().add(this);
		}
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public boolean isSelected() {
		return this.selected;
	}

	public void setSelected(boolean value) {
		this.selected = value;
	}

	public boolean isSelectable() {
		return selectable;
	}

	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}

	public int getChildCount() {
		return children.size();
	}

	public boolean isLeaf() {
		if (children == null)
			return true;

		return children.isEmpty();
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
		((MyTreeNodeData)this.data).setCount(count);
	}

	public String getDisplayString() {
		return displayString;
	}

	public void setDisplayString(String displayString) {
		this.displayString = displayString;
		((MyTreeNodeData)this.data).setDisplayString(displayString);
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((data == null) ? 0 : data.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		MyTreeNode other = (MyTreeNode) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))// TODO: data.equals?
			return false;

		return true;
	}

	@Override
	public String toString() {
		return this.displayString + "[" + count + "]";
	}

	
	

	
}
