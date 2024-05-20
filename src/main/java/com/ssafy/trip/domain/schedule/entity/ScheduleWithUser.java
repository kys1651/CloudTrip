package com.ssafy.trip.domain.schedule.entity;

import com.ssafy.trip.domain.user.entity.SimpleUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ScheduleWithUser extends Schedule {
    private String cityName;
    private SimpleUser user;
    private List<String> usernames = new ArrayList<>();
}
