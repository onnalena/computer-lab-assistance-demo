package com.tut.idc.library.persistence;

import com.tut.idc.library.persistence.entity.PasswordResetToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, Long> {
    PasswordResetToken findByIdNumber(String idNumber);
}
