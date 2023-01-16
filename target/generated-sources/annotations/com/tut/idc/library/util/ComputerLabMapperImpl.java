package com.tut.idc.library.util;

import com.tut.idc.library.model.UserContactDTO;
import com.tut.idc.library.model.UserDTO;
import com.tut.idc.library.persistence.entity.UserContactEntity;
import com.tut.idc.library.persistence.entity.UserEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-01-15T10:56:48+0200",
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
    public List<UserContactEntity> convertUserContactDtoToEntity(List<UserContactDTO> userContactEntities) {
        if ( userContactEntities == null ) {
            return null;
        }

        List<UserContactEntity> list = new ArrayList<UserContactEntity>( userContactEntities.size() );
        for ( UserContactDTO userContactDTO : userContactEntities ) {
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
}
