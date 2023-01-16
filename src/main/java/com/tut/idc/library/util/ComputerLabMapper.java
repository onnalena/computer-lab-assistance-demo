package com.tut.idc.library.util;

import com.tut.idc.library.model.UserContactDTO;
import com.tut.idc.library.model.UserDTO;
import com.tut.idc.library.persistence.entity.UserContactEntity;
import com.tut.idc.library.persistence.entity.UserEntity;
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
    List<UserContactEntity> convertUserContactDtoToEntity(List<UserContactDTO> userContactEntities);

    List<UserContactDTO> convertUserContactEntityToDTO(List<UserContactEntity> userContactEntities);



}
