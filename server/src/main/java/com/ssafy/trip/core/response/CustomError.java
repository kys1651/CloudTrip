package com.ssafy.trip.core.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomError {
    private String field;
    private String code;
    private String message;
    private String objectName;
}
