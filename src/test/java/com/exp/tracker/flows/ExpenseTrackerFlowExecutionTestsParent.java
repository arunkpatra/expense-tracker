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

package com.exp.tracker.flows;

import org.springframework.webflow.config.FlowDefinitionResource;
import org.springframework.webflow.config.FlowDefinitionResourceFactory;
import org.springframework.webflow.test.execution.AbstractXmlFlowExecutionTests;

public abstract class ExpenseTrackerFlowExecutionTestsParent extends AbstractXmlFlowExecutionTests {

	protected FlowDefinitionResource[] getModelResources(
			FlowDefinitionResourceFactory resourceFactory) {
		FlowDefinitionResource[] flowDefinitionResources = new FlowDefinitionResource[1];
		flowDefinitionResources[0] = resourceFactory
				.createResource("flows/parentflow/parentflow-flow.xml", null, "parentflow");
		return flowDefinitionResources;
	}
}
