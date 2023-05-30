package com.tut.idc.library.persistence;

import com.tut.idc.library.model.enums.ContactPreference;
import com.tut.idc.library.persistence.entity.UserContactEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserContactRepository extends CrudRepository<UserContactEntity, Long> {
    List<UserContactEntity> findByUser_IDNumber(String idNumber);
    UserContactEntity findByContact(String contact);
}
