package com.taskagile.web.apis;

import com.taskagile.config.SecurityConfiguration;
import com.taskagile.doman.application.UserService;
import com.taskagile.doman.model.user.EmailAddressExistsException;
import com.taskagile.doman.model.user.UsernameExistsException;
import com.taskagile.utils.JsonUtils;
import com.taskagile.web.paypload.RegistrationPayload;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {SecurityConfiguration.class, RegistrationApiController.class})
@WebMvcTest
@ActiveProfiles("test")
public class RegistrationApiControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void register_blankPayload_shouldFailAndReturn400() throws Exception {
        mockMvc.perform(post("/api/registrations"))
            .andExpect(status().is(400));
    }

    @Test
    public void register_existedUsername_shouldFailAndReturn400() throws Exception {
        RegistrationPayload payload = new RegistrationPayload();
        payload.setUsername("exist");
        payload.setEmailAddress("test@taskagile.com");
        payload.setPassword("MyPassword!");

        doThrow(UsernameExistsException.class)
            .when(userService)
            .register(payload.toCommand());

        mockMvc.perform(
            post("/api/registrations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(payload)))
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.message").value("Username already exists"));
    }

    @Test
    public void register_existedEmailAddress_shouldFailAndReturn400() throws Exception {
        RegistrationPayload payload = new RegistrationPayload();
        payload.setUsername("test");
        payload.setEmailAddress("exist@taskagile.com");
        payload.setPassword("MyPassword!");

        doThrow(EmailAddressExistsException.class)
            .when(userService)
            .register(payload.toCommand());

        mockMvc.perform(
            post("/api/registrations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(payload))
        ).andExpect(status().is(400))
            .andExpect(jsonPath("$.message").value("Email address already exists"));
    }

    @Test
    public void register_validPayload_shouldSuccessedAndReturn201() throws Exception {
        RegistrationPayload payload = new RegistrationPayload();
        payload.setUsername("sunny");
        payload.setEmailAddress("sunny@taskagile.com");
        payload.setPassword("MyPassword!");

        doNothing().when(userService)
            .register(payload.toCommand());

        mockMvc.perform(
            post("/api/registrations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(payload))
        ).andExpect(status().is(201));
    }

}
