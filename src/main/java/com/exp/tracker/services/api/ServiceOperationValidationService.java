package com.exp.tracker.services.api;

import org.springframework.webflow.execution.RequestContext;

import com.exp.tracker.data.model.GroupBean;

public interface ServiceOperationValidationService {

	void checkGroupCreationResult(GroupBean group, RequestContext ctx);
}
