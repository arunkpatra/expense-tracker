package com.exp.tracker.data.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.exp.tracker.data.entities.GroupEntity;
import com.exp.tracker.data.entities.UserEntity;

public class GroupBean implements Serializable {

	private static final long serialVersionUID = 3831397102812305481L;

	private Long id;
	private String groupName;
	private String groupDescription;
	private Boolean active;
	
	private List<UserBean> users;
	
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
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public List<UserBean> getUsers() {
		return users;
	}
	public void setUsers(List<UserBean> users) {
		this.users = users;
	}
	public GroupBean(GroupEntity ge) {
		this.id = ge.getId();
		this.groupName = ge.getGroupName();
		this.groupDescription = ge.getGroupDescription();
		this.active = ge.getActive() == 1 ? true : false;
		setUsers(new ArrayList<UserBean>());
		if (null != ge.getUsers()) {
			for (UserEntity ue : ge.getUsers()) {
				getUsers().add(new UserBean(ue));
			}
		}
	}
	
	public GroupBean() {
		// by default we make the group active
		setActive(true);
	}
	public GroupEntity getGroupEntity() {
		GroupEntity ge = new GroupEntity();
		ge.setActive(getActive() ? 1: 0);
		ge.setGroupName(getGroupName());
		ge.setGroupDescription(getGroupDescription());
		ge.setCreationDate(null);
		ge.setLastUpdatedDate(null);
		ge.setUsers(null);
		return ge;
	}
	
	public void clearUserData() {
		setGroupDescription(null);
		setGroupName(null);
	}
}
