package com.tut.idc.library.web;
import com.tut.idc.library.model.LoginDTO;
import com.tut.idc.library.model.UserContactDTO;
import com.tut.idc.library.model.UserDTO;
import com.tut.idc.library.model.enums.UserStatus;
import com.tut.idc.library.persistence.entity.OTPEntity;
import com.tut.idc.library.persistence.entity.UserContactEntity;
import com.tut.idc.library.persistence.entity.UserEntity;
import com.tut.idc.library.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/user")
@Slf4j
public class UserController {
    private UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/add-user")
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO student){
        student.setStatus(UserStatus.INACTIVE);
        return new ResponseEntity<>(userService.addUser(student), HttpStatus.CREATED);
    }
    @GetMapping("/verify-otp/{IDNumber}/{oneTimePin}")
    public ResponseEntity<OTPEntity> verifyOTP(@PathVariable String IDNumber, @PathVariable String oneTimePin){
        log.info("Verifying OTP...");
        return new ResponseEntity<>(userService.verifyOTP(IDNumber, oneTimePin), HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<UserDTO> loginUser(@RequestBody LoginDTO loginDTO){
        return new ResponseEntity<>(userService.verifyLogin(loginDTO), HttpStatus.OK);
    }

    @PutMapping("/update-user")
    public ResponseEntity<UserDTO> updateStudent(@RequestBody UserDTO userContactDetails){
        return new ResponseEntity<>(userService.updateUser(userContactDetails), HttpStatus.OK);
    }

    @GetMapping("/get-user/{IDNumber}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String IDNumber){
        return new ResponseEntity<>(this.userService.getUser(IDNumber), HttpStatus.OK);
    }
    @GetMapping("/get-all-users")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        return new ResponseEntity<>(this.userService.getAllUsers(), HttpStatus.OK);
    }
}
