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

package com.exp.tracker.services.api;

import java.util.Collection;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.security.access.annotation.Secured;
import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.execution.RequestContext;

import com.exp.tracker.data.entities.RoleEntity;
import com.exp.tracker.data.entities.UserEntity;
import com.exp.tracker.data.model.PasswordChangeBean;
import com.exp.tracker.data.model.UserBean;

public interface UserService {
	
	public void storeUserInSession(ExternalContext ctx, UserBean ub);
	/**
	 * 
	 * @return a set of users
	 */
	public List<UserEntity> getUsers();
	
	public Collection<UserBean> getUserBeans();
	
	public UserBean getUser(String userName);
	
	public UserBean addUser(UserBean ub);
	
	public UserBean updateAutorization(UserBean ub);
	
	@Secured(RoleEntity.ROLE_SITE_ADMIN)
	public int deleteUser(Long id, String currentUserName);
	
	public List<String> getUserNames();
	
	public List<SelectItem> getUserNamesSelectItems();
	
	public void removeAuthById(Long id);
	
	public void updateUser(UserBean ub);
	
	public String changePassword(PasswordChangeBean pB, UserBean Ub);
	
	public boolean isPasswordChangeNeeded(String userName);
	
	public UserBean resetPassword(String userName, RequestContext ctx);

}
