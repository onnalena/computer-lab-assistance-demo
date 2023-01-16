package com.tut.idc.library.web;


import com.tut.idc.library.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.AddressException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("dev-test")
public class DevTestController {

    @Autowired
    private TokenService tokenService;


    @PostMapping("/test-send-email")
    public ResponseEntity<String> sendEmail(@RequestBody SimpleMailMessage mailMessage) throws AddressException {
        return new ResponseEntity<>(tokenService.sendEmail(mailMessage), HttpStatus.OK);
    }

    @PostMapping("/test-send-sms")
    private  ResponseEntity<String> sendSMS(@RequestParam String toCellNumber, @RequestParam String message){
        return new ResponseEntity<>(tokenService.sendSMS(toCellNumber, message), HttpStatus.OK);
    }

   /* @PostMapping("/send-sms")
    private ResponseEntity<SmsResponse> sendSms(@RequestBody SmsRequest smsRequest) throws URISyntaxException {
        return smsService.sendSms(smsRequest);
    }*/


}
