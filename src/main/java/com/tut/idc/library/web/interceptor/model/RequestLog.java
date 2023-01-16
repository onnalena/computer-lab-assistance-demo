package com.tut.idc.library.web.interceptor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestLog {
    private String url;
    private Long timeTaken;
    private HttpStatus status;
    private int code;
}
