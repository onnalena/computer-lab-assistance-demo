package com.tut.idc.library.model;

import com.tut.idc.library.model.enums.ComputerLabStatus;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ComputerLabDTO {
    private String ComputerLabName;
    private String buildingName;
    private String description;
    private String openingTime;
    private String closingTime;
    private ComputerLabStatus status;
    private Integer numberOfComputersAvailable;
}
