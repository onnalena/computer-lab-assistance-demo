package com.tut.idc.library.persistence;

import com.tut.idc.library.persistence.entity.ComputerLabEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComputerLabRepository extends CrudRepository<ComputerLabEntity, Long> {
    ComputerLabEntity findByComputerLabName(String computerLabName);
}
