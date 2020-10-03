package com.taskagile.domain.application.impl;

import com.taskagile.doman.application.commands.RegistrationCommand;
import com.taskagile.doman.application.impl.UserServiceImpl;
import com.taskagile.doman.common.event.DomainEventPublisher;
import com.taskagile.doman.common.mail.MailManager;
import com.taskagile.doman.model.user.RegistrationException;
import com.taskagile.doman.model.user.RegistrationManagement;
import com.taskagile.doman.model.user.UsernameExistsException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.doThrow;

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





}
