package com.exp.tracker.flows.welcome;

import org.easymock.EasyMock;
import org.springframework.faces.model.converter.FacesConversionService;
import org.springframework.webflow.config.FlowDefinitionResource;
import org.springframework.webflow.config.FlowDefinitionResourceFactory;
import org.springframework.webflow.core.collection.LocalAttributeMap;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.test.MockExternalContext;
import org.springframework.webflow.test.MockFlowBuilderContext;
import org.springframework.webflow.test.execution.AbstractXmlFlowExecutionTests;

import com.exp.tracker.data.model.UserBean;
import com.exp.tracker.services.api.UserService;

public class WelcomeFlowExecutionTests extends AbstractXmlFlowExecutionTests
{

    private UserService userService;

    protected void setUp()
    {
        userService = EasyMock.createMock(UserService.class);
    }

    @Override
    protected FlowDefinitionResource getResource(
            FlowDefinitionResourceFactory resourceFactory)
    {
        return resourceFactory
                .createFileResource("src/main/webapp/WEB-INF/flows/welcome/welcome-flow.xml");
    }

    protected void configureFlowBuilderContext(
            MockFlowBuilderContext builderContext)
    {
        builderContext.registerBean("userService", userService);
        builderContext.getFlowBuilderServices().setConversionService(
                new FacesConversionService());
    }
    
    public void testStartWelcomeFlow() {
        UserBean etUser = createTestUserBean();
        EasyMock.expect(userService.getUser("System")).andReturn(etUser);
        EasyMock.expect(userService.isPasswordChangeNeeded("System")).andReturn(false);
        
        EasyMock.replay(userService);
        
        MockExternalContext context = new MockExternalContext();
        context.setCurrentUser("System");

        startFlow(context);
        
        assertCurrentStateEquals("welcome");
        assertResponseWrittenEquals("welcome", context);
        assertTrue(getRequiredFlowAttribute("etUser") instanceof UserBean);

        EasyMock.verify(userService);
    }
    
    private UserBean createTestUserBean() {
        UserBean u = new UserBean();
        u.setId(1L);
        u.setEnabled(true);
        u.setUsername("System");
        u.setPassword("admin");
        u.setPasswordChangeNeeded(true);
        u.setFirstName("System");
        u.setLastName("Administrator");
        u.setId(1L);
        
        return u;
    }
}
