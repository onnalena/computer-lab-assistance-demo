package com.tut.idc.library.service;

import com.tut.idc.library.model.ComputerLabDTO;
import com.tut.idc.library.model.enums.ComputerLabStatus;
import com.tut.idc.library.model.enums.ComputerStatus;
import com.tut.idc.library.persistence.BookingRepository;
import com.tut.idc.library.persistence.ComputerRepository;
import com.tut.idc.library.persistence.ComputerLabRepository;
import com.tut.idc.library.persistence.entity.ComputerEntity;
import com.tut.idc.library.persistence.entity.ComputerLabEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ComputerLabService {

    private ComputerLabRepository computerLabRepository;
    private ComputerRepository computerRepository;
    private BookingRepository bookingRepository;
    @Autowired
    public ComputerLabService(ComputerLabRepository computerLabRepository, ComputerRepository computerRepository, BookingRepository bookingRepository) {
        this.computerLabRepository = computerLabRepository;
        this.computerRepository = computerRepository;
        this.bookingRepository = bookingRepository;
    }

    public ComputerLabEntity addComputerLab(ComputerLabDTO computerLab){
        if(computerLabRepository.findByComputerLabName(computerLab.getComputerLabName()) != null){
            throw new RuntimeException("Computer Lab already exists!");
        }
        ComputerLabEntity newComputerLab = ComputerLabEntity.builder()
                .computerLabName(computerLab.getComputerLabName())
                .buildingName(computerLab.getBuildingName())
                .description(computerLab.getDescription())
                .openingTime(computerLab.getOpeningTime())
                .closingTime(computerLab.getClosingTime())
                .status(ComputerLabStatus.CLOSED)
                .build();
        /*log.info("New Computer Lab:::" + newComputerLab.toString());*/
        return computerLabRepository.save(newComputerLab);
    }

    public List<ComputerLabDTO> retrieveAllComputerLabs() {
        List<ComputerLabEntity> computerLabs = (List<ComputerLabEntity>) computerLabRepository.findAll();
        List<ComputerLabDTO> computerLabsDTO = new ArrayList<>();

        for (ComputerLabEntity computerLab: computerLabs) {
            ComputerLabDTO computerLabDTO = ComputerLabDTO.builder()
                    .ComputerLabName(computerLab.getComputerLabName())
                    .buildingName(computerLab.getBuildingName())
                    .description(computerLab.getDescription())
                    .status(computerLab.getStatus())
                    .numberOfComputersAvailable(numberOfAvailableComputers(computerLab))
                    .build();

            computerLabsDTO.add(computerLabDTO);
        }
        return computerLabsDTO;
    }

   private Integer numberOfAvailableComputers(ComputerLabEntity computerLab) {
        List<ComputerEntity> computers = computerRepository.findByComputerLabId(computerLab.getId());
        Integer numberOfComputersAvailable = 0;

        for (ComputerEntity computer: computers) {
            if(computer.getStatus().equals(ComputerStatus.AVAILABLE)){
                numberOfComputersAvailable+=1;
            }
        }
        return numberOfComputersAvailable;
    }
}
