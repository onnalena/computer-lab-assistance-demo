package com.tut.idc.library.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TokenService {

    private JavaMailSender javaMailSender;
    private String sender;
    private String twilioAccountSID;
    private String twilioAuthenticationToken;
    private String twilioCellNumber;
    private String twilioUsername;


    @Autowired
    public TokenService(
                        @Value("${spring.mail.username}") String sender,
                        @Value("${twilio.account.sid}") String twilioAccountSID,
                        @Value("${twilio.account.sid}") String twilioUsername,
                        @Value("${twilio.authentication.token}") String twilioAuthenticationToken,
                        @Value("${twilio.phone.number}") String twilioCellNumber, JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
        this.sender = sender;
        this.twilioAccountSID = twilioAccountSID;
        this.twilioUsername = twilioUsername;
        this.twilioAuthenticationToken = twilioAuthenticationToken;
        this.twilioCellNumber = twilioCellNumber;
    }

    public String sendEmail(SimpleMailMessage mailMessage){
        mailMessage.setFrom(sender);
        mailMessage.setBcc(mailMessage.getTo()[0]);
        mailMessage.setCc(mailMessage.getTo()[0]);
        javaMailSender.send(mailMessage);

        return "Email sent successfully";
    }

    public String sendSMS(String toCellNumber, String message){
        String cellNumber = "+27"+toCellNumber.substring(1);
        log.info(cellNumber);
        Twilio.init(twilioUsername, twilioAuthenticationToken, twilioAccountSID);
        Message.creator(new PhoneNumber(cellNumber),
                new PhoneNumber(twilioCellNumber), message).create();
        return "SMS sent successfully";
    }

}
