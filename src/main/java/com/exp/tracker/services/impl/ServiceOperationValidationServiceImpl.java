package com.exp.tracker.services.impl;

import org.springframework.binding.message.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.webflow.execution.RequestContext;

import com.exp.tracker.data.model.GroupBean;
import com.exp.tracker.services.api.ServiceOperationValidationService;

@Service("validationService")
public class ServiceOperationValidationServiceImpl implements
		ServiceOperationValidationService {

	@Override
	public void checkGroupCreationResult(GroupBean group, RequestContext ctx) {
		if (null == group) {
			ctx.getMessageContext().addMessage(
					new MessageBuilder().error()
							.defaultText("Failed to create group.").build());
		} else {
			ctx.getMessageContext().addMessage(
					new MessageBuilder().info()
							.defaultText("Your group was created succesfuly.")
							.build());
		}

	}

}
