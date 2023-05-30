package com.tut.idc.library.configuration;

import com.tut.idc.library.model.enums.*;
import com.tut.idc.library.persistence.*;
import com.tut.idc.library.persistence.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
public class DemoConfiguration {

    private ComputerLabRepository computerLabRepository;
    private ComputerRepository computerRepository;
    private UserRepository userRepository;
    private UserContactRepository userContactRepository;
    private BookingRepository bookingRepository;
    private PCNameIncrementRepository pcNameIncrementRepository;

    @Autowired
    public DemoConfiguration(ComputerLabRepository computerLabRepository, ComputerRepository computerRepository, UserRepository userRepository, UserContactRepository userContactRepository, BookingRepository bookingRepository, PCNameIncrementRepository pcNameIncrementRepository) {
        this.computerLabRepository = computerLabRepository;
        this.computerRepository = computerRepository;
        this.userRepository = userRepository;
        this.userContactRepository = userContactRepository;
        this.bookingRepository = bookingRepository;
        this.pcNameIncrementRepository = pcNameIncrementRepository;
    }

    @Bean
    public InitializingBean creatTestData() {
        return () -> {
            log.info("library-check ::: creating demo data");

            PCNameIncrement pcNameIncrement = pcNameIncrementRepository.save(PCNameIncrement.builder()
                    .computerName("PC-")
                    .increment(0L)
                    .build());
            
            for (int i = 0; i < 10; i++) {
                ComputerLabEntity lab = ComputerLabEntity.builder()
                        .computerLabName("Lab-" + (i+1))
                        .description("E-center")
                        .openingTime("08:00")
                        .closingTime("22:00")
                        .buildingName("B" + (i+1))
                        .status(ComputerLabStatus.OPEN)
                        .build();

                ComputerLabEntity labEntity = computerLabRepository.save(lab);
                log.info(pcNameIncrement.toString());
                log.info(pcNameIncrement.getComputerName() + "" + (pcNameIncrement.getIncrement() + 1));
                ComputerEntity computer = ComputerEntity.builder()
                        .computerName(pcNameIncrement.getComputerName() + "" + (pcNameIncrement.getIncrement() + 1))
                        .brandName("HP")
                        .serialNumber("S4MX" + (i+1))
                        .computerLab(labEntity)
                        .build();

                pcNameIncrement.setIncrement(pcNameIncrement.getIncrement() + 1);
                pcNameIncrementRepository.save(pcNameIncrement);

                computerRepository.save(computer);
            }
            BCryptPasswordEncoder encode = new BCryptPasswordEncoder();
            UserEntity user = UserEntity.builder()
                    .firstname("Library")
                    .lastname("Test User")
                    .IDNumber("9710260367088")
                    .password(encode.encode("safe"))
                    .status(UserStatus.ACTIVE)
                    .userType(UserType.USER)
                    .build();


            UserEntity saveUser = userRepository.save(user);
            List<UserContactEntity> userContactEntities = Arrays.asList(UserContactEntity.builder()
                    .contactPreference(ContactPreference.EMAIL)
                    .contact("rakodi@outlook.com")
                    .status(UserContactOption.PRIMARY)
                    .user(saveUser)
                    .build(), UserContactEntity.builder()
                    .contactPreference(ContactPreference.SMS)
                    .contact("0648785000")
                    .status(UserContactOption.SECONDARY)
                    .user(saveUser)
                    .build());

            UserEntity user1 = UserEntity.builder()
                    .firstname("System")
                    .lastname("User")
                    .IDNumber("9902131067088")
                    .password(encode.encode("safe"))
                    .status(UserStatus.ACTIVE)
                    .userType(UserType.ADMIN)
                    .build();

            UserEntity saveUser1 = userRepository.save(user1);

            List<UserContactEntity> user1ContactEntities = Arrays.asList(UserContactEntity.builder()
                    .contactPreference(ContactPreference.EMAIL)
                    .contact("natasharakodi@gmail.com")
                    .status(UserContactOption.PRIMARY)
                    .user(saveUser1)
                    .build(), UserContactEntity.builder()
                    .contactPreference(ContactPreference.SMS)
                    .contact("0715583369")
                    .status(UserContactOption.SECONDARY)
                    .user(saveUser1)
                    .build());

            userContactRepository.saveAll(userContactEntities);
            userContactRepository.saveAll(user1ContactEntities);

            BookingEntity booking = BookingEntity.builder()
                    .IDNumber("9710260367088")
                    .status(BookingStatus.EXPIRED)
                    .contactPreference(ContactPreference.EMAIL)
                    .computer(computerRepository.findBySerialNumber("S4MX1"))
                    .computerLab(computerLabRepository.findByComputerLabName("Lab-1"))
                    .dateTime(LocalDateTime.parse("2023-05-30T09:00"))
                    .accessToken("87011")
                    .build();
            BookingEntity booking1 = BookingEntity.builder()
                    .IDNumber("9902131067088")
                    .status(BookingStatus.UPCOMING)
                    .contactPreference(ContactPreference.EMAIL)
                    .computer(computerRepository.findBySerialNumber("S4MX1"))
                    .computerLab(computerLabRepository.findByComputerLabName("Lab-1"))
                    .dateTime(LocalDateTime.parse("2023-05-30T12:00"))
                    .accessToken("87011")
                    .build();
            bookingRepository.save(booking);
            bookingRepository.save(booking1);

        };
    }
}
