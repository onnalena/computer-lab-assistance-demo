package com.tut.idc.library.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@Data
@Table(name = "pc_name_increment")
@AllArgsConstructor
@NoArgsConstructor
public class PCNameIncrement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String computerName;

    private Long increment;
}
