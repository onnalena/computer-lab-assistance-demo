package com.tut.idc.library.model;

import com.tut.idc.library.model.enums.ComputerLabStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComputerLabDTO {
    private String computerLabName;
    private String buildingName;
    private String description;
    private String openingTime;
    private String closingTime;
    private ComputerLabStatus status;
    private int numberOfComputersAvailable;
    private int numberOfComputersBooked;
    private int numberOfComputers;
}
