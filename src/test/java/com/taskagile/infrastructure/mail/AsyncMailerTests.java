package com.taskagile.infrastructure.mail;

import com.taskagile.doman.common.mail.SimpleMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AsyncMailerTests {

    @MockBean
    private JavaMailSender mailSenderMock;

    @Autowired
    private AsyncMailer asyncMailer;

    @Test(expected = IllegalArgumentException.class)
    public void send_nullMessage_shouldFail() {
        asyncMailer.send(null);
    }

    @Test
    public void send_validMessage_shouldSucceed() {
        String from = "system@taskagile.com";
        String to = "console.output@taskagile.com";
        String subject = "A test message";
        String body = "Username: test, Email Address: test@taskagile.com";

        SimpleMessage message = new SimpleMessage(to, subject, body, from);
        asyncMailer.send(message);

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText("Username: test, Email Address: test@taskagile.com");
        verify(mailSenderMock).send(simpleMailMessage);
    }

}
