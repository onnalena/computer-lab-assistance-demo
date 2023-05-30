package com.tut.idc.library.persistence.entity;

import com.tut.idc.library.model.enums.ComputerLabStatus;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "computer_lab")
@AllArgsConstructor
@NoArgsConstructor
public class ComputerLabEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String computerLabName;
    private String buildingName;
    private String description;
    private String openingTime;
    private String closingTime;
    @Enumerated(EnumType.STRING)
    private ComputerLabStatus status;
    @OneToMany(mappedBy = "id")
    private List<ComputerEntity> computers;
    @OneToMany(mappedBy = "id")
    private List<BookingEntity> bookings;

}
