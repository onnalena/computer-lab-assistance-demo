package com.tut.idc.library.model;

import com.tut.idc.library.model.enums.ComputerStatus;
import lombok.Builder;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Builder
public class ComputerDTO {
    private String computerName;
    private ComputerLabDTO computerLab;
    @Enumerated(EnumType.STRING)
    private ComputerStatus status;
}
