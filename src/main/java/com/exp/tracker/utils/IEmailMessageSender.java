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

package com.exp.tracker.utils;

import java.util.List;

import com.exp.tracker.data.model.SettlementBean;
import com.exp.tracker.data.model.UserBean;

public interface IEmailMessageSender {
	public void sendPasswordResetEmail(UserBean ub);	
	public void sendSettlementNotice(SettlementBean sb, List<UserBean> ul, byte[] settlementReport, byte[] expenseReport);
	public void sendWelcomeEmail(UserBean ub);
}
