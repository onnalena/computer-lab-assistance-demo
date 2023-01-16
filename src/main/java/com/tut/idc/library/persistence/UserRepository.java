package com.tut.idc.library.persistence;

import com.tut.idc.library.persistence.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, String> {
    UserEntity findByIDNumber(String idNumber);
}
