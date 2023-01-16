package com.tut.idc.library.service;

import com.tut.idc.library.model.*;
import com.tut.idc.library.model.enums.ContactPreference;
import com.tut.idc.library.model.enums.OTPStatus;
import com.tut.idc.library.model.enums.UserStatus;
import com.tut.idc.library.persistence.UserContactRepository;
import com.tut.idc.library.persistence.UserRepository;
import com.tut.idc.library.persistence.entity.OTPEntity;
import com.tut.idc.library.persistence.OTPRepository;
import com.tut.idc.library.persistence.entity.UserContactEntity;
import com.tut.idc.library.persistence.entity.UserEntity;
import com.tut.idc.library.util.ComputerLabMapper;
import com.tut.idc.library.util.PrimaryContactUtil;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;

@Service
@Slf4j
public class UserService {

    private static final String OTP_SUBJECT = "Lab Assistant - Verify %s";
    private static final String OTP_MSG = "Hi, %s %s%n%n Your OTP to verify your %s - %s";
    private UserRepository userRepository;
    private TokenAgent tokenAgent;
    private OTPRepository oneTimePinRepository;
    private UserContactRepository userContactRepository;
    private ComputerLabMapper mapper;
    @Autowired
    public UserService(UserRepository userRepository, TokenAgent tokenAgent, OTPRepository oneTimePinRepository, UserContactRepository userContactRepository) {
        this.userRepository = userRepository;
        this.tokenAgent = tokenAgent;
        this.oneTimePinRepository = oneTimePinRepository;
        this.userContactRepository = userContactRepository;
        this.mapper = Mappers.getMapper(ComputerLabMapper.class);
    }

    public UserDTO addUser(UserDTO user){
        UserEntity existingStudent = userRepository.findByIDNumber(user.getIDNumber());

        if (existingStudent != null) {
            throw new RuntimeException("User already exits!");
        }

        user.setPassword(encryptPassword(user.getPassword()));

        UserEntity savedUser = userRepository.save(mapper.convertUserDtoToEntity(user));
        List<UserContactEntity> userContactEntities = mapper.convertUserContactDtoToEntity(user.getUserContacts());
        userContactEntities.forEach(x -> x.setUser(savedUser));

        List<UserContactEntity> savedContacts = (List<UserContactEntity>) userContactRepository.saveAll(userContactEntities);
        savedUser.setContact(savedContacts);
        sendOTP(savedUser);

        return user;
    }

    public UserDTO updateUser(UserDTO updatedUserContactDetails) {
        List<UserContactEntity> userContactEntities = mapper.convertUserContactDtoToEntity(updatedUserContactDetails.getUserContacts());

        UserContactEntity updatedEmailDetails = userContactEntities.stream().filter((x) -> ContactPreference.EMAIL.equals(x.getContactPreference()))
                .findFirst().orElseThrow(null);
        UserContactEntity updatedCellPhoneDetails = userContactEntities.stream().filter((x) -> ContactPreference.SMS.equals(x.getContactPreference()))
                .findFirst().orElseThrow(null);

        List<UserContactEntity> existingUserContacts = userContactRepository.findByUser_IDNumber(updatedUserContactDetails.getIDNumber());

        updatedEmailDetails.setId(existingUserContacts.stream().filter(x -> x.getContactPreference().equals(ContactPreference.EMAIL)).findFirst().get().getId());
        updatedCellPhoneDetails.setId(existingUserContacts.stream().filter(x -> x.getContactPreference().equals(ContactPreference.SMS)).findFirst().get().getId());

        UserEntity existingUser = userRepository.findByIDNumber(updatedUserContactDetails.getIDNumber());

        userContactEntities.forEach(x -> x.setUser(existingUser));

        userContactRepository.saveAll(userContactEntities);
        existingUser.setContact(userContactEntities);

        sendOTP(existingUser);

        return updatedUserContactDetails;
    }

    public OTPEntity verifyOTP(String IDNumber, String oneTimePin){
        OTPEntity oneTimePinEntity = oneTimePinRepository.findByIDNumber(IDNumber);
        UserEntity user = userRepository.findByIDNumber(IDNumber);
        if(!oneTimePinEntity.getOneTimePin().equals(oneTimePin)){
            throw new RuntimeException("Invalid OTP!");
        }

        oneTimePinEntity.setStatus(OTPStatus.ACTIONED);
        user.setStatus(UserStatus.ACTIVE);
        log.info("OTP Verified!");
        userRepository.save(user);
        return oneTimePinRepository.save(oneTimePinEntity);
    }

    public UserDTO verifyLogin(LoginDTO userLoginDetails){
        UserEntity user = userRepository.findByIDNumber(userLoginDetails.getIDNumber());
        BCryptPasswordEncoder verifyPassword = new BCryptPasswordEncoder();

        if(user == null){
            throw new RuntimeException("User doesn't exist!");
        }
        if(!verifyPassword.matches(userLoginDetails.getPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect password!");
        }

        List<UserContactEntity> userContactsEntity = userContactRepository.findByUser_IDNumber(user.getIDNumber());
        List<UserContactDTO> userContactDTOS = mapper.convertUserContactEntityToDTO(userContactsEntity);
        UserDTO userDTO = mapper.convertUserEntityToDTO(user);
        userDTO.setUserContacts(userContactDTOS);

        log.info("Successfully logged In!");
        return userDTO;
    }

    public List<UserDTO> getAllUsers(){
        List<UserEntity> userEntities = (List<UserEntity>) this.userRepository.findAll();
        return mapper.convertUserEntityToDTO(userEntities);
    }

    public UserDTO getUser(String IDNumber){
        Optional<UserEntity> optionalUserEntity = userRepository.findById(IDNumber);
        if (!optionalUserEntity.isPresent()) {
            throw new RuntimeException("User with id ---- " + IDNumber + " not found");
        }
        return mapper.convertUserEntityToDTO(optionalUserEntity.get());
    }

    //PRIVATE METHODS
    private void sendOTP(UserEntity user) {
        String oneTimePin = generateOTP();
        OTPEntity oneTimePinEntity = OTPEntity.builder()
                .IDNumber(String.valueOf(user.getIDNumber()))
                .oneTimePin(oneTimePin)
                .status(OTPStatus.NOT_ACTIONED)
                .build();

        PrimaryContactUtil primaryContactUtil = PrimaryContactUtil.retrievePrimaryContact(user.getContact());

        TokenAgent agent = tokenAgent.retrieveTokenAgent(primaryContactUtil.getPreferredContact());
        primaryContactUtil.setSubject(String.format(OTP_SUBJECT, primaryContactUtil.getPreferredContact().name()));
        primaryContactUtil.setMessageContent(String.format(OTP_MSG, user.getFirstname(), user.getLastname(),
                primaryContactUtil.getPreferredContact().name().toLowerCase(), oneTimePin));
        agent.sendToken(primaryContactUtil);
        oneTimePinRepository.save(oneTimePinEntity);
    }
    private String encryptPassword(String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    private String generateOTP(){
        return UUID.randomUUID().toString().substring(0,6);
    }

}
