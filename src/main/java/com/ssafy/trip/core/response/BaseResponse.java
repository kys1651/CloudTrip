package com.ssafy.trip.core.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.ToString;
import org.apache.catalina.connector.InputBuffer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
public abstract class BaseResponse<T> {
    @Getter(onMethod_ = @JsonProperty("isSuccess"))
    private final boolean isSuccess;
    private final int code;
    private final String message;
    protected T data;
    protected List<CustomError> errors;

    public BaseResponse(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
        this.data = null;
        this.errors = new ArrayList<>();
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

}
