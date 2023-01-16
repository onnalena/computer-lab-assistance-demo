package com.tut.idc.library.service;

import com.tut.idc.library.model.ComputerDTO;
import com.tut.idc.library.model.ComputerLabDTO;
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
public class ComputerService {

    private ComputerLabRepository computerLabRepository;
    private ComputerRepository computerRepository;
    private BookingRepository bookingRepository;

    @Autowired
    public ComputerService(ComputerLabRepository computerLabRepository, ComputerRepository computerRepository, BookingRepository bookingRepository) {
        this.computerLabRepository = computerLabRepository;
        this.computerRepository = computerRepository;
        this.bookingRepository = bookingRepository;
    }

    public ComputerEntity addComputer(ComputerDTO computer){
        if(computerRepository.findByComputerName(computer.getComputerName()) != null){
            throw new RuntimeException("Computer name already exists!");
        }

        ComputerLabEntity computerLab = computerLabRepository.findByComputerLabName(computer.getComputerLab().getComputerLabName());

        ComputerEntity newComputer = ComputerEntity.builder()
                .computerName(computer.getComputerName())
                .status(ComputerStatus.AVAILABLE)
                .computerLab(computerLab)
                .build();
        /*log.info("New Computer:::" + newComputer.toString());*/

        return computerRepository.save(newComputer);
    }

    public List<ComputerDTO> retrieveAllComputersForAGivenLibrary(String computerLabName) {
        List<ComputerEntity> computers = computerRepository.findByComputerLabId(computerLabRepository.findByComputerLabName(computerLabName).getId());
        List<ComputerDTO> computerDTOS = new ArrayList<>();
        for (ComputerEntity computer: computers) {
            ComputerDTO computerDTO = ComputerDTO.builder()
                    .computerName(computer.getComputerName())
                    .computerLab(convertComputerLabEntityToDTO(computer.getComputerLab()))
                    .status(computer.getStatus())
                    .build();
            computerDTOS.add(computerDTO);
        }
        return computerDTOS;
    }

    private ComputerLabDTO convertComputerLabEntityToDTO(ComputerLabEntity computerLab) {
        return ComputerLabDTO.builder()
                .ComputerLabName(computerLab.getComputerLabName())
                .buildingName(computerLab.getBuildingName())
                .description(computerLab.getDescription())
                .build();
    }

    /*private ComputerStatus getComputerStatus(ComputerEntity entity) {
        return bookingEntity == null ? ComputerStatus.AVAILIABLE : bookingEntity.getStatus();
    }*/
}
