package com.ssafy.trip.domain.schedule.entity;

import com.ssafy.trip.domain.user.entity.SimpleUser;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ScheduleWithSearch {
    private Long scheduleId;
    private String name;
    private Long userId;
    private int cityCode;
    private String password;
    private String thumbnailImage;
    private LocalDate startDate;
    private LocalDate endDate;
    private int day;
    private int count;
    private boolean isFinished;
    private boolean isMulti;
    private boolean isPrivate;
    private String cityName;
    private SimpleUser user;
    private List<String> usernames;
}
