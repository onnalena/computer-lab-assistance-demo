package com.tut.idc.library.service;

import com.tut.idc.library.web.exception.TokenException;
import com.tut.idc.library.util.PrimaryContactUtil;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;


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
        try {
            tokenService.sendEmail(mailMessage);
        } catch (Exception | Error ex){
            throw new TokenException("Failed to send token.");
        }
    }
}
