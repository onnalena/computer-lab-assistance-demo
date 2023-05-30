package com.tut.idc.library.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDTO {
    private String IDNumber;
    private int stars;
    private String comment;
    private String date;

}
