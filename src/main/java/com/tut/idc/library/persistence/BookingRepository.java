package com.tut.idc.library.persistence;

import com.tut.idc.library.persistence.entity.BookingEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends CrudRepository<BookingEntity, String> {
    BookingEntity findByIDNumber(String IDNumber);
    List<BookingEntity> findAllByIDNumber(String IDNumber);
}
