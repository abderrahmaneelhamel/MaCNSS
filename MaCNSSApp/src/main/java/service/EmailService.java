package service;

import model.Agent;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalTime;
import java.util.Properties;

public class EmailService {

    private final String smtpHost = "smtp.gmail.com";
    private final String smtpPort = "587";
    private final String smtpUsername = "abdoelhamel359@gmail.com";
    private final String smtpPassword = "ixoh fafz wbvh pasl";

    public void sendCode(Agent authenticatedAgent, int code) {
        String recipientEmail = authenticatedAgent.getEmail(); // Assuming Agent class has a getEmail() method
        String subject = "Your Verification Code";
        String messageBody = "Your verification code is: " + code;

        final String username = "najouabelhaj7@gmail.com";
        final String password = "sfoy kzkg rbna pnnx";
        Properties properties = System.getProperties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.put("mail.smtp.starttls.enable", "true");
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUsername, smtpPassword);
            }
        });


        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(smtpUsername));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(messageBody);

            Transport.send(message);
            System.out.println("Email sent successfully to: " + recipientEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    public void sendCode2(Agent authenticatedAgent, int code) {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.put("mail.smtp.starttls.enable", "true");
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUsername, smtpPassword);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(smtpUsername));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(authenticatedAgent.getEmail()));
            message.setSubject("khribi9a");
            message.setText("the code : "+code);
            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();

        }
    }

}
