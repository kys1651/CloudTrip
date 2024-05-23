package com.ssafy.trip.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SimpleUser {
    private Long userId;
    private String nickname;
    private String profileImage;
    @JsonProperty("isFollowing")
    private boolean isFollowing;
}
