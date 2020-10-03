package com.taskagile.infrastructure.repository;

import com.taskagile.doman.model.user.User;
import com.taskagile.doman.model.user.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
public class HibernateUserRepositoryTests {

    @TestConfiguration
    public static class UserRepositoryTestContextConfiguration {
        @Bean
        public UserRepository userRepository(EntityManager entityManager) {
            return new HibernateUserRepository(entityManager);
        }
    }

    @Autowired
    private UserRepository userRepository;

    @Test(expected = PersistenceException.class)
    public void save_nullUsernameUser_shouldFail() {
        User invalidUser = User.create("sunny", null, "MyPassword!");
        userRepository.save(invalidUser);
    }

    @Test(expected = PersistenceException.class)
    public void save_nullEmailAddressUser_shouldFail() {
        User invalidUser = User.create("sunny", null, "MyPassword!");
        userRepository.save(invalidUser);
    }

    @Test
    public void save_validUser_shouldSuccess() {
        String username = "sunny";
        String emailAddress = "sunny@taskagile.com";
        User newUser = User.create(username, emailAddress, "MyPassword!");
        userRepository.save(newUser);
        assertNotNull("New user's id should be generated", newUser.getId());
        assertNotNull("New user's created date should be generated", newUser.getCreatedDate());
        assertNotNull(username, newUser.getUsername());
        assertNotNull(emailAddress, newUser.getEmailAddress());
        assertNotNull("", newUser.getFirstName());
        assertNotNull("", newUser.getLastName());
    }

    @Test
    public void save_usernameAlreadyExist_shouldFail() {
        String username = "sunny";
        String emailAddress = "sunny@taskagile.com";
        User alreadyExist = User.create(username, emailAddress, "MyPassword!");
        userRepository.save(alreadyExist);

        try {
            User newUser = User.create(username, "new@taskagile.com", "MyPassword!");
            userRepository.save(newUser);
        } catch (Exception e) {
            assertEquals(ConstraintViolationException.class.toString(), e.getCause().getClass().toString());
        }
    }

    @Test
    public void findByEmailAddress_notExist_shouldReturnEmptyResult() {
        String emailAddress = "sunny@taskagile.com";
        User user = userRepository.findByEmailAddress(emailAddress);
        assertNull("No user should by found", user);
    }

    @Test
    public void findEmailAddress_exist_shouldReturnResult() {
        String emailAddress = "sunny@taskagile.com";
        String username = "sunny";
        User newUser = User.create(username, emailAddress, "MyPassowrd!");
        userRepository.save(newUser);
        User found = userRepository.findByEmailAddress(emailAddress);
        assertEquals("Username should match", username, found.getUsername());
    }

    @Test
    public void findByUsername_notExist_shouldReturnEmptyResult() {
        String username = "sunny";
        User user = userRepository.findByUsername(username);
        assertNull("No user should by found", user);
    }

    @Test
    public void findByUsername_exist_shouldReturnResult() {
        String username = "sunny";
        String emailAddress = "sunny@taskagile.com";
        User newUser = User.create(username, emailAddress, "MyPassword!");
        userRepository.save(newUser);
        User found = userRepository.findByUsername(username);
        assertEquals("Email address should match", emailAddress, found.getEmailAddress());
    }

}
