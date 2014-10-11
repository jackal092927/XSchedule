package com.tongji.basicinfo;

import java.io.Serializable;

import javax.faces.bean.ManagedProperty;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.tongji.persistence.IUserDAO;
import com.tongji.persistence.models.UserModel;
import com.tongji.share.tools.ModelsUtil;

public class MyUserDetailsService implements UserDetailsService, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6178006208166296388L;
	@ManagedProperty(value = "#{userDao}")
	private IUserDAO userDAO;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String account)
			throws UsernameNotFoundException {
		UserModel userModel = userDAO.getById(account);
		if (userModel == null) {
			throw new UsernameNotFoundException("user not found");
		}
		// userModel.getRoles();

		return ModelsUtil.usermodelToSpringUserdetails(userModel);
	}

	public IUserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(IUserDAO userDAO) {
		this.userDAO = userDAO;
	}

}
