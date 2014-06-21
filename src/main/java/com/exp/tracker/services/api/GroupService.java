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
