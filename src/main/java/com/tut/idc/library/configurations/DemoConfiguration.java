package com.tut.idc.library.configurations;

import com.tut.idc.library.model.enums.*;
import com.tut.idc.library.persistence.ComputerLabRepository;
import com.tut.idc.library.persistence.ComputerRepository;
import com.tut.idc.library.persistence.UserContactRepository;
import com.tut.idc.library.persistence.UserRepository;
import com.tut.idc.library.persistence.entity.ComputerEntity;
import com.tut.idc.library.persistence.entity.ComputerLabEntity;
import com.tut.idc.library.persistence.entity.UserContactEntity;
import com.tut.idc.library.persistence.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
public class DemoConfiguration {

    private ComputerLabRepository computerLabRepository;
    private ComputerRepository computerRepository;
    private UserRepository userRepository;
    private UserContactRepository userContactRepository;

    @Autowired
    public DemoConfiguration(ComputerLabRepository computerLabRepository, ComputerRepository computerRepository, UserRepository userRepository, UserContactRepository userContactRepository) {
        this.computerLabRepository = computerLabRepository;
        this.computerRepository = computerRepository;
        this.userRepository = userRepository;
        this.userContactRepository = userContactRepository;
    }

    @Bean
    public InitializingBean creatTestData() {
        return () -> {
            log.info("library-check ::: creating demo data");

            for (int i = 0; i < 10; i++) {
                ComputerLabEntity lab = ComputerLabEntity.builder()
                        .computerLabName("Lab-" + i+1)
                        .description("E-center" + i+1)
                        .status(ComputerLabStatus.OPEN)
                        .build();

                ComputerLabEntity labEntity = computerLabRepository.save(lab);

                ComputerEntity computer = ComputerEntity.builder()
                        .computerName("PC1" + i+1)
                        .status(ComputerStatus.AVAILABLE)
                        .computerLab(labEntity)
                        .build();

                computerRepository.save(computer);
            }

            UserEntity user = UserEntity.builder()
                    .firstname("Library")
                    .lastname("Test User")
                    .IDNumber("216153804")
                    .password("safe")
                    .status(UserStatus.ACTIVE)
                    .build();

            UserEntity saveUser = userRepository.save(user);

            List<UserContactEntity> userContactEntities = Arrays.asList(UserContactEntity.builder()
                    .contactPreference(ContactPreference.EMAIL)
                    .contact("onalennarakodi@outlook.com")
                    .status(UserContactOption.SECONDARY)
                    .user(saveUser)
                    .build(), UserContactEntity.builder()
                    .contactPreference(ContactPreference.SMS)
                    .contact("0648785074")
                    .status(UserContactOption.PRIMARY)
                    .user(saveUser)
                    .build());

            userContactRepository.saveAll(userContactEntities);
        };
    }
}
