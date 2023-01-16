package com.tut.idc.library.service;

import com.tut.idc.library.model.*;
import com.tut.idc.library.model.enums.ComputerStatus;
import com.tut.idc.library.persistence.*;
import com.tut.idc.library.persistence.entity.*;
import com.tut.idc.library.util.PrimaryContactUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@Slf4j
public class BookingService {

    private static final String BOOKING_SUBJECT = "Lab Assistant - Access Token";
    private static final String BOOKING_MSG = "Hi - %s %s %n%nUse this token - %s - to access %s.";
    private ComputerRepository computerRepository;
    private BookingRepository bookingRepository;
    private UserRepository userRepository;
    private UserContactRepository userContactRepository;
    private ComputerLabRepository computerLabRepository;
    private TokenAgent tokenAgent;
    @Autowired
    public BookingService(ComputerRepository computerRepository, BookingRepository bookingRepository, UserRepository userRepository, UserContactRepository userContactRepository,ComputerLabRepository computerLabRepository, TokenAgent tokenAgent) {
        this.computerRepository = computerRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.userContactRepository = userContactRepository;
        this.computerLabRepository = computerLabRepository;
        this.tokenAgent = tokenAgent;
    }

    public BookingResponse createBooking(BookingDTO bookingDTO) {
        UserEntity user = userRepository.findByIDNumber(bookingDTO.getIDNumber());

        Stream<ComputerEntity> computerEntityStream = computerLabRepository.findByComputerLabName(bookingDTO.getComputerLabName())
                .getComputers().stream()
                .filter(x -> x.getStatus().name().contains(ComputerStatus.AVAILABLE.name()));

        Optional<ComputerEntity> optionalComputer = computerEntityStream
                .findFirst();

        if (!optionalComputer.isPresent()) {
            throw new RuntimeException("no computer available");
        }

        ComputerEntity allocatedComputer = optionalComputer.get();
        String generatedToken = generateToken();

        allocatedComputer.setStatus(ComputerStatus.BOOKED);
        computerRepository.save(allocatedComputer);

        BookingEntity bookingEntity = BookingEntity.builder()
                .computer(allocatedComputer)
                .IDNumber(bookingDTO.getIDNumber())
                .status(ComputerStatus.BOOKED)
                .accessToken(generatedToken)
                .dateAndTime(bookingDTO.getDateAndTime())
                .build();

        BookingResponse bookingResponse = BookingResponse.builder()
                .token(generatedToken)
                .contactPreference(bookingDTO.getContactPreference())
                .computerName(allocatedComputer.getComputerName())
                .build();
        bookingRepository.save(bookingEntity);

        // TODO: send token to user(student) via preferred contact
        sendToken(bookingResponse, user);
        return bookingResponse;
    }

    public List<UserContactDTO> getUserContacts(String IDNumber) {
        List<UserContactEntity> userContacts = userContactRepository.findByUser_IDNumber(IDNumber);
        List<UserContactDTO> userContactDTOS = new ArrayList<>();
        for (UserContactEntity userContact: userContacts) {
            userContactDTOS.add(UserContactDTO.builder()
                    .contact(userContact.getContact())
                    .contactPreference(userContact.getContactPreference())
                    .status(userContact.getStatus())
                    .build());
        }
        return userContactDTOS;
    }

    public List<BookingEntity> getAllBookings(){
        return (List<BookingEntity>) this.bookingRepository.findAll();
    }

    public List<BookingEntity> getAllBookingsForAGivenUser(String IDNumber){
        return this.bookingRepository.findAllByIDNumber(IDNumber);
    }


    private String generateToken() {
        return UUID.randomUUID().toString().substring(7);
    }

    private void sendToken(BookingResponse bookingResponse, UserEntity user) {
        PrimaryContactUtil primaryContactUtil = PrimaryContactUtil.retrievePrimaryContact(userContactRepository.findByUser_IDNumber(user.getIDNumber()));
        log.info(primaryContactUtil.toString());

        TokenAgent agent = tokenAgent.retrieveTokenAgent(primaryContactUtil.getPreferredContact());
        primaryContactUtil.setSubject(BOOKING_SUBJECT);
        primaryContactUtil.setMessageContent(String.format(BOOKING_MSG, user.getFirstname(), user.getLastname(), bookingResponse.getToken(), bookingResponse.getComputerName()));
        agent.sendToken(primaryContactUtil);
    }
}

