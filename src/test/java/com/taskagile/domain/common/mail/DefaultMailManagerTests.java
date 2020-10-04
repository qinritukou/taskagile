package com.taskagile.domain.common.mail;

import com.taskagile.doman.common.mail.DefaultMailManager;
import com.taskagile.doman.common.mail.Mailer;
import com.taskagile.doman.common.mail.Message;
import com.taskagile.doman.common.mail.MessageVariable;
import freemarker.template.Configuration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class DefaultMailManagerTests {

    @TestConfiguration
    static class DefaultMessageCreatorConfiguration {

        @Bean
        @Primary
        public FreeMarkerConfigurationFactoryBean getFreemarkerConfiguration() {
            FreeMarkerConfigurationFactoryBean factoryBean = new FreeMarkerConfigurationFactoryBean();
            factoryBean.setTemplateLoaderPath("/mail-templates/");
            return factoryBean;
        }

    }

    @MockBean
    private Mailer mailer;

    @Autowired
    private Configuration configuration;

    @Autowired
    private DefaultMailManager defaultMailManager;

    @Test(expected = IllegalArgumentException.class)
    public void send_nullEmailAddress_shouldFail() {
        defaultMailManager.send(null, "Test subject", "test.ftl");
    }

    @Test(expected = IllegalArgumentException.class)
    public void send_emptyEmailAddress_shouldFail() {
        defaultMailManager.send("", "Test subject", "test.ftl");
    }

    @Test(expected = IllegalArgumentException.class)
    public void send_nullSubject_shouldFail() {
        defaultMailManager.send("test@taskagile.com", null, "test.tfl");
    }

    @Test(expected = IllegalArgumentException.class)
    public void send_emptySubject_shouldFail() {
        defaultMailManager.send("test@taskagile.com", "", "test.tfl");
    }

    @Test(expected = IllegalArgumentException.class)
    public void send_nullTemplateName_shouldFail() {
        defaultMailManager.send("test@taskagile.com", "Test subject", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void send_emptyTemplateName_shouldFail() {
        defaultMailManager.send("test@taskagile.com", "Test subject", "");
    }

    @Test
    public void send_validParameters_shouldSucceed() {
        String to = "user@example.com";
        String subject = "Test subject";
        String templateName = "test.ftl";

        defaultMailManager.send(to, subject, templateName, MessageVariable.from("name", "test"));
        ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
        verify(mailer).send(messageArgumentCaptor.capture());

        Message messageSent = messageArgumentCaptor.getValue();
        assertEquals(to, messageSent.getTo());
        assertEquals(subject, messageSent.getSubject());
        assertEquals("noreply@taskagile.com", messageSent.getFrom());
//        assertEquals("Hello, test\n", messageSent.getBody());
    }

}
