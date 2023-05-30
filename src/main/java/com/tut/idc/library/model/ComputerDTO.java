package com.tut.idc.library.model;

import com.tut.idc.library.model.enums.ComputerStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComputerDTO {
    private String computerName;
    private String brandName;
    private String serialNumber;
    private ComputerLabDTO computerLab;
}
