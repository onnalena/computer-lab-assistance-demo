package com.tut.idc.library.web;

import com.itextpdf.text.DocumentException;
import com.tut.idc.library.model.DownloadResponse;
import com.tut.idc.library.model.ReportCriteria;
import com.tut.idc.library.service.ReportService;
import com.tut.idc.library.model.enums.ReportType;
import com.tut.idc.library.web.exception.ReportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Base64;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/report")
public class ReportController {

    final private ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService){
        this.reportService = reportService;
    }

    @PostMapping("/download")
    public ResponseEntity<byte[]> downloadExcel(@RequestBody ReportCriteria reportCriteria) throws IOException, DocumentException {
        final String filename = reportCriteria.getDownloadFileType().equals("EXCEL") ? reportCriteria.getReportType().toString() + ".xlsx"
                : reportCriteria.getReportType().toString() + ".pdf";
        final ByteArrayResource byteArrayResource = reportService.downloadFile(reportCriteria);

        if (byteArrayResource.exists()) {
            return new ResponseEntity<>(byteArrayResource.getByteArray(), HttpStatus.OK);
        }
        throw new ReportException("Failed to download file.");
    }
}
