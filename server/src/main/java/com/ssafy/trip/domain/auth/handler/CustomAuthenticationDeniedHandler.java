package com.ssafy.trip.domain.auth.handler;

import com.ssafy.trip.core.exception.CustomException;
import com.ssafy.trip.core.exception.ErrorCode;
import com.ssafy.trip.core.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        CustomException exception = new CustomException(ErrorCode.ACCESS_DENIED);
        ErrorResponse errorResponse = new ErrorResponse(exception, request.getAttribute("error-message").toString());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(errorResponse.toJson());
    }
}
