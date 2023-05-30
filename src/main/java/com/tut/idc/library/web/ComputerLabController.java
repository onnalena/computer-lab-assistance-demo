package com.tut.idc.library.web;

import com.tut.idc.library.model.ComputerDTO;
import com.tut.idc.library.model.ComputerLabDTO;
import com.tut.idc.library.persistence.entity.ComputerLabEntity;
import com.tut.idc.library.service.ComputerLabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("api/computer-lab")
public class ComputerLabController {

    private ComputerLabService computerLabService;

    @Autowired
    public ComputerLabController(ComputerLabService computerLabService) {
        this.computerLabService = computerLabService;
    }

    @PostMapping("/add-computer-lab")
    public ResponseEntity<ComputerLabDTO> addComputerLab(@RequestBody ComputerLabDTO computerLab){
        return new ResponseEntity<>(computerLabService.addComputerLab(computerLab), HttpStatus.CREATED);
    }

    @GetMapping("/get-computer-labs")
    public ResponseEntity<List<ComputerLabDTO>> retrieveAllComputerLabs() {
        return new ResponseEntity<>(computerLabService.retrieveAllComputerLabs(), HttpStatus.OK);
    }

    @PutMapping("/update-computer-lab")
    public ResponseEntity<ComputerLabDTO> updateComputerLab(@RequestBody ComputerLabDTO computerLabDTO){
        return new ResponseEntity<>(this.computerLabService.updateComputerLab(computerLabDTO), HttpStatus.OK);
    }

   /* @PostMapping("/delete-computer-lab")
    public ResponseEntity<List<ComputerLabDTO>> deleteComputerLab(@RequestBody ComputerLabDTO computerLabDTO){
        return new ResponseEntity<>(this.computerLabService.deleteComputerLab(computerLabDTO), HttpStatus.OK);
    }*/
}
