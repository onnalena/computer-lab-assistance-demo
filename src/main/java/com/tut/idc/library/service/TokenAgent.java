package com.tut.idc.library.service;

import com.tut.idc.library.model.enums.ContactPreference;
import com.tut.idc.library.util.PrimaryContactUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class TokenAgent {

    @Autowired
    private TokenService service;
    public TokenAgent retrieveTokenAgent(final ContactPreference contactPreference) {
        if (ContactPreference.EMAIL.equals(contactPreference)) {
            return new EmailAgent(service);
        } else {
            return new SMSAgent(service);
        }
    }
    public void sendToken(PrimaryContactUtil primaryContactUtil) {
      log.info("library-check ::: invoking token-agent sendToken");
    }
}
