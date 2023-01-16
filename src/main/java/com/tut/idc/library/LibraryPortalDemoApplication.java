package com.tut.idc.library;

import com.tut.idc.library.persistence.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class LibraryPortalDemoApplication {
    @Autowired
    private UserRepository userRepository;
    public static void main(String[] args) {
        SpringApplication.run(LibraryPortalDemoApplication.class, args);
    }

    @Bean
    public InitializingBean creatTestDataT() {
        return () -> {
            log.info("===========");
            log.info("findUserById {} ", userRepository.findByIDNumber("216153804"));
            log.info("===========");
        };
    }
}
