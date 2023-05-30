package com.tut.idc.library.web;

import com.tut.idc.library.model.*;
import com.tut.idc.library.persistence.entity.ComputerEntity;
import com.tut.idc.library.service.BookingService;
import com.tut.idc.library.web.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/booking")
public class BookingController {

    private BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/book-computer")
    public ResponseEntity<BookingResponse> bookComputer(@RequestBody BookingDTO bookingDTO) {
        return new ResponseEntity<>(bookingService.createBooking(bookingDTO), HttpStatus.CREATED);
    }

    @GetMapping("/get-contacts/{IDNumber}")
    public ResponseEntity<List<UserContactDTO>> getUserContacts(@PathVariable String IDNumber){
        return new ResponseEntity<>(bookingService.getUserContacts(IDNumber), HttpStatus.OK);
    }

    @GetMapping("/get-all-bookings")
    public ResponseEntity<List<BookingDTO>> getAllBookings(){
        return new ResponseEntity<>(this.bookingService.getAllBookings(), HttpStatus.OK);
    }

    @PostMapping("/get-available-time-slots")
    public ResponseEntity<List<String>> getAvailableTimeSlots(@RequestBody BookingDateSelected dateSelected){
        if(bookingService.getAvailableTimeSlots(dateSelected).isEmpty()){
            throw new EntityNotFoundException("No time slots", EntityNotFoundException.AVAILABILITY_MESSAGE);
        }
        return new ResponseEntity<>(bookingService.getAvailableTimeSlots(dateSelected), HttpStatus.OK);
    }


    @GetMapping("/get-user-bookings/{IDNumber}")
    public ResponseEntity<List<BookingDTO>> getUserBookings(@PathVariable String IDNumber){
        return new ResponseEntity<>(this.bookingService.getUserBookings(IDNumber),HttpStatus.OK);
    }

    @GetMapping("/get-available-computer/{computerLabName}/{date}")
    public ResponseEntity<ComputerEntity> getAvailableComputer(@PathVariable String computerLabName, @PathVariable LocalDateTime date){
        return new ResponseEntity<>(this.bookingService.getAvailableComputer(computerLabName, date),HttpStatus.OK);
    }

    @PostMapping("/delete-booking")
    public ResponseEntity<List<BookingDTO>> deleteBooking(@RequestBody BookingDTO bookingDTO){
        return new ResponseEntity<>(this.bookingService.cancelBooking(bookingDTO), HttpStatus.OK);
    }
}
