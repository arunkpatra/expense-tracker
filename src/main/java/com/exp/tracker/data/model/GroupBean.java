/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
