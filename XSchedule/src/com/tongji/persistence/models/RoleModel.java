package com.tongji.persistence.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class RoleModel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5152605911879024713L;

	@Id
	@Column(name="role_id", nullable = false, unique = true)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer roleId;
	
	@Column(name="role_name", nullable = false, unique = true)
	private String roleName;
	
	//TODO: EAGER TO LAZY
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
	private Set<UserModel> userSet = new HashSet<UserModel>();

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Set<UserModel> getUserSet() {
		return userSet;
	}

	public void setUserSet(Set<UserModel> userSet) {
		this.userSet = userSet;
	}
	

}
