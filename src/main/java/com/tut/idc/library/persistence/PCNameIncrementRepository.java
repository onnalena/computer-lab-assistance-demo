package com.tut.idc.library.persistence;

import com.tut.idc.library.persistence.entity.PCNameIncrement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PCNameIncrementRepository extends CrudRepository<PCNameIncrement, Long> {
}
