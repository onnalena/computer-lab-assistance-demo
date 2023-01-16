package com.tut.idc.library.persistence;

import com.tut.idc.library.model.enums.OTPStatus;
import com.tut.idc.library.persistence.entity.OTPEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OTPRepository extends CrudRepository<OTPEntity, String> {
    OTPEntity findByIDNumber(String studentNumber);
    OTPEntity findByStatus(OTPStatus otpStatus);
}
