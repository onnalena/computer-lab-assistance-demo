package com.tut.idc.library.persistence.entity;

import com.tut.idc.library.model.enums.ComputerStatus;
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
    @ManyToOne(cascade = CascadeType.ALL)
    private ComputerEntity computer;
    private String IDNumber;
    private String accessToken;
    @Enumerated(EnumType.STRING)
    private ComputerStatus status;
    private LocalDateTime dateAndTime;
}
