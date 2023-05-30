package com.tut.idc.library.model.report;

import com.tut.idc.library.util.annotation.ColumnName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComputerLabReport {
    @ColumnName(name = "Computer Lab")
    private String computerLabName;
    @ColumnName(name = "Building No.")
    private String buildingName;
    @ColumnName(name = "Description")
    private String description;
    @ColumnName(name = "Opening Time")
    private String openingTime;
    @ColumnName(name = "Closing Time")
    private String closingTime;
    @ColumnName(name = "No. of Available Computers")
    private int numberOfComputersAvailable;
    @ColumnName(name = "No. of Booked Computers")
    private int numberOfComputersBooked;
    @ColumnName(name = "Total No. of Computers")
    private int numberOfComputers;
}
