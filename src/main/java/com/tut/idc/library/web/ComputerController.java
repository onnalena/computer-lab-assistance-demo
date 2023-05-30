package com.tut.idc.library.web;

import com.tut.idc.library.model.ComputerDTO;
import com.tut.idc.library.service.ComputerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("api/computer")
@Slf4j
public class ComputerController {

    private ComputerService computerService;

    public ComputerController(ComputerService computerService) {
        this.computerService = computerService;
    }

    @PostMapping("/add-computer")
    public ResponseEntity<ComputerDTO> addComputer(@RequestBody ComputerDTO computer){
        return new ResponseEntity<>(computerService.addComputer(computer), HttpStatus.CREATED);
    }
    @GetMapping("/get-computers/{computerLabName}")
    public ResponseEntity<List<ComputerDTO>> retrieveAllComputersForAGivenLibrary(@PathVariable String computerLabName){
        return  new ResponseEntity<>(computerService.retrieveAllComputerLabComputers(computerLabName), HttpStatus.OK);
    }

    @GetMapping("/get-all-computers")
    public ResponseEntity<List<ComputerDTO>> getAllComputers(){
        return  new ResponseEntity<>(computerService.getAllComputers(), HttpStatus.OK);
    }

    @PutMapping("/update-computer")
    public ResponseEntity<ComputerDTO> updateComputer(@RequestBody ComputerDTO computerDTO){
        return new ResponseEntity<>(this.computerService.updateComputer(computerDTO), HttpStatus.OK);
    }

    @PostMapping("/delete-computer")
    public ResponseEntity<List<ComputerDTO>> deleteComputer(@RequestBody ComputerDTO computerDTO){
        return new ResponseEntity<>(this.computerService.deleteComputer(computerDTO), HttpStatus.OK);
    }

    @PostMapping("/unlink-computer")
    public ResponseEntity<List<ComputerDTO>> unlinkComputer(@RequestBody String computerName){
        return new ResponseEntity<>(this.computerService.unlinkComputer(computerName), HttpStatus.OK);
    }

    @PostMapping("/link-computer")
    public ResponseEntity<List<ComputerDTO>> linkComputer(@RequestBody ComputerDTO computerDTO){
        return new ResponseEntity<>(this.computerService.linkComputer(computerDTO), HttpStatus.OK);
    }

}
