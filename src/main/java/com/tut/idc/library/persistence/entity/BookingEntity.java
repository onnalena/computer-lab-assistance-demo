package com.tut.idc.library.persistence.entity;

import com.tut.idc.library.model.enums.BookingStatus;
import com.tut.idc.library.model.enums.ComputerStatus;
import com.tut.idc.library.model.enums.ContactPreference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@Table(name = "booking")
@AllArgsConstructor
@NoArgsConstructor
public class BookingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String IDNumber;
    private String accessToken;
    private LocalDateTime dateTime;
    @Enumerated(EnumType.STRING)
    private ContactPreference contactPreference;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    @ManyToOne()
    private ComputerEntity computer;
    @ManyToOne()
    private ComputerLabEntity computerLab;
}
