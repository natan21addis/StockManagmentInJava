package com.inventory.OTP;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

public class OtpManager {

    private final String fromEmail = "nataddis21@gmail.com";     // your Gmail here
    private final String appPassword = "flak vyqj mwae aodx"; // your Gmail app password here

    // Generates a 6-digit OTP as String
    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public String sendOtpToEmail(String toEmail) {
        String otp = generateOtp();

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");


        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(fromEmail, appPassword);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Your OTP Code");
            message.setText("Your OTP is: " + otp);

            Transport.send(message);

            System.out.println("OTP sent to: " + toEmail + " OTP: " + otp);
            return otp;
        } catch (MessagingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
