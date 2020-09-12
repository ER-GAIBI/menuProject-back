package com.bezkoder.springjwt.services;

import com.bezkoder.springjwt.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private static final String URL = "http://46.101.151.85:4200/successRegistration";

    @Autowired
    private IUserService service;

    @Autowired
    private JavaMailSender mailSender;

    /*@Value("${spring.mail.username}")
    private String from;*/

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        service.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        /*String confirmationUrl
                = event.getAppUrl() + "/api/auth/regitrationConfirm?token=" + token;*/

        String message = "We are so glad you registred on our platefrom, Here's your personal confirmation link : " +
                "نحن سعداء للغاية بتسجيلك على منصتنا ، إليك رابط التأكيد الشخصي:";

        SimpleMailMessage email = new SimpleMailMessage();
        /*email.setFrom(from);*/
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + URL + "?token=" + token);
        mailSender.send(email);
    }
}
