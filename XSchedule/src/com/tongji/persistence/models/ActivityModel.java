package com.tongji.persistence.models;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "activity")
public class ActivityModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5883006814942096022L;

	@Id
	@Column(name = "id_activity")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String title;//not null; default "NO TITLE"

	private String location;

	@Column(name = "begin_time")
	private Date beginTime;

	@Column(name = "end_Time")
	private Date endTime;

	@Column(name = "publish_time")
	private Date publishTime; //not null

	private Integer publicity;

	private String uuid;
	
	@Column(name = "img_default")
	private String imageDefault;
	
	@Column(name="img_count")
	private int imageCount;

	@Column(name = "owner")
	private String ownerAccount;
	
	@Column(name="owner_category", length=255)
	private String ownerCategory;
	
	@Column(name="owner_setting_color", columnDefinition="default 'DEFAULT'")
	private String ownerColorSetting;
	
	@Column(name="last_update_time", insertable = false)
	private Timestamp lastUpdateTime;
	
	@Column(name="valid")
	private boolean valid;
	
	@Column(name = "all_day")
	private boolean allDay; //default = false
	
	//--- relations ---//
	@ManyToOne(fetch = FetchType.EAGER)
	@Cascade(CascadeType.REFRESH)
	@JoinColumn(name = "owner", insertable = false, updatable = false)
	private UserModel owner;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "activity")
	@Cascade(CascadeType.ALL)
	private Set<CommentModel> comments = new HashSet<CommentModel>();

	@OneToMany(fetch = FetchType.LAZY, 
			mappedBy = "relatedActivity")
	@Cascade(CascadeType.ALL)
	private Set<UserEventRelation> relatedUsers = new HashSet<UserEventRelation>();
	
	@ManyToOne(fetch=FetchType.EAGER)
	@Cascade(CascadeType.REFRESH)
	@JoinColumns({
		@JoinColumn(name="owner",referencedColumnName="owner_account", insertable = false, updatable = false),
		@JoinColumn(name="owner_category",referencedColumnName="category", insertable = false, updatable = false),
	})
	private UserEventClassifyModel ownerCategoryWithSetting;
	
	public ActivityModel() {
	}// without it has no problem?

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	public Integer getPublicity() {
		return publicity;
	}

	public void setPublicity(Integer publicity) {
		this.publicity = publicity;
	}

	@Override
	public String toString() {
		return "ActivityModel [id_activity=" + id + ", title=" + title
				+ ", publish_time=" + publishTime + "]";
	}

	public UserModel getOwner() {
		return owner;
	}

	public void setOwner(UserModel owner) {
		this.owner = owner;
	}

	public Set<CommentModel> getComments() {
		return comments;
	}

	public void setComments(Set<CommentModel> comments) {
		this.comments = comments;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getOwnerAccount() {
		return ownerAccount;
	}

	public void setOwnerAccount(String ownerAccount) {
		this.ownerAccount = ownerAccount;
	}

	public String getOwnerCategory() {
		return ownerCategory;
	}

	public void setOwnerCategory(String ownerCategory) {
		this.ownerCategory = ownerCategory;
	}

	public Set<UserEventRelation> getRelatedUsers() {
		return relatedUsers;
	}

	public void setRelatedUsers(Set<UserEventRelation> relatedUsers) {
		this.relatedUsers = relatedUsers;
	}

	public UserEventClassifyModel getOwnerCategoryWithSetting() {
		return ownerCategoryWithSetting;
	}

	public void setOwnerCategoryWithSetting(UserEventClassifyModel ownerCategoryWithSetting) {
		this.ownerCategoryWithSetting = ownerCategoryWithSetting;
	}

	public String getOwnerColorSetting() {
		return ownerColorSetting;
	}

	public void setOwnerColorSetting(String ownerColorSetting) {
		this.ownerColorSetting = ownerColorSetting;
	}

	public String getImageDefault() {
		return imageDefault;
	}

	public void setImageDefault(String imageDefault) {
		this.imageDefault = imageDefault;
	}

	public int getImageCount() {
		return imageCount;
	}

	public void setImageCount(int imageCount) {
		this.imageCount = imageCount;
	}
	
	@Transient
	public String getImageDefaultUri(){
		String imageUri;
		if(getImageCount() == 0 ){
			if(getImageDefault() == null){
				imageUri = "share/imgs/noImg.jpg";
				return imageUri;
			}else {
				return null;
			}
		}else {
			if(getImageDefault() != null){
				//TODO: if in temp dir?
				imageUri = getUuid() + "/imgs/" + getImageDefault();
				return imageUri;
			}else {
				return null;
			}
		}
	}

	public Timestamp getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Timestamp lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public boolean isAllDay() {
		return allDay;
	}

	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
	}

}
