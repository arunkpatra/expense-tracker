package com.exp.tracker.services.api;

import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.security.access.annotation.Secured;
import org.springframework.webflow.context.ExternalContext;

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
	
	public List<UserBean> getUserBeans();
	
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
	
	public UserBean resetPassword(String userName);

}
