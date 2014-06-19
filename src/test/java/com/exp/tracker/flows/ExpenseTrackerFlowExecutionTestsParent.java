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
