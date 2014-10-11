package com.tongji.share.view;

import java.util.Date;

import javax.faces.bean.ManagedProperty;

import org.primefaces.model.TreeNode;

import com.tongji.basicinfo.IUserService;

public class NavigationScheduleService implements INavigationScheduleService {

	@ManagedProperty(value = "#{UserService}")
	// meaningless currently
	private IUserService userService;

	public static final int PIN_INDEX = 0;
	public static final int JOIN_INDEX = 1;

	@Override
	public TreeNode getTree(String account, Date startTime, Date endTime) {
		// TODO Auto-generated method stub
		MyTreeNode root = new MyTreeNode(new MyTreeNodeData(), null);
		root.setDisplayString("Event");// not show?

		MyTreeNode pinNode = new MyTreeNode(new MyTreeNodeData(), root);
		pinNode.setDisplayString("Pin");
		pinNode.setCount(userService.getPinEventCount(account));

		MyTreeNode joinNode = new MyTreeNode(new MyTreeNodeData(), root);
		joinNode.setDisplayString("Join");
		joinNode.setCount(userService.getJoinEventCount(account));

		TreeNode publicEventsRoot = userService.getPublicEventsRoot(root,
				account, startTime, endTime);
		TreeNode privateEventsRoot = userService.getPrivateEventsRoot(root,
				account, startTime, endTime);
		TreeNode friendsEventsRoot = userService.getFriendsEventsRoot(root,
				account, startTime, endTime);

		// publicEventsRoot.setParent(root);
		// privateEventsRoot.setParent(root);
		// friendsEventsRoot.setParent(root);

		// root.getChildren().add(publicEventsRoot);
		// root.getChildren().add(privateEventsRoot);
		// root.getChildren().add(friendsEventsRoot);
		// has been included in the upper 3 actions.

		return root;
	}

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

}
