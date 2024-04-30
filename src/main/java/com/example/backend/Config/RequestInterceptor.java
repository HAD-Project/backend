package com.example.backend.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestInterceptor implements HandlerInterceptor {

    @Value("${hmis_id}")
    private String hmisId;

    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) {
        boolean allow = true;

        String hiuHeader = request.getHeader("x-hiu-id");
        String hipHeader = request.getHeader("x-hip-id");

        if(hipHeader != null && !hipHeader.equals(hmisId)) {
            allow = false;
        }
        if(hiuHeader != null && !hiuHeader.equals(hmisId)) {
            allow = false;
        }
        if(request.getRequestURI().contains("/v3/hip/health-information/request")) {
            allow = true;
        }
        System.out.println("HMIS: " + this.hmisId + " HIP: " + hipHeader + " HIU: " + hiuHeader);
        return allow;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView model){

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception exception){
        
    }

}
