package com.tut.idc.library.service;

import com.tut.idc.library.util.PrimaryContactUtil;
import com.tut.idc.library.web.exception.TokenException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class SMSAgent extends TokenAgent{
    public TokenService tokenService;
    @Override
    public void sendToken(PrimaryContactUtil primaryContact){
        String messageToSend = primaryContact.getSubject() + " " + primaryContact.getMessageContent();
        try {
            tokenService.sendSMS(primaryContact.getContactValue(), messageToSend);
        } catch (Exception | Error ex){
            throw new TokenException("Failed to send token.", ex);
        }
    }
}
