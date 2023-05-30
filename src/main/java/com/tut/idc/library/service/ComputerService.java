package com.tut.idc.library.service;

import com.tut.idc.library.model.BookingResponse;
import com.tut.idc.library.model.ComputerDTO;
import com.tut.idc.library.model.enums.BookingStatus;
import com.tut.idc.library.persistence.*;
import com.tut.idc.library.persistence.entity.*;
import com.tut.idc.library.util.ComputerLabMapper;
import com.tut.idc.library.util.PrimaryContactUtil;
import com.tut.idc.library.web.exception.EntityAlreadyExistsException;
import com.tut.idc.library.web.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ComputerService {
    private static final String NOTIFICATION_SUBJECT = "Lab Assistant - Computer Maintenance";
    private static final String NOTIFICATION_MSG = "Hi - %s %s %n%n%s is unavailable due to maintenance. Please book again. Sorry for the inconvenience.";
    private ComputerLabRepository computerLabRepository;
    private ComputerRepository computerRepository;
    private BookingRepository bookingRepository;
    private UserRepository userRepository;
    final private UserContactRepository userContactRepository;
    private PCNameIncrementRepository pcNameIncrementRepository;
    final private TokenAgent tokenAgent;
    private ComputerLabMapper mapper;

    @Autowired
    public ComputerService(ComputerLabRepository computerLabRepository, ComputerRepository computerRepository, BookingRepository bookingRepository, UserRepository userRepository, UserContactRepository userContactRepository, PCNameIncrementRepository pcNameIncrementRepository, TokenAgent tokenAgent) {
        this.computerLabRepository = computerLabRepository;
        this.computerRepository = computerRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.userContactRepository = userContactRepository;
        this.pcNameIncrementRepository = pcNameIncrementRepository;
        this.tokenAgent = tokenAgent;
        this.mapper = Mappers.getMapper(ComputerLabMapper.class);
    }

    public ComputerDTO addComputer(ComputerDTO computer) {
        ComputerLabEntity computerLab = computerLabRepository.findByComputerLabName(computer.getComputerLab().getComputerLabName());
        PCNameIncrement pcNameIncrement = ((List<PCNameIncrement>) pcNameIncrementRepository.findAll()).get(0);
        if (computerRepository.findBySerialNumber(computer.getSerialNumber()) != null) {
            throw new EntityAlreadyExistsException("Computer with serial no - " + computer.getSerialNumber() + "- ");
        }
        ComputerEntity newComputer = mapper.convertComputerDtoToEntity(computer);
        newComputer.setComputerName(pcNameIncrement.getComputerName() + "" + (pcNameIncrement.getIncrement() + 1));
        newComputer.setBrandName(computer.getBrandName());
        newComputer.setSerialNumber(computer.getSerialNumber());
        newComputer.setComputerLab(computerLab);

        pcNameIncrement.setIncrement(pcNameIncrement.getIncrement() + 1);
        pcNameIncrementRepository.save(pcNameIncrement);

        return mapper.convertComputerEntityToDTO(computerRepository.save(newComputer));
    }

    public List<ComputerDTO> retrieveAllComputerLabComputers(String computerLabName) {
        List<ComputerEntity> computers = computerRepository.findByComputerLabId(computerLabRepository.findByComputerLabName(computerLabName).getId());

        if (computers == null || computers.isEmpty()) {
            throw new EntityNotFoundException("Computers ", EntityNotFoundException.NOT_FOUND_MESSAGE);
        }
        return mapper.convertComputerEntityToDTO(computers);
    }

    public List<ComputerDTO> getAllComputers() {
        List<ComputerEntity> allComputers = (List<ComputerEntity>) computerRepository.findAll();
        return mapper.convertComputerEntityToDTO(allComputers);
    }

    public ComputerDTO updateComputer(ComputerDTO computerDTO) {
        ComputerEntity computerEntity = this.computerRepository.findByComputerName(computerDTO.getComputerName());

        computerEntity.setBrandName(computerDTO.getBrandName());
        computerEntity.setSerialNumber(computerDTO.getSerialNumber());

        return mapper.convertComputerEntityToDTO(this.computerRepository.save(computerEntity));
    }

    public List<ComputerDTO> unlinkComputer(String computerName){
        final ComputerEntity computerEntity = this.computerRepository.findByComputerName(computerName);
        final List<BookingEntity> bookingEntity = this.bookingRepository.findByComputerId(computerEntity.getId());
        final List<UserEntity> users = new ArrayList<>();

        if(!bookingEntity.isEmpty()){
           bookingEntity.forEach(booking -> {
               if(booking.getStatus().equals(BookingStatus.UPCOMING)){
                   UserEntity user = userRepository.findByIDNumber(booking.getIDNumber());
                   if(!users.contains(user)){
                       users.add(user);
                   }
                   bookingRepository.delete(booking);
               }else {
                   bookingRepository.delete(booking);
               }
           });
        }

        if(!users.isEmpty()){
            users.forEach(user -> {
                sendNotification(computerEntity.getComputerName(), user);
            });
        }

        computerEntity.setComputerLab(null);
        computerRepository.save(computerEntity);
        return getAllComputers();
    }

    public List<ComputerDTO> linkComputer(ComputerDTO computerDTO){
        final ComputerEntity computerEntity = this.computerRepository.findByComputerName(computerDTO.getComputerName());
        final ComputerLabEntity computerLabEntity = this.computerLabRepository.findByComputerLabName(computerDTO.getComputerLab().getComputerLabName());
        computerEntity.setComputerLab(computerLabEntity);
        computerRepository.save(computerEntity);
        return getAllComputers();
    }

    public List<ComputerDTO> deleteComputer(ComputerDTO computerDTO) {
        final ComputerEntity computerEntity = this.computerRepository.findByComputerName(computerDTO.getComputerName());
        this.computerRepository.delete(computerEntity);
        return getAllComputers();
    }
    private void sendNotification(String computerName, UserEntity user) {
        PrimaryContactUtil primaryContactUtil = PrimaryContactUtil.retrievePrimaryContact(userContactRepository.findByUser_IDNumber(user.getIDNumber()));

        TokenAgent agent = tokenAgent.retrieveTokenAgent(primaryContactUtil.getPreferredContact());
        primaryContactUtil.setSubject(NOTIFICATION_SUBJECT);
        primaryContactUtil.setMessageContent(String.format(NOTIFICATION_MSG, user.getFirstname(), user.getLastname(), computerName));
        log.info(primaryContactUtil.toString());
        agent.sendToken(primaryContactUtil);
    }
}
