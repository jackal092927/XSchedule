package com.tongji.share.view;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.primefaces.model.TreeNode;
import org.primefaces.model.TreeNodeEvent;

import com.tongji.basicinfo.MyScheduleEvent;
import com.tongji.basicinfo.MyScheduleModel;
import com.tongji.basicinfo.ScheduleBean;
import com.tongji.basicinfo.UserBean;

@ManagedBean
@SessionScoped
public class NavigationScheduleBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3725467107196366017L;

	private TreeNode root;

	private TreeNode[] selectedNodes;

	@ManagedProperty(value = "#{scheduleBean}")
	private ScheduleBean scheduleBean;

	@ManagedProperty(value = "#{userBean}")
	private UserBean currentUser;

	@ManagedProperty(value = "#{navigationScheduleService}")
	private INavigationScheduleService navigationScheduleService;

	@PostConstruct
	public void initialize() {
		if (currentUser != null) {
			try {
				Date start = null;
				Date end = null;
				updateTree(start, end);
				updateSchedule();
			} catch (Exception e) {
				// TODO: what exception?
				e.printStackTrace();
			}
		}
	}

	public void updateTree(Date start, Date end) {
		root = navigationScheduleService.getTree(currentUser.getAccount(),
				null, null);

	}

	private void traverseTree(TreeNode node, Object[] vars,
			TreeNodeHandler nodeHandler) {
		if (node != null) {
			nodeHandler.handle(node, vars);
			List<TreeNode> nodeList = node.getChildren();
			for (TreeNode treeNode : nodeList) {
				traverseTree(treeNode, vars, nodeHandler);
			}
		}
	}

	public void updateSchedule() {
		Object[] vars = new Object[1];
		ScheduleModel eventModel = scheduleBean.getEventModel();
		if (eventModel != null) {
			eventModel.clear();
		}
		vars[0] = eventModel;
		traverseTree(root, vars, new TreeNodeHandler() {

			@Override
			public void handle(Object node, Object[] vars) {
				// TODO Auto-generated method stub
				ScheduleModel eventModel = (ScheduleModel) vars[0];
				TreeNode treeNode = (TreeNode) node;
				if (treeNode.getData() != null) {
					MyTreeNodeData treeNodeData = (MyTreeNodeData) treeNode
							.getData();
					List eventList = treeNodeData.getEventData();
					if (eventList!=null) {
						for (Object event : eventList) {
							eventModel.addEvent((MyScheduleEvent) event);
						}
					}
				}
			}
		});
	}

	public NavigationScheduleBean() {
	}

	public ScheduleBean getScheduleBean() {
		return scheduleBean;
	}

	public void setScheduleBean(ScheduleBean scheduleBean) {
		this.scheduleBean = scheduleBean;
	}

	public TreeNode getRoot() {
		return root;
	}

	private void setRoot(TreeNode root) {
		this.root = root;
	}

	public TreeNode[] getSelectedNodes() {
		return selectedNodes;
	}

	public void setSelectedNodes(TreeNode[] selectedNodes) {
		this.selectedNodes = selectedNodes;
	}

	public UserBean getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(UserBean currentUser) {
		this.currentUser = currentUser;
	}

	public INavigationScheduleService getNavigationScheduleService() {
		return navigationScheduleService;
	}

	public void setNavigationScheduleService(
			INavigationScheduleService navigationScheduleService) {
		this.navigationScheduleService = navigationScheduleService;
	}

	public void selectNodes() {
		if (selectedNodes != null && selectedNodes.length > 0) {
			ScheduleModel eventModel = scheduleBean.getEventModel();
			eventModel.clear();
			for (TreeNode node : selectedNodes) {
				List<ScheduleEvent> eventList = ((MyTreeNodeData) node
						.getData()).getEventData();
				for (ScheduleEvent scheduleEvent : eventList) {
					if (!eventModel.getEvents().contains(scheduleEvent)) {
						//TODO: can be optimized;
						eventModel.addEvent(scheduleEvent);
					}
				}
			}
		}
	}

}
