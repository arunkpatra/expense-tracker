package com.exp.tracker.data.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "et_groups")
@NamedQueries({
	@NamedQuery(name = "getAllGroups", query = "SELECT g FROM GroupEntity g")})
public class GroupEntity implements Serializable {

	private static final long serialVersionUID = 4431970009023604556L;

	/**
	 * A generated identifier that uniquely identifies this entity.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * The name of the group. It should be unique to avoid confusion in group
	 * listings.
	 */
	@Column(name = "groupname")
	private String groupName;

	/**
	 * The description of the group.
	 */
	@Column(name = "groupdescription")
	private String groupDescription;

	/**
	 * Indicates whether this group is active
	 */
	@Column(name = "active")
	private int active;

	/**
	 * The date on which this group was created
	 */
	@Column(name = "creationdate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	/**
	 * The date on which this entity was last updated
	 */
	@Column(name = "lastupdateddate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdatedDate;

	/**
	 * A set of users who belong to this group. Multiple users can belong to a
	 * group, and a user may belong to multiple users. We will consider the
	 * GroupEntity to be the owning side from a JPA standpoint.
	 */
	@ManyToMany(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
	@JoinTable(name = "et_group_user", joinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
	private Set<UserEntity> users;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupDescription() {
		return groupDescription;
	}

	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public Set<UserEntity> getUsers() {
		return users;
	}

	public void setUsers(Set<UserEntity> users) {
		this.users = users;
	}
}
