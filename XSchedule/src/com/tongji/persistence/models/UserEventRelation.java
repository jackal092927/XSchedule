package com.tongji.persistence.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "user_event", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"related_user", "related_activity" }) })
public class UserEventRelation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 235973356507562580L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "related_category")
	private String relatedCategory;

	@Column(name = "related_user")
	private String relatedUserAccount;

	@Column(name = "related_activity")
	private Integer relatedActivityId;

	@Column(name = "authority")
	private String authority;

	@Column(name = "related_user_setting_color")
	private String relatedColorSetting = "DEFAULT";
	
	@Column(name="event_related_type", columnDefinition="default 'FRIEND'")
	private String eventRelatedType = "FRIEND";
	
	@Column(name="pin")
	private boolean pin = false;
	
	@Column(name="joinin")
	private boolean joinin = false;

//	@Transient
//	private List authorityList;
	
	// EARGER IS OK
	@ManyToOne(fetch = FetchType.EAGER)
	@Cascade(CascadeType.REFRESH)
	@JoinColumn(name = "related_user", nullable = true, insertable = false, updatable = false)
	private UserModel relatedUser;

	@ManyToOne(fetch = FetchType.EAGER)
	@Cascade(CascadeType.REFRESH)
	@JoinColumn(name = "related_activity", nullable = true, insertable = false, updatable = false)
	private ActivityModel relatedActivity;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRelatedCategory() {
		return relatedCategory;
	}

	public void setRelatedCategory(String relatedCategory) {
		this.relatedCategory = relatedCategory;
	}

	public UserModel getRelatedUser() {
		return relatedUser;
	}

	public void setRelatedUser(UserModel relatedUser) {
		this.relatedUser = relatedUser;
	}

	public ActivityModel getRelatedActivity() {
		return relatedActivity;
	}

	public void setRelatedActivity(ActivityModel relatedActivity) {
		this.relatedActivity = relatedActivity;
	}

	public String getRelatedUserAccount() {
		return relatedUserAccount;
	}

	public void setRelatedUserAccount(String relatedUserAccount) {
		this.relatedUserAccount = relatedUserAccount;
	}

	public Integer getRelatedActivityId() {
		return relatedActivityId;
	}

	public void setRelatedActivityId(Integer relatedActivityId) {
		this.relatedActivityId = relatedActivityId;
	}

	public String getAuthority() {
		return authority;
	}

	public String getRelatedColorSetting() {
		return relatedColorSetting;
	}

	public void setRelatedColorSetting(String relatedColorSetting) {
		this.relatedColorSetting = relatedColorSetting;
	}

	public List<String> getAuthorityList() {
		List<String> authorityList = new ArrayList<String>();
		if (getAuthority() == null) {
			setAuthority(new String("1"));
		}
		String[] strArray = getAuthority().split(",");
		for (int i = 0; i < strArray.length; i++) {
			authorityList.add(strArray[i]);
		}
		return authorityList;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getEventRelatedType() {
		return eventRelatedType;
	}

	public void setEventRelatedType(String eventRelatedType) {
		this.eventRelatedType = eventRelatedType;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return this == null;
		}
		if (((UserEventRelation) obj).getRelatedUserAccount().equals(
				this.getRelatedUserAccount())) {
			if (((UserEventRelation) obj).getRelatedActivityId() == null
					|| this.getRelatedActivityId() == null) {
				return true;
			}
			return ((UserEventRelation) obj).getRelatedActivityId().equals(
					this.relatedActivityId);
		}
		return false;

	}

	@Override
	public int hashCode() {
		//TODO:
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + (this.relatedUserAccount != null ? this.relatedUserAccount.hashCode() : 0 );
		result = PRIME * result + (this.relatedActivityId != null ? this.relatedActivityId.hashCode() : 0 );
		return result;
	}

	public boolean isPin() {
		return pin;
	}

	public void setPin(boolean pin) {
		this.pin = pin;
	}

	public boolean isJoinin() {
		return joinin;
	}

	public void setJoinin(boolean joinin) {
		this.joinin = joinin;
	}


}
