package com.bezkoder.springjwt.services;

import com.bezkoder.springjwt.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private static final String URL = "http://qmenusa.com/successRegistration";

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

        String message = "We are so glad you registred on our platefrom, Here's your personal confirmation link : " +
                "نحن سعداء للغاية بتسجيلك على منصتنا ، إليك رابط التأكيد الشخصي";

        String contact = ": للتواصل والاستفسارات " ;
        String twitter = ": تويتر " ;
        String twitterLink = " https://twitter.com/QMenu1 " ;
        String watssp = ": واتساب " ;
        String watsspLink = " https://api.whatsapp.com/message/6XZYOGADORWCF1 " ;
        String em = " : البريد الإلكتروني " ;
        String emLink = " Help@qmenusa.com " ;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("noreply@qmenusa.com");
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + URL + "?token=" + token + "\r\n" + contact + "\r\n"
                + twitter + "\r\n" + twitterLink + "\r\n"
                + watssp + "\r\n" + watsspLink + "\r\n"
                + em + "\r\n" + emLink);
        mailSender.send(email);
    }
}
