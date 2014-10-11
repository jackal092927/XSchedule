package com.tongji.share.view;

import java.util.Date;

import org.primefaces.model.TreeNode;

public interface INavigationScheduleService {
	public TreeNode getTree(String account, Date startTime, Date endTime);
}
