package com.tut.idc.library.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tut.idc.library.model.enums.ReportType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportCriteria {

    private ReportType reportType;
    private String reportDate;
    private String downloadFileType;
}
