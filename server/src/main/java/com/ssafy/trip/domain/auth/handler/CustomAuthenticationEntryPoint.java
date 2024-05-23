package com.ssafy.trip.domain.auth.handler;

import com.ssafy.trip.core.exception.CustomException;
import com.ssafy.trip.core.exception.ErrorCode;
import com.ssafy.trip.core.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        CustomException exception = new CustomException(ErrorCode.INVALID_TOKEN);
        if (request.getAttribute("error-message") == null) {
            request.setAttribute("error-message", exception.getMessage());
        }
        ErrorResponse errorResponse = new ErrorResponse(exception, request.getAttribute("error-message").toString());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(errorResponse.toJson());
    }
}
