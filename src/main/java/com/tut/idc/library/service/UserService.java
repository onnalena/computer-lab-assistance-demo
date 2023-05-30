package com.tut.idc.library.service;

import com.tut.idc.library.model.*;
import com.tut.idc.library.model.enums.*;
import com.tut.idc.library.persistence.*;
import com.tut.idc.library.persistence.entity.*;
import com.tut.idc.library.util.ComputerLabMapper;
import com.tut.idc.library.util.PrimaryContactUtil;
import com.tut.idc.library.web.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.control.MappingControl;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class UserService {

    private static final String OTP_SUBJECT = "Lab Assistant - Verify Account";
    private static final String TEMP_PIN_MSG = "Hi, %s %s%n%nUse this Temporary Login Pin - %s - to login. Expires: %s.";
    private static final String OTP_MSG = "Hi, %s %s%n%n Your OTP to verify your account - %s. Expires: %s.";
    private UserRepository userRepository;
    private TokenAgent tokenAgent;
    private OTPRepository oneTimePinRepository;
    private UserContactRepository userContactRepository;
    private PasswordResetTokenRepository passwordResetTokenRepository;
    private FeedbackRepository feedbackRepository;
    final private BookingRepository bookingRepository;
    private ComputerLabMapper mapper;

    @Autowired
    public UserService(UserRepository userRepository, TokenAgent tokenAgent, OTPRepository oneTimePinRepository,
                       UserContactRepository userContactRepository, PasswordResetTokenRepository passwordResetTokenRepository, FeedbackRepository feedbackRepository, BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.tokenAgent = tokenAgent;
        this.oneTimePinRepository = oneTimePinRepository;
        this.userContactRepository = userContactRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.feedbackRepository = feedbackRepository;
        this.bookingRepository = bookingRepository;
        this.mapper = Mappers.getMapper(ComputerLabMapper.class);
    }

    public UserDTO addUser(UserDTO user) {

        if (userRepository.findByIDNumber(user.getIDNumber()) != null &&
                !userRepository.findByIDNumber(user.getIDNumber()).getStatus().equals(UserStatus.DEACTIVATED)) {
            throw new EntityAlreadyExistsException("User with ID - " + user.getIDNumber());
        }

        if (userRepository.findByIDNumber(user.getIDNumber()) != null &&
                userRepository.findByIDNumber(user.getIDNumber()).getStatus().equals(UserStatus.DEACTIVATED)) {
            throw new UserDeactivatedException(UserDeactivatedException.DEACTIVATED);
        }

        user.getUserContacts().forEach(x -> {
            log.info("x == {}", x.getContact());
            if (userContactRepository.findByContact(x.getContact()) != null) {
                throw new EntityAlreadyExistsException(String.format("Contact - {%s} -", x.getContact()));
            }
        });

        new IDNumber(user.getIDNumber());
        isCellphoneNumberValid(user.getUserContacts().stream().filter(x -> x.getContactPreference().equals(ContactPreference.SMS)).findFirst().get().getContact());
        String userEmail = user.getUserContacts().stream().filter(x -> x.getContactPreference().equals(ContactPreference.EMAIL)).findFirst().get().getContact();
        user.setPassword(encryptPassword(user.getPassword()));
        user.setStatus(UserStatus.INACTIVE);
        user.setUserType(UserType.USER);

        UserEntity savedUser = userRepository.save(mapper.convertUserDtoToEntity(user));
        List<UserContactEntity> userContactEntities = mapper.convertUserContactDtoToEntity(user.getUserContacts());
        userContactEntities.forEach(x -> x.setUser(savedUser));

        List<UserContactEntity> savedContacts = (List<UserContactEntity>) userContactRepository.saveAll(userContactEntities);
        savedUser.setContact(savedContacts);
        sendOTP(savedUser);

        return user;
    }

    public PasswordResetToken addAdminUser(UserDTO admin) {
        if (userRepository.findByIDNumber(admin.getIDNumber()) != null) {
            throw new EntityAlreadyExistsException("User with ID - " + admin.getIDNumber());
        }

        admin.getUserContacts().forEach(x -> {
            log.info("x == {}", x.getContact());
            if (userContactRepository.findByContact(x.getContact()) != null) {
                throw new EntityAlreadyExistsException(String.format("Contact - {%s} -", x.getContact()));
            }
        });
        admin.setStatus(UserStatus.INACTIVE);

        UserEntity savedAdminUser = userRepository.save(mapper.convertUserDtoToEntity(admin));
        List<UserContactEntity> userContactEntities = mapper.convertUserContactDtoToEntity(admin.getUserContacts());
        userContactEntities.forEach(x -> x.setUser(savedAdminUser));

        List<UserContactEntity> savedAdminContacts = (List<UserContactEntity>) userContactRepository.saveAll(userContactEntities);
        savedAdminUser.setContact(savedAdminContacts);

        return createPasswordResetToken(savedAdminUser.getIDNumber());
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

    public UserDTO adminUpdateUser(UserDTO updatedUserContactDetails) {
        
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
        existingUser.setStatus(UserStatus.INACTIVE);
        userRepository.save(existingUser);

        return updatedUserContactDetails;
    }

    public OTPEntity verifyOTP(String IDNumber, String oneTimePin) {
        OTPEntity oneTimePinEntity = oneTimePinRepository.findByIDNumber(IDNumber);
        UserEntity user = userRepository.findByIDNumber(IDNumber);
        if (!oneTimePinEntity.getOneTimePin().equals(oneTimePin)) {
            throw new TokenException("Invalid OTP!");
        }
        if (oneTimePinEntity.getExpiryDate().isBefore(LocalDate.now())) {
            throw new DateException("OTP has expired!");
        }

        oneTimePinRepository.delete(oneTimePinEntity);
        user.setStatus(UserStatus.ACTIVE);

        userRepository.save(user);
        return oneTimePinEntity;
    }

    public LoginResponse verifyLogin(LoginDTO userLoginDetails) {

        if (userRepository.findByIDNumber(userLoginDetails.getIDNumber()) == null) {
            throw new EntityNotFoundException("User with ID - " + userLoginDetails.getIDNumber(),
                    EntityNotFoundException.NOT_FOUND_MESSAGE);
        }
        if (userRepository.findByIDNumber(userLoginDetails.getIDNumber()).getStatus().equals(UserStatus.DEACTIVATED)) {
            throw new EntityNotFoundException("User with ID - " + userLoginDetails.getIDNumber(),
                    EntityNotFoundException.NOT_FOUND_MESSAGE);
        }
        if(userRepository.findByIDNumber(userLoginDetails.getIDNumber()).getStatus().equals(UserStatus.INACTIVE)){
            log.info("User Status == {}",userRepository.findByIDNumber(userLoginDetails.getIDNumber()).getStatus());
            sendOTP(userRepository.findByIDNumber(userLoginDetails.getIDNumber()));
        }
        UserEntity user = userRepository.findByIDNumber(userLoginDetails.getIDNumber());
        List<UserContactEntity> userContactsEntity = userContactRepository.findByUser_IDNumber(user.getIDNumber());
        List<UserContactDTO> userContactDTOS = mapper.convertUserContactEntityToDTO(userContactsEntity);
        UserDTO userDTO = mapper.convertUserEntityToDTO(user);
        userDTO.setUserContacts(userContactDTOS);

        BCryptPasswordEncoder verifyPassword = new BCryptPasswordEncoder();
        LoginResponse loginResponse;

        if (passwordResetTokenRepository.findByIdNumber(userLoginDetails.getIDNumber()) == null) {
            if (!verifyPassword.matches(userLoginDetails.getPassword(), user.getPassword())) {
                throw new PasswordException("Incorrect password!");
            }
            loginResponse = LoginResponse.builder()
                    .loginType(LoginType.LOGIN)
                    .user(userDTO)
                    .build();
        } else {
            PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByIdNumber(
                    userLoginDetails.getIDNumber());
            if (passwordResetToken.getExpiryDate().isBefore(LocalDate.now())) {
                //Delete passwordResetToken??
                passwordResetTokenRepository.delete(passwordResetToken);
                throw new DateException("Reset token has expired!");
            }
            if (!verifyPassword.matches(userLoginDetails.getPassword(),
                    passwordResetToken.getToken())) {
                throw new PasswordException("Incorrect password!");
            }
            loginResponse = LoginResponse.builder()
                    .loginType(LoginType.PASSWORD_RESET)
                    .user(userDTO)
                    .build();
        }
        if (user.getStatus().equals(UserStatus.INACTIVE)) {
            user.setStatus(UserStatus.ACTIVE);
            userRepository.save(user);
        }

        return loginResponse;
    }

    public boolean verifyPassword(LoginDTO loginDTO) {
        if (loginDTO.getPassword().isEmpty()) {
            throw new PasswordException("Password is invalid.");
        }
        BCryptPasswordEncoder verify = new BCryptPasswordEncoder();
        String savedPassword = userRepository.findByIDNumber(loginDTO.getIDNumber()).getPassword();
        if (!verify.matches(loginDTO.getPassword(), savedPassword)) {
            throw new PasswordException("Incorrect password!");
        }
        return true;
    }

    public List<UserDTO> getAllUsers() {
        List<UserEntity> userEntities = (List<UserEntity>) this.userRepository.findAll();
        return mapper.convertUserEntityToDTO(userEntities);
    }

    public UserDTO getUser(String IDNumber) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(IDNumber);
        return mapper.convertUserEntityToDTO(optionalUserEntity.get());
    }

    public PasswordResetToken forgotPassword(String idNumber) {
        if (userRepository.findByIDNumber(idNumber) == null) {
            throw new EntityNotFoundException("User with ID - " + idNumber, EntityNotFoundException.NOT_FOUND_MESSAGE);
        }
        return createPasswordResetToken(idNumber);
    }

    public UserDTO updatePassword(PasswordResetDTO passwordResetDTO) {
        if (this.passwordResetTokenRepository.findByIdNumber(passwordResetDTO.getIDNumber()) != null) {
            this.passwordResetTokenRepository.delete(this.passwordResetTokenRepository.findByIdNumber(passwordResetDTO.getIDNumber()));
        }
        UserEntity user = new UserEntity();
        if(this.userRepository.findByIDNumber(passwordResetDTO.getIDNumber()) != null){
            user = this.userRepository.findByIDNumber(passwordResetDTO.getIDNumber());
        }

        user.setPassword(encryptPassword(passwordResetDTO.getPassword()));
        if (user.getStatus().equals(UserStatus.INACTIVE)) {
            user.setStatus(UserStatus.ACTIVE);
        }
        return mapper.convertUserEntityToDTO(userRepository.save(user));
    }

    public FeedbackDTO saveUserFeedback(FeedbackDTO feedback) {
        FeedbackEntity userFeedback = FeedbackEntity.builder()
                .IDNumber(feedback.getIDNumber())
                .stars(feedback.getStars())
                .comment(feedback.getComment())
                .date(LocalDateTime.now())
                .build();
        FeedbackEntity savedUserFeedback = feedbackRepository.save(userFeedback);
        String date = savedUserFeedback.getDate().toString();
        feedback.setDate(date.substring(0, date.lastIndexOf(":")));
        return feedback;
    }

    public List<FeedbackDTO> getFeedback() {
        if (feedbackRepository.findAll() == null) {
            throw new EntityNotFoundException("No feedback");
        }
        List<FeedbackEntity> feedbackEntities = (List<FeedbackEntity>) feedbackRepository.findAll();
        List<FeedbackDTO> feedbackDTOs = new ArrayList<>();
        feedbackEntities.forEach(feedback -> {
            String date = feedback.getDate().toString();
            feedbackDTOs.add(FeedbackDTO.builder()
                    .IDNumber(feedback.getIDNumber())
                    .stars(feedback.getStars())
                    .comment(feedback.getComment())
                    .date(date.substring(0, date.lastIndexOf(":")))
                    .build());
        });
        return feedbackDTOs;
    }

    public List<UserDTO> deactivateUser(String IDNumber) {
        final UserEntity user = this.userRepository.findByIDNumber(IDNumber);
        final List<BookingEntity> bookings = bookingRepository.findAllByIDNumber(IDNumber);
        user.setStatus(UserStatus.DEACTIVATED);
        if (!bookings.isEmpty()) {
            bookingRepository.deleteAll(bookings);
        }
        userRepository.save(user);
        return getAllUsers();
    }

    public List<UserDTO> reactivateUser(String IDNumber) {
        final UserEntity user = this.userRepository.findByIDNumber(IDNumber);
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
        createPasswordResetToken(IDNumber);
        return getAllUsers();
    }

    //PRIVATE METHODS
    private boolean isCellphoneNumberValid(String cellPhoneNumber) {
        if (!cellPhoneNumber.isEmpty()) {
            if (cellPhoneNumber.length() > 12) {
                throw new CellPhoneNumberException(CellPhoneNumberException.NOT_VALID);
            } else {
                if (cellPhoneNumber.length() == 12) {
                    if (!cellPhoneNumber.substring(0, 1).equals("+")) {
                        throw new CellPhoneNumberException(CellPhoneNumberException.NOT_VALID);
                    } else {
                        if (!cellPhoneNumber.substring(1, 3).equals("27")) {
                            throw new CellPhoneNumberException(CellPhoneNumberException.NOT_VALID);
                        }
                    }
                } else if (cellPhoneNumber.length() == 11 || cellPhoneNumber.length() < 10) {
                    throw new CellPhoneNumberException(CellPhoneNumberException.NOT_VALID);
                } else if (cellPhoneNumber.length() == 10) {
                    if (!cellPhoneNumber.substring(0, 1).equals("0")) {
                        throw new CellPhoneNumberException(CellPhoneNumberException.NOT_VALID);
                    }
                }
            }
        }
        return true;
    }

    private PasswordResetToken createPasswordResetToken(String idNumber) {
        UserEntity user = userRepository.findByIDNumber(idNumber);
        String tempPin = generateOTP();
        String subject = "Lab Assistant - Temporary Login Pin";
        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .idNumber(idNumber)
                .token(encryptPassword(tempPin))
                .expiryDate(LocalDate.now().plusDays(1))
                .build();

        PrimaryContactUtil primaryContactUtil = PrimaryContactUtil.retrievePrimaryContact(user.getContact());
        TokenAgent agent = tokenAgent.retrieveTokenAgent(primaryContactUtil.getPreferredContact());
        primaryContactUtil.setSubject(subject);
        primaryContactUtil.setMessageContent(String.format(TEMP_PIN_MSG, user.getFirstname(), user.getLastname(),
                tempPin, passwordResetToken.getExpiryDate()));
        try {
            agent.sendToken(primaryContactUtil);
        } catch (TokenException ex) {
            user.setStatus(UserStatus.INACTIVE);
            userRepository.save(user);
            throw ex;
        }
        return passwordResetTokenRepository.save(passwordResetToken);
    }

    private void sendOTP(UserEntity user) {
        String oneTimePin = generateOTP();
        OTPEntity oneTimePinEntity = OTPEntity.builder()
                .IDNumber(String.valueOf(user.getIDNumber()))
                .oneTimePin(oneTimePin)
                .status(OTPStatus.NOT_ACTIONED)
                .expiryDate(LocalDate.now().plusDays(1))
                .build();

        PrimaryContactUtil primaryContactUtil = PrimaryContactUtil.retrievePrimaryContact(user.getContact());

        TokenAgent agent = tokenAgent.retrieveTokenAgent(primaryContactUtil.getPreferredContact());
        primaryContactUtil.setSubject(String.format(OTP_SUBJECT));
        primaryContactUtil.setMessageContent(String.format(OTP_MSG, user.getFirstname(), user.getLastname(),
                oneTimePin, oneTimePinEntity.getExpiryDate()));
        try {
            agent.sendToken(primaryContactUtil);
        } catch (TokenException ex) {
            user.setStatus(UserStatus.INACTIVE);
            userRepository.save(user);
            throw ex;
        }
        oneTimePinRepository.save(oneTimePinEntity);
    }

    private String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    private String generateOTP() {
        String code = String.format("%040d", new BigInteger(UUID.randomUUID().toString().replace("-", ""), 16));
        return code.substring(0, 6);
    }

}
