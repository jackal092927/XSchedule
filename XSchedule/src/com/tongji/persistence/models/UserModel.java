package com.tongji.persistence.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "user")
public class UserModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6115952154708658761L;

	@Id
	@Column(unique = true, nullable = false)
	private String account;

	private String username;

	@Column(nullable = false, updatable = false)
	private String password;

	private String email;

	private String qq_no;

	private String phone_no;

	private int role;

	@Column(name = "major")
	private String major;

	@Column(name = "grade")
	private String grade;

	@Column(name = "degree")
	private String degree = "BACHELOR";

	@Column(name = "gender")
	// 0:F; 1:M; 2:NA
	private Integer gender;
	
	@Column(name = "self_introduction")
	private String selfIntro;

	// TODO: EARGER TO LAZY
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
	@Cascade(CascadeType.ALL)
	private Set<ActivityModel> activities = new HashSet<ActivityModel>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
	@Cascade(CascadeType.ALL)
	private Set<CourseModel> courses = new HashSet<CourseModel>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
	@Cascade(CascadeType.ALL)
	private Set<CommentModel> comments = new HashSet<CommentModel>();

	@ManyToMany(fetch = FetchType.LAZY)
	@Cascade(CascadeType.ALL)
	@JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "account", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "role_id", nullable = false, updatable = false) })
	private Set<RoleModel> roles = new HashSet<RoleModel>();

	@ManyToMany(fetch = FetchType.LAZY)
	@Cascade(CascadeType.ALL)
	@JoinTable(name = "user_follower", joinColumns = { @JoinColumn(name = "user_account") }, inverseJoinColumns = { @JoinColumn(name = "follower_account") })
	private Set<UserModel> followers = new HashSet<UserModel>();

	@ManyToMany(mappedBy = "followers")
	@Cascade(CascadeType.REFRESH)
	private Set<UserModel> followTargets = new HashSet<UserModel>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "relatedUser")
	@Cascade(CascadeType.ALL)
	private Set<UserEventRelation> relatedActivities = new HashSet<UserEventRelation>();

	// TODO: EAGER TO LAZY
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
	@Cascade(CascadeType.ALL)
	private List<UserEventClassifyModel> classifications = new ArrayList<UserEventClassifyModel>();

	public UserModel() {
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQq_no() {
		return qq_no;
	}

	public void setQq_no(String qq_no) {
		this.qq_no = qq_no;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public String getPhone_no() {
		return phone_no;
	}

	public void setPhone_no(String phone_no) {
		this.phone_no = phone_no;
	}

	@Override
	public String toString() {
		StringBuffer strBuff = new StringBuffer();
		strBuff.append("Account: ").append(account);
		strBuff.append("Username: ").append(username);

		return strBuff.toString();
	}

	//

	public Set<ActivityModel> getActivities() {
		return activities;
	}

	public void setActivities(Set<ActivityModel> activities) {
		this.activities = activities;
	}

	public Set<CourseModel> getCourses() {
		return courses;
	}

	public void setCourses(Set<CourseModel> courses) {
		this.courses = courses;
	}

	public Set<CommentModel> getComments() {
		return comments;
	}

	public void setComments(Set<CommentModel> comments) {
		this.comments = comments;
	}

	public Set<RoleModel> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleModel> roles) {
		this.roles = roles;
	}

	public Set<UserEventRelation> getRelatedActivities() {
		return relatedActivities;
	}

	public void setRelatedActivities(Set<UserEventRelation> relatedActivities) {
		this.relatedActivities = relatedActivities;
	}

	public Set<UserModel> getFollowers() {
		return followers;
	}

	public void setFollowers(Set<UserModel> followers) {
		this.followers = followers;
	}

	public Set<UserModel> getFollowTargets() {
		return followTargets;
	}

	public void setFollowTargets(Set<UserModel> followTargets) {
		this.followTargets = followTargets;
	}

	public List<UserEventClassifyModel> getClassifications() {
		return classifications;
	}

	public void setClassifications(List<UserEventClassifyModel> classifications) {
		this.classifications = classifications;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {

			return false;

		}
		return this.account.equals(((UserModel) obj).getAccount());
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getSelfIntro() {
		return selfIntro;
	}

	public void setSelfIntro(String selfIntro) {
		this.selfIntro = selfIntro;
	}

}
