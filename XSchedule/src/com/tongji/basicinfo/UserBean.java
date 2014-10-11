package com.tongji.basicinfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.RememberMeServices;

import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;
import com.tongji.persistence.models.UserModel;

@ManagedBean
@SessionScoped
public class UserBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1023096391053687929L;
	private static final String SUCCESS = "success";
	private static final String ERROR = "error";
	private static final String FAIL = "fail";
	private static final String LOGOUT = "logout";

	// Spring User Service is injected...
	@ManagedProperty(value = "#{userService}")
	IUserService userService;

	@ManagedProperty(value = "#{authenticationManager}")
	private AuthenticationManager authenticationManager;

	@ManagedProperty(value = "#{userDetailsService}")
	private UserDetailsService userDetailsService;

	@ManagedProperty(value = "#{rememberMeServices}")
	private RememberMeServices rememberMeServices;

	List<UserModel> userList;

	private String account;
	private String username;
	private String password;
	
	private String theme="aristo";
	
	
	private String errorMsg;
	private Boolean rememberMe;

	public UserBean() {
	}

	public String login() {
		Authentication result = null;
		try {

			if (rememberMe) {
				UserDetails userDetails = getUserDetailsService()
						.loadUserByUsername(account);// get a userDetailService
														// by userId;
				RememberMeAuthenticationToken rememberMeAuthenticationToken = new RememberMeAuthenticationToken(
						"springSecKey", userDetails,
						userDetails.getAuthorities());// use the userService to creat a rememberme...Token
				HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext
						.getCurrentInstance().getExternalContext().getRequest();
				HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext
						.getCurrentInstance().getExternalContext()
						.getResponse();
				rememberMeServices.loginSuccess(httpServletRequest,
						httpServletResponse, rememberMeAuthenticationToken);// save
																			// user
																			// into
																			// cookie
				result = rememberMeAuthenticationToken;
				userDetails.getAuthorities();
			} else {
				Authentication request = new UsernamePasswordAuthenticationToken(
						this.account, this.password);
				result = authenticationManager.authenticate(request);

			}
			SecurityContextHolder.getContext().setAuthentication(result);
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}

		// try {
		// UserModel user = new UserModel();
		// user.setAccount(account);
		// user.setPassword(password);
		// if (getUserService().validateUser(user)==true) {
		// return SUCCESS;
		// }else {
		// return FAIL;
		// }
		//
		// } catch (DataAccessException e) {
		// e.printStackTrace();
		// }
		if (result.isAuthenticated()) {
			return SUCCESS;

		} else {
			return FAIL;
		}
		// return SUCCESS;
	}

	public String logout() {
		SecurityContextHolder.clearContext();
		/**
		 * Delete Cookies
		 */
		HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext
				.getCurrentInstance().getExternalContext().getResponse();
		Cookie cookie = new Cookie("SPRING_SECURITY_REMEMBER_ME_COOKIE", null);
		cookie.setMaxAge(0);
		cookie.setPath(httpServletRequest.getContextPath().length() > 0 ? httpServletRequest
				.getContextPath() : "/");
		httpServletResponse.addCookie(cookie);

		return LOGOUT;

	}

	// //check the user
	// public String check(){
	// if(validate()==true){
	// errorMsg = "";
	// return "welcome";
	// }
	// errorMsg = "Login fail!";
	// return "login";
	//
	// }
	//
	// //validate the username and password
	// private boolean validate(){
	// if("abc".equals(account) && "abc".equals(password)){
	// return true;
	// }
	// return false;
	// }

	// no need now.
	public String addUser() {
		try {
			// UserModel user = new UserModel(this);
			UserModel user = new UserModel();
			user.setAccount(account);
			user.setUsername(username);
			user.setPassword(password);
			getUserService().addUser(user);
			return SUCCESS;

		} catch (DataAccessException e) {
			e.printStackTrace();
		}

		return ERROR;
	}

	/**
	 * Reset Fields
	 * 
	 */
	public void reset() {
		this.account = "";
		this.username = "";
		this.password = "";
	}

	/**
	 * Get User List
	 * 
	 * @return List - User List
	 */
	public List<UserModel> getUserList() {
		userList = new ArrayList<UserModel>();
		userList.addAll(getUserService().getUsers());
		return userList;
	}

	/**
	 * Get User Service
	 * 
	 * @return IUserService - User Service
	 */
	public IUserService getUserService() {
		return userService;
	}

	/**
	 * Set User Service
	 * 
	 * @param IUserService
	 *            - User Service
	 */
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	public void setAuthenticationManager(
			AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	public Boolean getRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(Boolean rememberMe) {
		this.rememberMe = rememberMe;
	}

	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}

	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	public RememberMeServices getRememberMeServices() {
		return rememberMeServices;
	}

	public void setRememberMeServices(RememberMeServices rememberMeServices) {
		this.rememberMeServices = rememberMeServices;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

}
