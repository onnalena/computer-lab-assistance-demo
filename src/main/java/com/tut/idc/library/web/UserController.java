package com.tut.idc.library.web;

import com.tut.idc.library.model.*;
import com.tut.idc.library.persistence.entity.OTPEntity;
import com.tut.idc.library.persistence.entity.PasswordResetToken;
import com.tut.idc.library.service.ReportService;
import com.tut.idc.library.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/user")
@Slf4j
public class UserController {
    private UserService userService;
    private ReportService reportService;
    @Autowired
    public UserController(UserService userService, ReportService reportService) {
        this.userService = userService;
        this.reportService = reportService;
    }
    @PostMapping("/add-user")
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO user) throws IOException {
        return new ResponseEntity<>(userService.addUser(user), HttpStatus.CREATED);
    }
    @PostMapping("/add-admin-user")
    public ResponseEntity<PasswordResetToken> addAdminUser(@RequestBody UserDTO admin){
        return new ResponseEntity<>(userService.addAdminUser(admin), HttpStatus.CREATED);
    }

    @GetMapping("/verify-otp/{IDNumber}/{oneTimePin}")
    public ResponseEntity<OTPEntity> verifyOTP(@PathVariable String IDNumber, @PathVariable String oneTimePin){
        log.info("Verifying OTP...");
        return new ResponseEntity<>(userService.verifyOTP(IDNumber, oneTimePin), HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginDTO loginDTO){
        log.info(loginDTO.getIDNumber());
        return new ResponseEntity<>(userService.verifyLogin(loginDTO), HttpStatus.OK);
    }

    @PutMapping("/update-user")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userContactDetails){
        return new ResponseEntity<>(userService.updateUser(userContactDetails), HttpStatus.OK);
    }
    @PutMapping("/admin-update-user")
    public ResponseEntity<UserDTO> adminUpdateUser(@RequestBody UserDTO userContactDetails){
        return new ResponseEntity<>(userService.adminUpdateUser(userContactDetails), HttpStatus.OK);
    }

    @GetMapping("/get-user/{IDNumber}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String IDNumber){
        return new ResponseEntity<>(this.userService.getUser(IDNumber), HttpStatus.OK);
    }
    @GetMapping("/get-all-users")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        return new ResponseEntity<>(this.userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/forgot-password/{idNumber}")
    public ResponseEntity<PasswordResetToken> forgotPassword(@PathVariable String idNumber){
        return new ResponseEntity<>(this.userService.forgotPassword(idNumber), HttpStatus.CREATED);
    }

    @PutMapping ("/update-password")
    public ResponseEntity<UserDTO> updatePassword(@RequestBody PasswordResetDTO passwordResetDTO){
        return new ResponseEntity<>(userService.updatePassword(passwordResetDTO), HttpStatus.CREATED);
    }
    @PostMapping("/verify-password")
    public ResponseEntity<Boolean> verifyPassword(@RequestBody LoginDTO loginDTO){
        return new ResponseEntity<>(userService.verifyPassword(loginDTO), HttpStatus.OK);
    }

    @PostMapping("/feedback")
    public ResponseEntity<FeedbackDTO> saveUserFeedBack(@RequestBody FeedbackDTO feedback){
        return new ResponseEntity<>(userService.saveUserFeedback(feedback), HttpStatus.OK);
    }

    @GetMapping("/get-feedback")
    public ResponseEntity<List<FeedbackDTO>> getFeedback(){
        return new ResponseEntity<>(userService.getFeedback(), HttpStatus.OK);
    }
    @PostMapping("/deactivate")
    public ResponseEntity<List<UserDTO>> deactivate(@RequestBody String IDNumber){
        return new ResponseEntity<>(userService.deactivateUser(IDNumber), HttpStatus.OK);
    }

    @PostMapping("/reactivate")
    public ResponseEntity<List<UserDTO>> reactivate(@RequestBody String IDNumber){
        return new ResponseEntity<>(userService.reactivateUser(IDNumber), HttpStatus.OK);
    }
}
