package com.tut.idc.library.persistence;

import com.tut.idc.library.persistence.entity.BookingEntity;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.TemporalType;
import java.awt.print.Book;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface BookingRepository extends CrudRepository<BookingEntity, Long> {
    List<BookingEntity> findByDateTime(LocalDateTime dateTime);
    List<BookingEntity> findAllByIDNumber(String IDNumber);
    List<BookingEntity> findByDateTimeAndIDNumber(LocalDateTime dateTime, String IDNumber);
    List<BookingEntity> findByComputerId(Long computerId);
}
