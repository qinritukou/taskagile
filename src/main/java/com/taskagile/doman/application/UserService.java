package com.taskagile.doman.application;

import com.taskagile.doman.application.commands.RegistrationCommand;
import com.taskagile.doman.model.user.RegistrationException;

public interface UserService {

    /**
     * Register a new user with username, email address, and password.
     *
     * @param command instance of <code>RegistrationCommand</code>
     * @throws RegistrationException when registration failed. Possible reasons are:
     *                               1) Username already exists
     *                               2) Email address already exists.
     */
    void register(RegistrationCommand command) throws RegistrationException;

}
