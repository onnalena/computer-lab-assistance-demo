package com.tut.idc.library.model.report;

import com.tut.idc.library.model.enums.ComputerStatus;
import com.tut.idc.library.util.annotation.ColumnName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComputerReport {
    @ColumnName(name = "Computer Name")
    private String computerName;
    @ColumnName(name = "Brand Name")
    private String brandName;
    @ColumnName(name = "Serial Number")
    private String serialNumber;
    @ColumnName(name = "Computer Lab")
    private String computerLab;
}
