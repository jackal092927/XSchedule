package com.tongji.persistence.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "user_event_classify", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"owner_account", "category" }) })
public class UserEventClassifyModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2228537704967670070L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "owner_account")
	private String ownerAccount;

	@Column(name = "category", length = 255)
	private String category;

	@Column(name = "category_setting_color")
	private String colorSetting;

	@Column(name = "event_type")
	private String eventType;

	@ManyToOne(fetch = FetchType.EAGER)
	@Cascade(CascadeType.REFRESH)
	@JoinColumn(name = "owner_account", insertable = false, updatable = false)
	private UserModel owner;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOwnerAccount() {
		return ownerAccount;
	}

	public void setOwnerAccount(String ownerAccount) {
		this.ownerAccount = ownerAccount;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public UserModel getOwner() {
		return owner;
	}

	public void setOwner(UserModel owner) {
		this.owner = owner;
	}

	@Override
	public String toString() {
		return ownerAccount + ": " + category;
	}

	public String getColor() {
		return getColorSetting();
	}

	public void setColor(String color) {
		this.setColorSetting(color);
	}

	public String getColorSetting() {
		return colorSetting;
	}

	public void setColorSetting(String colorSetting) {
		this.colorSetting = colorSetting;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

}
