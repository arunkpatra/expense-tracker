package com.exp.tracker.flows.changepassword;

import org.easymock.EasyMock;
import org.springframework.faces.model.converter.FacesConversionService;
import org.springframework.webflow.config.FlowDefinitionResource;
import org.springframework.webflow.config.FlowDefinitionResourceFactory;
import org.springframework.webflow.test.MockExternalContext;
import org.springframework.webflow.test.MockFlowBuilderContext;
import org.springframework.webflow.test.execution.AbstractXmlFlowExecutionTests;

import com.exp.tracker.data.model.PasswordChangeBean;
import com.exp.tracker.data.model.UserBean;
import com.exp.tracker.services.api.UserService;

public class ChangePasswordFlowExecutionTests extends
        AbstractXmlFlowExecutionTests
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
                .createFileResource("src/main/webapp/WEB-INF/flows/changepassword/changepassword-flow.xml");
    
    }
    protected void configureFlowBuilderContext(
            MockFlowBuilderContext builderContext)
    {
        builderContext.registerBean("userService", userService);
        builderContext.getFlowBuilderServices().setConversionService(
                new FacesConversionService());
    }
    
    public void testStartChangePasswordFlow() {
        UserBean etUser = createTestUserBean();
        UserBean currentUserBean = createTestUserBean();
        
        EasyMock.expect(userService.getUser("System")).andReturn(etUser);
        EasyMock.expect(userService.getUser("System")).andReturn(currentUserBean);
        
        EasyMock.replay(userService);
        
        MockExternalContext context = new MockExternalContext();
        context.setCurrentUser("System");
        
        startFlow(context);

        assertCurrentStateEquals("changePasswordEntry");
        assertResponseWrittenEquals("changePasswordEntry", context);
        assertTrue(getRequiredFlowAttribute("etUser") instanceof UserBean);
        assertTrue(getRequiredFlowAttribute("currentUserBean") instanceof UserBean);

        EasyMock.verify(userService);
    }
    
    public void testChangePasswordEntry_Save() {
        setCurrentState("changePasswordEntry");
        PasswordChangeBean passwordChangeBean = new PasswordChangeBean();
        UserBean currentUserBean = createTestUserBean();
        getFlowScope().put("passwdChangeBean", passwordChangeBean);
        getFlowScope().put("currentUserBean", currentUserBean);
        
        MockExternalContext context = new MockExternalContext();
        context.setEventId("save");
        resumeFlow(context);

        assertCurrentStateEquals("passwordChangeResults");
        assertResponseWrittenEquals("passwordChangeResults", context);
        
        context.setEventId("ok");
        resumeFlow(context);
        assertFlowExecutionEnded();
    }
    
    public void testChangePasswordEntry_Cancel() {
        setCurrentState("changePasswordEntry");
        PasswordChangeBean passwordChangeBean = new PasswordChangeBean();
        getFlowScope().put("passwdChangeBean", passwordChangeBean);
        
        MockExternalContext context = new MockExternalContext();
        context.setEventId("cancel");
        resumeFlow(context);

        assertFlowExecutionEnded();
    }
    private UserBean createTestUserBean()
    {
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
