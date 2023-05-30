package com.tut.idc.library.util;

import com.tut.idc.library.model.*;
import com.tut.idc.library.persistence.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper
public interface ComputerLabMapper {

    @Mapping(source = "user.contact", target = "userContacts")
    UserDTO convertUserEntityToDTO(UserEntity user);
    UserEntity convertUserDtoToEntity(UserDTO dto);
    List<UserDTO> convertUserEntityToDTO(List<UserEntity> userEntities);
    List<UserContactEntity> convertUserContactDtoToEntity(List<UserContactDTO> userContactDTOS);
    List<UserContactDTO> convertUserContactEntityToDTO(List<UserContactEntity> userContactEntities);

    List<BookingDTO> convertBookingEntityToDTO(List<BookingEntity> bookingEntities);

    @Mappings({
            @Mapping(source = "bookingEntity.computer.computerLab.computerLabName", target = "computerLabName"),
            @Mapping(source = "bookingEntity.computer.computerName", target = "computerName")
    })
    BookingDTO convertBookingEntityToDTO(BookingEntity bookingEntity);

    List<BookingEntity> convertBookingDtoToEntity(List<BookingDTO> bookingDTOS);
    BookingEntity convertBookingEntityToDTO(BookingDTO bookingDTO);

    ComputerDTO convertComputerEntityToDTO(ComputerEntity computerEntity);
    List<ComputerDTO> convertComputerEntityToDTO(List<ComputerEntity> computerEntities);

    ComputerEntity convertComputerDtoToEntity(ComputerDTO computerDTO);
    List<ComputerEntity> convertComputerDtoToEntity(List<ComputerDTO> computerDTOS);

    ComputerLabEntity convertComputerLabDtoToEntity(ComputerLabDTO computerLabDTO);
    List<ComputerLabEntity> convertComputerLabDtoToEntity(List<ComputerLabDTO> computerLabDTOS);

    ComputerLabDTO convertComputerLabEntityToDTO(ComputerLabEntity computerLabEntity);
    List<ComputerLabDTO> convertComputerLabEntityToDTO(List<ComputerLabEntity> computerLabEntities);

}
