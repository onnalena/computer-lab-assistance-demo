package com.tut.idc.library.persistence.entity;

import com.tut.idc.library.model.enums.OTPStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "one_time_pin")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OTPEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String IDNumber;
    private String oneTimePin;
    @Enumerated(EnumType.STRING)
    private OTPStatus status;
    private LocalDate expiryDate;
}
