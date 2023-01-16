package com.tut.idc.library.web;

import com.tut.idc.library.model.BookingDTO;
import com.tut.idc.library.model.BookingResponse;
import com.tut.idc.library.model.UserContactDTO;
import com.tut.idc.library.persistence.entity.BookingEntity;
import com.tut.idc.library.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("booking")
public class BookingController {

    private BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingResponse> bookComputer(@RequestBody BookingDTO bookingDTO) {
        return new ResponseEntity<>(bookingService.createBooking(bookingDTO), HttpStatus.CREATED);
    }

    @GetMapping("/get-contacts/{IDNumber}")
    public ResponseEntity<List<UserContactDTO>> getUserContacts(@PathVariable String IDNumber){
        return new ResponseEntity<>(bookingService.getUserContacts(IDNumber), HttpStatus.OK);
    }

    @GetMapping("/get-all-bookings")
    public ResponseEntity<List<BookingEntity>> getAllBookings(){
        return new ResponseEntity<>(this.bookingService.getAllBookings(), HttpStatus.OK);
    }

    @GetMapping("/get-user-bookings/{IDNumber}")
    public ResponseEntity<List<BookingEntity>> getUserBookings(@PathVariable String IDNumber){
        return new ResponseEntity<>(this.bookingService.getAllBookingsForAGivenUser(IDNumber),HttpStatus.OK);
    }
}
