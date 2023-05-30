package com.tut.idc.library.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DownloadResponse {
    private byte[] fileContent;
    private String filename;
}
