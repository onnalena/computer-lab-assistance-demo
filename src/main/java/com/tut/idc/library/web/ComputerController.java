package com.tut.idc.library.web;

import com.tut.idc.library.model.ComputerDTO;
import com.tut.idc.library.persistence.entity.ComputerEntity;
import com.tut.idc.library.service.ComputerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("computer")
@Slf4j
public class ComputerController {

    private ComputerService computerService;

    public ComputerController(ComputerService computerService) {
        this.computerService = computerService;
    }

    @PostMapping("/add-computer")
    public ResponseEntity<ComputerEntity> addComputer(@RequestBody ComputerDTO computer){
        log.info("Communicating with controller...");
        return new ResponseEntity<>(computerService.addComputer(computer), HttpStatus.CREATED);
    }
    @GetMapping("/get-computers/{computerLabName}")
    public ResponseEntity<List<ComputerDTO>> retrieveAllComputersForAGivenLibrary(@PathVariable String computerLabName){
        return  new ResponseEntity<>(computerService.retrieveAllComputersForAGivenLibrary(computerLabName), HttpStatus.OK);
    }
}
