package com.tut.idc.library.service;

import com.tut.idc.library.model.*;
import com.tut.idc.library.model.enums.BookingStatus;
import com.tut.idc.library.persistence.*;
import com.tut.idc.library.persistence.entity.*;
import com.tut.idc.library.util.ComputerLabMapper;
import com.tut.idc.library.util.PrimaryContactUtil;
import com.tut.idc.library.web.exception.DateException;
import com.tut.idc.library.web.exception.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Slf4j
public class BookingService {

    private static final String BOOKING_SUBJECT = "Lab Assistant - Access Token";
    private static final String BOOKING_MSG = "Hi - %s %s %n%nUse this token - %s - to access %s in %s";
    private ComputerRepository computerRepository;
    private BookingRepository bookingRepository;
    private UserRepository userRepository;
    private UserContactRepository userContactRepository;
    private ComputerLabRepository computerLabRepository;
    private TokenAgent tokenAgent;
    private ComputerLabMapper mapper;
    @Autowired
    public BookingService(ComputerRepository computerRepository, BookingRepository bookingRepository, UserRepository userRepository, UserContactRepository userContactRepository,ComputerLabRepository computerLabRepository, TokenAgent tokenAgent) {
        this.computerRepository = computerRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.userContactRepository = userContactRepository;
        this.computerLabRepository = computerLabRepository;
        this.tokenAgent = tokenAgent;
        this.mapper = Mappers.getMapper(ComputerLabMapper.class);
    }

    public BookingResponse createBooking(BookingDTO bookingDTO) {

        if(userRepository.findByIDNumber(bookingDTO.getIDNumber()) == null){
            throw new EntityNotFoundException("User with ID - " + bookingDTO.getIDNumber(),
                    EntityNotFoundException.NOT_FOUND_MESSAGE);
        }

        if(LocalDateTime.parse(bookingDTO.getDateTime()).isBefore(LocalDateTime.now())){
            throw new DateException(String.format("Time - %s - has passed.",
                    bookingDTO.getDateTime().substring(bookingDTO.getDateTime().indexOf('T') + 1)));
        }

        if(!bookingRepository.findAllByIDNumber(bookingDTO.getIDNumber()).isEmpty()){

            if(!bookingRepository.findByDateTimeAndIDNumber(LocalDateTime.parse(bookingDTO.getDateTime()), bookingDTO.getIDNumber())
                    .stream().filter(x -> x.getStatus().equals(BookingStatus.UPCOMING)).collect(Collectors.toList()).isEmpty()){

                throw new DateException(String.format("User with ID - %s - already has a booking at %s",
                        bookingDTO.getIDNumber(), bookingDTO.getDateTime().substring(bookingDTO.getDateTime()
                                .indexOf('T') + 1)));
            }
        }


        UserEntity user = userRepository.findByIDNumber(bookingDTO.getIDNumber());

        ComputerEntity allocatedComputer = getAvailableComputer(bookingDTO.getComputerLabName(), LocalDateTime.parse(bookingDTO.getDateTime()));
        ComputerLabEntity computerLab = allocatedComputer.getComputerLab();
        String generatedToken = generateToken();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        BookingEntity bookingEntity = BookingEntity.builder()
                .computer(allocatedComputer)
                .computerLab(computerLab)
                .IDNumber(bookingDTO.getIDNumber())
                .accessToken(encoder.encode(generatedToken))
                .dateTime(LocalDateTime.parse(bookingDTO.getDateTime()))
                .contactPreference(bookingDTO.getContactPreference())
                .status(BookingStatus.UPCOMING)
                .build();

        BookingResponse bookingResponse = BookingResponse.builder()
                .token(generatedToken)
                .contactPreference(bookingDTO.getContactPreference())
                .computerName(allocatedComputer.getComputerName())
                .computerLabName(allocatedComputer.getComputerLab().getComputerLabName())
                .build();
        bookingRepository.save(bookingEntity);
        computerRepository.save(allocatedComputer);

        sendToken(bookingResponse, user);
        return bookingResponse;
    }

    public ComputerEntity getAvailableComputer(String computerLabName, LocalDateTime bookingDate) {
        List<ComputerEntity> labComputers = computerRepository.findByComputerLabId(computerLabRepository
                .findByComputerLabName(computerLabName).getId());

        if(labComputers.isEmpty()){
           throw new EntityNotFoundException(computerLabName + " has no computers",
                   EntityNotFoundException.AVAILABILITY_MESSAGE);
        }

        for (ComputerEntity computer : labComputers) {
            log.info("internal-debug ::: Checking {}",computer.getComputerName());

            if(bookingRepository.findByComputerId(computer.getId()) != null &&
                    !bookingRepository.findByComputerId(computer.getId()).isEmpty()){

                log.info("internal-debug ::: Computer exists in booking table");
                BookingEntity booking = bookingRepository.findByComputerId(computer.getId())
                        .stream().filter(x -> x.getDateTime().equals(bookingDate) &&
                                x.getStatus().equals(BookingStatus.UPCOMING)).findFirst().orElse(null);
               
                if (booking == null){
                    log.info("internal-debug ::: Booking DateTime doesn't match any existing bookings");
                    return computer;
                }
                log.info("internal-debug ::: Booking DateTime matches an existing booking");
            } else {
                log.info("internal-debug ::: Computer doesn't exists in booking table");
                return computer;
            }
        }
        throw new EntityNotFoundException(computerLabName + " has no computers available at this time.");
    }

    public List<UserContactDTO> getUserContacts(String IDNumber) {
        List<UserContactEntity> userContacts = userContactRepository.findByUser_IDNumber(IDNumber);
        return mapper.convertUserContactEntityToDTO(userContacts);
    }

    public List<BookingDTO> getAllBookings(){
        if(this.bookingRepository.findAll() == null){
            throw new EntityNotFoundException("No bookings ", EntityNotFoundException.AVAILABILITY_MESSAGE);
        }
        List<BookingEntity> allBookings = (List<BookingEntity>) this.bookingRepository.findAll();
        return mapper.convertBookingEntityToDTO(allBookings);
    }

    public List<BookingDTO> getUserBookings(String IDNumber){
        if(this.bookingRepository.findAllByIDNumber(IDNumber) == null){
            throw new EntityNotFoundException("No bookings ", EntityNotFoundException.AVAILABILITY_MESSAGE);
        }
        List<BookingEntity> userBookings = this.bookingRepository.findAllByIDNumber(IDNumber);
        return mapper.convertBookingEntityToDTO(userBookings);
    }

    public List<String> getAvailableTimeSlots(BookingDateSelected dateSelected){
        if(dateSelected.getSelectedDate().isBefore(LocalDate.now())){
            throw new DateException(String.format("Date - %s - has passed.", dateSelected.getSelectedDate()));
        }

        List<String> availableTimeSlot = new ArrayList<>();
        ComputerLabEntity computerLab = computerLabRepository.findByComputerLabName(dateSelected.getComputerLabName());
        LocalTime openingTime = LocalTime.parse(computerLab.getOpeningTime());
        LocalTime closingTime = LocalTime.parse(computerLab.getClosingTime());
        int hrsCount = closingTime.getHour() - openingTime.getHour();
        int time = openingTime.getHour();

        for (int i = 0; i < hrsCount; i++) {
            String timeSlot ="";

                if(time < 10){
                    timeSlot = "0" + String.valueOf(time).concat(":00");
                } else {
                    timeSlot = String.valueOf(time).concat(":00");
                }
                LocalDateTime dateTime = dateSelected.getSelectedDate().atTime(LocalTime.parse(timeSlot));
                log.info(dateTime.toString());
                if(!dateTime.isBefore(LocalDateTime.now())){
                    availableTimeSlot.add(timeSlot);
                }
            time += 1;
        }

        if(availableTimeSlot.isEmpty()){
            throw new EntityNotFoundException(computerLab.getComputerLabName() + " has no timeslots ",
                    EntityNotFoundException.AVAILABILITY_MESSAGE);
        }

        return availableTimeSlot;
    }

    public List<BookingDTO> cancelBooking(BookingDTO bookingDTO){
        BookingEntity bookingEntity = this.bookingRepository.findByDateTimeAndIDNumber(
                LocalDateTime.parse(bookingDTO.getDateTime()), bookingDTO.getIDNumber()).stream().filter(x ->
                x.getStatus().equals(BookingStatus.UPCOMING)).findFirst().get();
        bookingEntity.setStatus(BookingStatus.CANCELLED);
        this.bookingRepository.save(bookingEntity);

        return mapper.convertBookingEntityToDTO(this.bookingRepository.findAllByIDNumber(bookingDTO.getIDNumber()));
    }

    @Scheduled(cron = "${booking.scheduler}")
    public void changeBookingStatus(){
        log.info("internal::debug - Running schedule.");
        try {
            final LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime bookingTime = LocalDateTime.now();

            bookingTime = bookingTime.withMinute(0);
            bookingTime = bookingTime.withSecond(0);
            bookingTime = bookingTime.withNano(0);

            final List<BookingEntity> bookings = bookingRepository.findByDateTime(bookingTime);

            log.info("booking {}", bookings);
            log.info("bookingTime {}", bookingTime);
            log.info("currentTime {}", currentDateTime);
            bookings.forEach(booking -> {
                if (currentDateTime.getMinute() >= 7 && booking.getStatus().equals(BookingStatus.UPCOMING)){
                    booking.setStatus(BookingStatus.EXPIRED);
                    bookingRepository.save(booking);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String generateToken() {
        return UUID.randomUUID().toString().substring(0,6);
    }

    private void sendToken(BookingResponse bookingResponse, UserEntity user) {
        PrimaryContactUtil primaryContactUtil = PrimaryContactUtil.retrievePrimaryContact(userContactRepository.findByUser_IDNumber(user.getIDNumber()));
        log.info(primaryContactUtil.toString());

        TokenAgent agent = tokenAgent.retrieveTokenAgent(primaryContactUtil.getPreferredContact());
        primaryContactUtil.setSubject(BOOKING_SUBJECT);
        primaryContactUtil.setMessageContent(String.format(BOOKING_MSG, user.getFirstname(), user.getLastname(),
                bookingResponse.getToken(), bookingResponse.getComputerName(), bookingResponse.getComputerLabName()));
        agent.sendToken(primaryContactUtil);
    }
}

