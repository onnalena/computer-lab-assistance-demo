package com.tut.idc.library.persistence;

import com.tut.idc.library.model.enums.ComputerStatus;
import com.tut.idc.library.persistence.entity.ComputerEntity;
import com.tut.idc.library.persistence.entity.ComputerLabEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComputerRepository extends CrudRepository<ComputerEntity, Long> {
    List<ComputerEntity> findByComputerLabId(Long computerLabId);
    ComputerEntity findByComputerName(String computerName);
    ComputerEntity findBySerialNumber(String serialNumber);
}
