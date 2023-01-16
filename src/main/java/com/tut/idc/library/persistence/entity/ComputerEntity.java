package com.tut.idc.library.persistence.entity;

import com.tut.idc.library.model.enums.ComputerStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@Entity
@Table(name = "computer")
@AllArgsConstructor
@NoArgsConstructor
public class ComputerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private ComputerLabEntity computerLab;
    private String computerName;
    @Enumerated(EnumType.STRING)
    private ComputerStatus status;
}
