package com.tut.idc.library.service;

import com.tut.idc.library.util.PrimaryContactUtil;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class EmailAgent extends TokenAgent {
    public TokenService tokenService;
    @Override
    public void sendToken(PrimaryContactUtil primaryContact) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(primaryContact.getContactValue());
        mailMessage.setSubject(primaryContact.getSubject());
        mailMessage.setText(primaryContact.getMessageContent());
        tokenService.sendEmail(mailMessage);
    }
}
