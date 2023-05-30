package com.tut.idc.library.util;

import com.tut.idc.library.model.BookingDTO;
import com.tut.idc.library.model.ComputerDTO;
import com.tut.idc.library.model.ComputerLabDTO;
import com.tut.idc.library.model.UserContactDTO;
import com.tut.idc.library.model.UserDTO;
import com.tut.idc.library.persistence.entity.BookingEntity;
import com.tut.idc.library.persistence.entity.ComputerEntity;
import com.tut.idc.library.persistence.entity.ComputerLabEntity;
import com.tut.idc.library.persistence.entity.UserContactEntity;
import com.tut.idc.library.persistence.entity.UserEntity;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-30T07:27:18+0200",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.4.1 (Amazon.com Inc.)"
)
public class ComputerLabMapperImpl implements ComputerLabMapper {

    @Override
    public UserDTO convertUserEntityToDTO(UserEntity user) {
        if ( user == null ) {
            return null;
        }

        UserDTO.UserDTOBuilder userDTO = UserDTO.builder();

        userDTO.userContacts( convertUserContactEntityToDTO( user.getContact() ) );
        userDTO.IDNumber( user.getIDNumber() );
        userDTO.firstname( user.getFirstname() );
        userDTO.lastname( user.getLastname() );
        userDTO.status( user.getStatus() );
        userDTO.password( user.getPassword() );
        userDTO.userType( user.getUserType() );

        return userDTO.build();
    }

    @Override
    public UserEntity convertUserDtoToEntity(UserDTO dto) {
        if ( dto == null ) {
            return null;
        }

        UserEntity.UserEntityBuilder userEntity = UserEntity.builder();

        userEntity.IDNumber( dto.getIDNumber() );
        userEntity.firstname( dto.getFirstname() );
        userEntity.lastname( dto.getLastname() );
        userEntity.password( dto.getPassword() );
        userEntity.userType( dto.getUserType() );
        userEntity.status( dto.getStatus() );

        return userEntity.build();
    }

    @Override
    public List<UserDTO> convertUserEntityToDTO(List<UserEntity> userEntities) {
        if ( userEntities == null ) {
            return null;
        }

        List<UserDTO> list = new ArrayList<UserDTO>( userEntities.size() );
        for ( UserEntity userEntity : userEntities ) {
            list.add( convertUserEntityToDTO( userEntity ) );
        }

        return list;
    }

    @Override
    public List<UserContactEntity> convertUserContactDtoToEntity(List<UserContactDTO> userContactDTOS) {
        if ( userContactDTOS == null ) {
            return null;
        }

        List<UserContactEntity> list = new ArrayList<UserContactEntity>( userContactDTOS.size() );
        for ( UserContactDTO userContactDTO : userContactDTOS ) {
            list.add( userContactDTOToUserContactEntity( userContactDTO ) );
        }

        return list;
    }

    @Override
    public List<UserContactDTO> convertUserContactEntityToDTO(List<UserContactEntity> userContactEntities) {
        if ( userContactEntities == null ) {
            return null;
        }

        List<UserContactDTO> list = new ArrayList<UserContactDTO>( userContactEntities.size() );
        for ( UserContactEntity userContactEntity : userContactEntities ) {
            list.add( userContactEntityToUserContactDTO( userContactEntity ) );
        }

        return list;
    }

    @Override
    public List<BookingDTO> convertBookingEntityToDTO(List<BookingEntity> bookingEntities) {
        if ( bookingEntities == null ) {
            return null;
        }

        List<BookingDTO> list = new ArrayList<BookingDTO>( bookingEntities.size() );
        for ( BookingEntity bookingEntity : bookingEntities ) {
            list.add( convertBookingEntityToDTO( bookingEntity ) );
        }

        return list;
    }

    @Override
    public BookingDTO convertBookingEntityToDTO(BookingEntity bookingEntity) {
        if ( bookingEntity == null ) {
            return null;
        }

        BookingDTO.BookingDTOBuilder bookingDTO = BookingDTO.builder();

        bookingDTO.computerLabName( bookingEntityComputerComputerLabComputerLabName( bookingEntity ) );
        bookingDTO.computerName( bookingEntityComputerComputerName( bookingEntity ) );
        bookingDTO.IDNumber( bookingEntity.getIDNumber() );
        if ( bookingEntity.getDateTime() != null ) {
            bookingDTO.dateTime( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( bookingEntity.getDateTime() ) );
        }
        bookingDTO.contactPreference( bookingEntity.getContactPreference() );
        bookingDTO.status( bookingEntity.getStatus() );

        return bookingDTO.build();
    }

    @Override
    public List<BookingEntity> convertBookingDtoToEntity(List<BookingDTO> bookingDTOS) {
        if ( bookingDTOS == null ) {
            return null;
        }

        List<BookingEntity> list = new ArrayList<BookingEntity>( bookingDTOS.size() );
        for ( BookingDTO bookingDTO : bookingDTOS ) {
            list.add( convertBookingEntityToDTO( bookingDTO ) );
        }

        return list;
    }

    @Override
    public BookingEntity convertBookingEntityToDTO(BookingDTO bookingDTO) {
        if ( bookingDTO == null ) {
            return null;
        }

        BookingEntity.BookingEntityBuilder bookingEntity = BookingEntity.builder();

        bookingEntity.IDNumber( bookingDTO.getIDNumber() );
        if ( bookingDTO.getDateTime() != null ) {
            bookingEntity.dateTime( LocalDateTime.parse( bookingDTO.getDateTime() ) );
        }
        bookingEntity.contactPreference( bookingDTO.getContactPreference() );
        bookingEntity.status( bookingDTO.getStatus() );

        return bookingEntity.build();
    }

    @Override
    public ComputerDTO convertComputerEntityToDTO(ComputerEntity computerEntity) {
        if ( computerEntity == null ) {
            return null;
        }

        ComputerDTO.ComputerDTOBuilder computerDTO = ComputerDTO.builder();

        computerDTO.computerName( computerEntity.getComputerName() );
        computerDTO.brandName( computerEntity.getBrandName() );
        computerDTO.serialNumber( computerEntity.getSerialNumber() );
        computerDTO.computerLab( convertComputerLabEntityToDTO( computerEntity.getComputerLab() ) );

        return computerDTO.build();
    }

    @Override
    public List<ComputerDTO> convertComputerEntityToDTO(List<ComputerEntity> computerEntities) {
        if ( computerEntities == null ) {
            return null;
        }

        List<ComputerDTO> list = new ArrayList<ComputerDTO>( computerEntities.size() );
        for ( ComputerEntity computerEntity : computerEntities ) {
            list.add( convertComputerEntityToDTO( computerEntity ) );
        }

        return list;
    }

    @Override
    public ComputerEntity convertComputerDtoToEntity(ComputerDTO computerDTO) {
        if ( computerDTO == null ) {
            return null;
        }

        ComputerEntity.ComputerEntityBuilder computerEntity = ComputerEntity.builder();

        computerEntity.computerLab( convertComputerLabDtoToEntity( computerDTO.getComputerLab() ) );
        computerEntity.computerName( computerDTO.getComputerName() );
        computerEntity.brandName( computerDTO.getBrandName() );
        computerEntity.serialNumber( computerDTO.getSerialNumber() );

        return computerEntity.build();
    }

    @Override
    public List<ComputerEntity> convertComputerDtoToEntity(List<ComputerDTO> computerDTOS) {
        if ( computerDTOS == null ) {
            return null;
        }

        List<ComputerEntity> list = new ArrayList<ComputerEntity>( computerDTOS.size() );
        for ( ComputerDTO computerDTO : computerDTOS ) {
            list.add( convertComputerDtoToEntity( computerDTO ) );
        }

        return list;
    }

    @Override
    public ComputerLabEntity convertComputerLabDtoToEntity(ComputerLabDTO computerLabDTO) {
        if ( computerLabDTO == null ) {
            return null;
        }

        ComputerLabEntity.ComputerLabEntityBuilder computerLabEntity = ComputerLabEntity.builder();

        computerLabEntity.computerLabName( computerLabDTO.getComputerLabName() );
        computerLabEntity.buildingName( computerLabDTO.getBuildingName() );
        computerLabEntity.description( computerLabDTO.getDescription() );
        computerLabEntity.openingTime( computerLabDTO.getOpeningTime() );
        computerLabEntity.closingTime( computerLabDTO.getClosingTime() );
        computerLabEntity.status( computerLabDTO.getStatus() );

        return computerLabEntity.build();
    }

    @Override
    public List<ComputerLabEntity> convertComputerLabDtoToEntity(List<ComputerLabDTO> computerLabDTOS) {
        if ( computerLabDTOS == null ) {
            return null;
        }

        List<ComputerLabEntity> list = new ArrayList<ComputerLabEntity>( computerLabDTOS.size() );
        for ( ComputerLabDTO computerLabDTO : computerLabDTOS ) {
            list.add( convertComputerLabDtoToEntity( computerLabDTO ) );
        }

        return list;
    }

    @Override
    public ComputerLabDTO convertComputerLabEntityToDTO(ComputerLabEntity computerLabEntity) {
        if ( computerLabEntity == null ) {
            return null;
        }

        ComputerLabDTO.ComputerLabDTOBuilder computerLabDTO = ComputerLabDTO.builder();

        computerLabDTO.computerLabName( computerLabEntity.getComputerLabName() );
        computerLabDTO.buildingName( computerLabEntity.getBuildingName() );
        computerLabDTO.description( computerLabEntity.getDescription() );
        computerLabDTO.openingTime( computerLabEntity.getOpeningTime() );
        computerLabDTO.closingTime( computerLabEntity.getClosingTime() );
        computerLabDTO.status( computerLabEntity.getStatus() );

        return computerLabDTO.build();
    }

    @Override
    public List<ComputerLabDTO> convertComputerLabEntityToDTO(List<ComputerLabEntity> computerLabEntities) {
        if ( computerLabEntities == null ) {
            return null;
        }

        List<ComputerLabDTO> list = new ArrayList<ComputerLabDTO>( computerLabEntities.size() );
        for ( ComputerLabEntity computerLabEntity : computerLabEntities ) {
            list.add( convertComputerLabEntityToDTO( computerLabEntity ) );
        }

        return list;
    }

    protected UserContactEntity userContactDTOToUserContactEntity(UserContactDTO userContactDTO) {
        if ( userContactDTO == null ) {
            return null;
        }

        UserContactEntity.UserContactEntityBuilder userContactEntity = UserContactEntity.builder();

        userContactEntity.contact( userContactDTO.getContact() );
        userContactEntity.contactPreference( userContactDTO.getContactPreference() );
        userContactEntity.status( userContactDTO.getStatus() );

        return userContactEntity.build();
    }

    protected UserContactDTO userContactEntityToUserContactDTO(UserContactEntity userContactEntity) {
        if ( userContactEntity == null ) {
            return null;
        }

        UserContactDTO.UserContactDTOBuilder userContactDTO = UserContactDTO.builder();

        userContactDTO.contact( userContactEntity.getContact() );
        userContactDTO.contactPreference( userContactEntity.getContactPreference() );
        userContactDTO.status( userContactEntity.getStatus() );

        return userContactDTO.build();
    }

    private String bookingEntityComputerComputerLabComputerLabName(BookingEntity bookingEntity) {
        if ( bookingEntity == null ) {
            return null;
        }
        ComputerEntity computer = bookingEntity.getComputer();
        if ( computer == null ) {
            return null;
        }
        ComputerLabEntity computerLab = computer.getComputerLab();
        if ( computerLab == null ) {
            return null;
        }
        String computerLabName = computerLab.getComputerLabName();
        if ( computerLabName == null ) {
            return null;
        }
        return computerLabName;
    }

    private String bookingEntityComputerComputerName(BookingEntity bookingEntity) {
        if ( bookingEntity == null ) {
            return null;
        }
        ComputerEntity computer = bookingEntity.getComputer();
        if ( computer == null ) {
            return null;
        }
        String computerName = computer.getComputerName();
        if ( computerName == null ) {
            return null;
        }
        return computerName;
    }
}
