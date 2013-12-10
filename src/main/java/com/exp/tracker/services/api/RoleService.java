package com.exp.tracker.services.api;

import java.util.List;

import javax.faces.model.SelectItem;

import com.exp.tracker.data.entities.RoleEntity;

public interface RoleService {
	
	public List<RoleEntity> getRoles();
	
	public List<SelectItem> getRolesSelectItems();

}
