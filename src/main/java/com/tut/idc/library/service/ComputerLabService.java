package com.tut.idc.library.service;

import com.tut.idc.library.model.ComputerLabDTO;
import com.tut.idc.library.model.enums.BookingStatus;
import com.tut.idc.library.model.enums.ComputerLabStatus;
import com.tut.idc.library.persistence.BookingRepository;
import com.tut.idc.library.persistence.ComputerRepository;
import com.tut.idc.library.persistence.ComputerLabRepository;
import com.tut.idc.library.persistence.entity.BookingEntity;
import com.tut.idc.library.persistence.entity.ComputerEntity;
import com.tut.idc.library.persistence.entity.ComputerLabEntity;
import com.tut.idc.library.util.ComputerLabMapper;
import com.tut.idc.library.web.exception.EntityAlreadyExistsException;
import com.tut.idc.library.web.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ComputerLabService {

    private ComputerLabRepository computerLabRepository;
    private ComputerRepository computerRepository;
    private BookingRepository bookingRepository;
    private ComputerLabMapper mapper;
    @Autowired
    public ComputerLabService(ComputerLabRepository computerLabRepository, ComputerRepository computerRepository, BookingRepository bookingRepository) {
        this.computerLabRepository = computerLabRepository;
        this.computerRepository = computerRepository;
        this.bookingRepository = bookingRepository;
        this.mapper = Mappers.getMapper(ComputerLabMapper.class);
    }

    public ComputerLabDTO addComputerLab(ComputerLabDTO computerLab){
        if(computerLabRepository.findByComputerLabName(computerLab.getComputerLabName()) != null){
            throw new EntityAlreadyExistsException("Computer Lab - " + computerLab.getComputerLabName());
        }

        ComputerLabEntity newComputerLab = mapper.convertComputerLabDtoToEntity(computerLab);
        newComputerLab.setStatus(ComputerLabStatus.CLOSED);

        log.info("New Computer Lab:::" + newComputerLab);

        return mapper.convertComputerLabEntityToDTO(computerLabRepository.save(newComputerLab));
    }

    public List<ComputerLabDTO> retrieveAllComputerLabs() {
        if(computerLabRepository.findAll() == null){
            throw new EntityNotFoundException("No computer labs ", EntityNotFoundException.AVAILABILITY_MESSAGE);
        }
        List<ComputerLabEntity> computerLabs = (List<ComputerLabEntity>) computerLabRepository.findAll();
        List<ComputerLabDTO> computerLabsDTO = new ArrayList<>();


        for (ComputerLabEntity computerLab: computerLabs) {
            int totalComputers = computerRepository.findByComputerLabId(computerLab.getId()).size();
            int bookedComputers = numberOfBookedComputers(computerLab.getId());
            int availableComputers = totalComputers - bookedComputers;

            ComputerLabDTO computerLabDTO = ComputerLabDTO.builder()
                    .computerLabName(computerLab.getComputerLabName())
                    .buildingName(computerLab.getBuildingName())
                    .description(computerLab.getDescription())
                    .openingTime(computerLab.getOpeningTime())
                    .closingTime(computerLab.getClosingTime())
                    .status(computerLab.getStatus())
                    .numberOfComputers((int) (((float)totalComputers / totalComputers) * 100))
                    .numberOfComputersAvailable((int) (((float)availableComputers / totalComputers) * 100))
                    .numberOfComputersBooked((int) (((float)bookedComputers / totalComputers) * 100))
                    .build();
            computerLabsDTO.add(computerLabDTO);
        }
        return computerLabsDTO;
    }

    public List<ComputerLabDTO> retrieveAllComputerLabsReport() {
        if(computerLabRepository.findAll() == null){
            throw new EntityNotFoundException("No computer labs ", EntityNotFoundException.AVAILABILITY_MESSAGE);
        }
        List<ComputerLabEntity> computerLabs = (List<ComputerLabEntity>) computerLabRepository.findAll();
        List<ComputerLabDTO> computerLabsDTO = new ArrayList<>();


        for (ComputerLabEntity computerLab: computerLabs) {
            int totalComputers = computerRepository.findByComputerLabId(computerLab.getId()).size();
            int bookedComputers = numberOfBookedComputers(computerLab.getId());
            int availableComputers = totalComputers - bookedComputers;

            ComputerLabDTO computerLabDTO = ComputerLabDTO.builder()
                    .computerLabName(computerLab.getComputerLabName())
                    .buildingName(computerLab.getBuildingName())
                    .description(computerLab.getDescription())
                    .openingTime(computerLab.getOpeningTime())
                    .closingTime(computerLab.getClosingTime())
                    .status(computerLab.getStatus())
                    .numberOfComputers(totalComputers)
                    .numberOfComputersAvailable(availableComputers)
                    .numberOfComputersBooked(bookedComputers)
                    .build();
            computerLabsDTO.add(computerLabDTO);
        }
        return computerLabsDTO;
    }

    public ComputerLabDTO updateComputerLab(ComputerLabDTO computerLabDTO){
        ComputerLabEntity computerLabEntity = this.computerLabRepository.findByComputerLabName(computerLabDTO.getComputerLabName());

        computerLabEntity.setBuildingName(computerLabDTO.getBuildingName());
        computerLabEntity.setDescription(computerLabDTO.getDescription());
        computerLabEntity.setOpeningTime(computerLabDTO.getOpeningTime());
        computerLabEntity.setClosingTime(computerLabDTO.getClosingTime());

        return mapper.convertComputerLabEntityToDTO(this.computerLabRepository.save(computerLabEntity));
    }

    private int numberOfBookedComputers(Long computerLabId) {
        List<ComputerEntity> computers = computerRepository.findByComputerLabId(computerLabId);
        int nextHour = LocalDateTime.now().getHour() + 1;
        int numberOfBookedComputers = 0;

        for (ComputerEntity computer: computers) {
            if(!bookingRepository.findByComputerId(computer.getId()).isEmpty()){
                List<BookingEntity> bookedComputers = bookingRepository.findByComputerId(computer.getId())
                        .stream().filter(x -> x.getStatus().equals(BookingStatus.UPCOMING)).collect(Collectors.toList());
                for (BookingEntity booking : bookedComputers) {
                    if(booking.getDateTime().toLocalDate().equals(LocalDate.now())){
                        int bookingHour =  booking.getDateTime().getHour();
                        if(nextHour == bookingHour){
                            numberOfBookedComputers++;
                        }
                    }
                }
            }
        }
        return numberOfBookedComputers;
    }

}
