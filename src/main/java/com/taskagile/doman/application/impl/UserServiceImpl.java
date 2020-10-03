package com.taskagile.doman.application.impl;

import com.taskagile.doman.application.UserService;
import com.taskagile.doman.application.commands.RegistrationCommand;
import com.taskagile.doman.common.event.DomainEventPublisher;
import com.taskagile.doman.common.mail.MailManager;
import com.taskagile.doman.common.mail.MessageVariable;
import com.taskagile.doman.model.user.RegistrationException;
import com.taskagile.doman.model.user.RegistrationManagement;
import com.taskagile.doman.model.user.User;
import com.taskagile.doman.model.user.events.UserRegisteredEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private RegistrationManagement registrationManagement;

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    @Autowired
    private MailManager mailManager;

    @Override
    public void register(RegistrationCommand command) throws RegistrationException {
        Assert.notNull(command, "Parameter `command` must not be null");
        User newUser = registrationManagement.register(
            command.getUsername(),
            command.getEmailAddress(),
            command.getPassword()
        );

        sendWelcomeMessage(newUser);
        domainEventPublisher.publish(new UserRegisteredEvent(newUser));
    }

    private void sendWelcomeMessage(User user) {
        mailManager.send(
            user.getEmailAddress(),
            "Welcome to TaskAgile",
            "welcome.ftl",
            MessageVariable.from("user", user)
        );
    }
}
