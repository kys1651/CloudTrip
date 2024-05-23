package com.ssafy.trip.core.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class CustomPage<T> {
    private List<T> contents;
    private int nextCursor;
    private boolean hasNext;

    public static <T> CustomPage<T> of(List<T> contents, int nextCursor, boolean hasNext) {
        return CustomPage.<T>builder()
                .contents(contents)
                .hasNext(hasNext)
                .nextCursor(nextCursor)
                .build();
    }
}
