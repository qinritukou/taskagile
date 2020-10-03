package com.taskagile.doman.model.user;

import com.taskagile.doman.common.security.PasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegistrationManagement {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncryptor passwordEncryptor;

    public User register(String username, String emailAddress, String password) throws RegistrationException {
        User existingUser = repository.findByUsername(username);
        if (existingUser != null) {
            throw new UsernameExistsException();
        }

        existingUser = repository.findByEmailAddress(emailAddress.toLowerCase());
        if (existingUser != null) {
            throw new EmailAddressExistsException();
        }

        String encryptedPassword = passwordEncryptor.encrypt(password);
        User newUser = User.create(username, emailAddress.toLowerCase(), encryptedPassword);
        repository.save(newUser);
        return newUser;
    }
}
