package br.com.developeralexandresantos.cursomc.config;

import br.com.developeralexandresantos.cursomc.services.DBService;
import br.com.developeralexandresantos.cursomc.services.EmailService;
import br.com.developeralexandresantos.cursomc.services.MockEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.text.ParseException;

@Configuration
@Profile("test")
public class TestConfig {

    @Autowired
    private DBService dbService;

    @Bean
    public boolean instantiateDataBase() throws ParseException {
        dbService.instantiateTestDatabase();
        return true;
    }
    
    @Bean
    public EmailService emailService() {
    	return new MockEmailService();
    }

    @Bean
    public JavaMailSender JMS () {
        return new JavaMailSenderImpl();
    }
}
