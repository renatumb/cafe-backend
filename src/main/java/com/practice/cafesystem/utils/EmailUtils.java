package com.practice.cafesystem.utils;

import java.util.List;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class EmailUtils {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendSimpleMessage(String to, String subject, String text, List<String> emailsList) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("renatu.mb@gmail.com");
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);

        if (emailsList != null && emailsList.size() > 0) {
            simpleMailMessage.setCc(emailsList.toArray(new String[0]));
        }
        javaMailSender.send(simpleMailMessage);
    }

    public void forgotMail(String to, String subject, String password) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        //helper.setFrom("renatu.mb@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);

        String htmlMsg= """
                <p><b>  Login Details for Cafe Menagement System </b></p>\n
                <p><b>  Email: </b> """ + to + """ 
                </p><p><b>  Password:</b> """ + password +"</p>";

        message.setContent(htmlMsg, "text/html");
        javaMailSender.send(message);
    }
}
