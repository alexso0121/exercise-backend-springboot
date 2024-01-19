package com.Acerise.System_api.utils;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {

    private final JavaMailSender javaMailSender;

    @Value("${server-domain}")
    private String serverDomain;

    @Value("${client-domain}")
    private String clientDomain;

    public EmailUtil(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    public void sendOtpEmail(String email,String id, String otp) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Verify OTP");
        mimeMessageHelper.setText("""
        <div>
          <a href="%s/register?id=%s&otp=%s" target="_blank">click link to verify</a>
        </div>
        """.formatted(clientDomain,id, otp), true);

        javaMailSender.send(mimeMessage);
    }
}
