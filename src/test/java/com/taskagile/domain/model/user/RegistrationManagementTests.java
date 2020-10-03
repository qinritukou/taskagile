package com.taskagile.domain.model.user;

import com.sun.deploy.association.RegisterFailedException;
import com.taskagile.doman.common.security.PasswordEncryptor;
import com.taskagile.doman.model.user.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegistrationManagementTests {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncryptor passwordEncryptor;

    @Autowired
    private RegistrationManagement registrationManagement;

    @Test(expected = UsernameExistsException.class)
    public void register_existedUsername_shouldFail() throws RegistrationException {
        String username = "existUsername";
        String emailAddress = "sunny@taskagile.com";
        String password = "MyPassword!";

        when(userRepository.findByUsername(username)).thenReturn(new User());
        registrationManagement.register(username, emailAddress, password);
    }

    @Test(expected = EmailAddressExistsException.class)
    public void register_existedEmailAddress_shouldFail() throws RegistrationException {
        String username = "sunny";
        String emailAddress = "existing@taskagile.com";
        String password = "MyPassword";

        when(userRepository.findByEmailAddress(emailAddress)).thenReturn(new User());
        registrationManagement.register(username, emailAddress, password);
    }

    @Test
    public void register_uppercaseEmailAddress_shouldSucceedAndBecomeLowerCase() throws RegistrationException {
        String username = "sunny";
        String emailAddress = "Sunny@TaskAgile.com";
        String password = "MyPassword!";
        registrationManagement.register(username, emailAddress, password);
        User userToSave = User.create(username, emailAddress.toLowerCase(), password);
        verify(userRepository).save(userToSave);
    }

    @Test
    public void register_newUser_shouldSucceed() throws RegisterFailedException, RegistrationException {
        String username = "sunny";
        String emailAddress = "sunny@taskagile.com";
        String password = "MyPassword!";
        String encryptedPassword = "EncryptedPassword";
        User newUser = User.create(username, emailAddress, encryptedPassword);

        // Setup repository mock
        // Return null to indicate no user exists
        when(userRepository.findByUsername(username)).thenReturn(null);
        when(userRepository.findByEmailAddress(emailAddress)).thenReturn(null);
        doNothing().when(userRepository).save(newUser);
        // Setup passwordEncryptor mock
        when(passwordEncryptor.encrypt(password)).thenReturn("EncryptedPassword");

        User savedUser = registrationManagement.register(username, emailAddress, password);
        InOrder inOrder = inOrder(userRepository);
        inOrder.verify(userRepository).findByUsername(username);
        inOrder.verify(userRepository).findByEmailAddress(emailAddress);
        inOrder.verify(userRepository).save(newUser);
        verify(passwordEncryptor).encrypt(password);
        assertEquals("Saved user's password should be encrypted", encryptedPassword, savedUser.getPassword());
    }

}
