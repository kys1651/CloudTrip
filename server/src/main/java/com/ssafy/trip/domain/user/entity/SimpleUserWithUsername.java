package com.ssafy.trip.domain.user.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class SimpleUserWithUsername extends SimpleUser {
    private String username;
}
