package com.ssafy.trip.domain.schedule.entity;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Schedule {
    private Long scheduleId;
    private String name;
    private Long userId;
    private int cityCode;
    private String password;
    private String thumbnailImage;
    private String publicKey;
    private LocalDate startDate;
    private LocalDate endDate;
    private int day;
    private boolean isFinished;
    private boolean isMulti;
    private boolean isPrivate;
}
