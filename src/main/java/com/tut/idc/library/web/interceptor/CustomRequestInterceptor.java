package com.tut.idc.library.web.interceptor;

import com.tut.idc.library.web.interceptor.model.RequestLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;

@Slf4j
@Configuration
public class CustomRequestInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getRequestURI().toString().startsWith("/error")) {
            return false;
        }
        RequestLog requestLog = RequestLog.builder()
                .url(request.getRequestURI().toString())
                .timeTaken(Instant.now().toEpochMilli())
                .build();
        request.setAttribute("requestLog", requestLog);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        RequestLog requestLog = (RequestLog) request.getAttribute("requestLog");
        requestLog.setCode(response.getStatus());
        requestLog.setStatus(HttpStatus.valueOf(response.getStatus()));
        requestLog.setTimeTaken(Instant.now().toEpochMilli() - requestLog.getTimeTaken());
        log.info("request-check ::: {}", requestLog);
    }

}
