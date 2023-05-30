package com.tut.idc.library.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Data
@Entity
@Table(name = "feedback")
@AllArgsConstructor
@NoArgsConstructor

public class FeedbackEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String IDNumber;
    private int stars;
    private String comment;
    private LocalDateTime date;

}
