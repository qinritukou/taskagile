package com.taskagile.domain.application.impl;

import com.taskagile.doman.application.commands.RegistrationCommand;
import com.taskagile.doman.application.impl.UserServiceImpl;
import com.taskagile.doman.common.event.DomainEventPublisher;
import com.taskagile.doman.common.mail.MailManager;
import com.taskagile.doman.common.mail.MessageVariable;
import com.taskagile.doman.model.user.*;
import com.taskagile.doman.model.user.events.UserRegisteredEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTests {

    @MockBean
    private RegistrationManagement registrationManagement;

    @MockBean
    private DomainEventPublisher domainEventPublisher;

    @MockBean
    private MailManager mailManager;

    @Autowired
    private UserServiceImpl userService;

    @Test(expected = IllegalArgumentException.class)
    public void register_nullCommand_shouldFail() throws RegistrationException {
        userService.register(null);
    }

    @Test(expected = RegistrationException.class)
    public void register_existingUsername_shouldFail() throws RegistrationException {
        String username = "existing";
        String emailAddress = "sunny@taskagile.com";
        String password = "MyPassword!";
        doThrow(UsernameExistsException.class).when(registrationManagement)
            .register(username, emailAddress, password);

        RegistrationCommand command = new RegistrationCommand(username, emailAddress, password);
        userService.register(command);
    }

    @Test(expected = RegistrationException.class)
    public void register_existingEmailAddress_shouldFail() throws RegistrationException {
        String username = "sunny";
        String emailAddress = "existing@taskagile.com";
        String password = "MyPassword!";
        doThrow(EmailAddressExistsException.class).when(registrationManagement)
            .register(username, emailAddress, password);

        RegistrationCommand command = new RegistrationCommand(username, emailAddress, password);
        userService.register(command);
    }

    @Test
    public void register_validCommand_shoudSuccessed() throws RegistrationException {
        String username = "sunny";
        String emailAddress = "sunny@taskagile.com";
        String password = "MyPassword!";

        User newUser = User.create(username, emailAddress, password);
        when(registrationManagement.register(username, emailAddress, password))
            .thenReturn(newUser);
        RegistrationCommand command = new RegistrationCommand(username, emailAddress, password);

        userService.register(command);

        verify(mailManager).send(
            emailAddress,
            "Welcome to TaskAgile",
            "welcome.ftl",
            MessageVariable.from("user", newUser)
        );
        verify(domainEventPublisher).publish(new UserRegisteredEvent(newUser));
    }

}
