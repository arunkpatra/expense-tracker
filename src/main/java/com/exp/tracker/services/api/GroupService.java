package com.exp.tracker.services.api;

import java.util.Collection;

import com.exp.tracker.data.model.GroupBean;

public interface GroupService {

	/**
	 * Get a list of groups that currently exist in the datastore.
	 * 
	 * @return The list of groups
	 */
	Collection<GroupBean> getGroups();

	/**
	 * Add a new group.
	 * 
	 * @param group
	 *            The group to be added
	 * @return The group just added. The id is populated in it. Returns null if
	 *         the group was not added.
	 */
	GroupBean addGroup(GroupBean group);
}
