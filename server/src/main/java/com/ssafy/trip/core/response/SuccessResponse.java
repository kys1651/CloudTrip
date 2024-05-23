package com.ssafy.trip.core.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class SuccessResponse<T> extends BaseResponse<T> {
    private final static SuccessResponse<Void> EMPTY = new SuccessResponse<>();

    public SuccessResponse() {
        super(true, 200, "success");
    }

    public SuccessResponse(T data) {
        super(true, 200, "success");
        super.data = data;
    }


    public static SuccessResponse<Void> empty() {
        return EMPTY;
    }

    public static <T> SuccessResponse<T> of(T data) {
        return new SuccessResponse<>(data);
    }

    @JsonIgnore
    @Override
    public List<CustomError> getErrors() {
        return super.getErrors();
    }
}
