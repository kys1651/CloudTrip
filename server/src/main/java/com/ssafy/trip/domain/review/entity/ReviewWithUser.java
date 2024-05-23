package com.ssafy.trip.domain.review.entity;

import com.ssafy.trip.domain.user.entity.SimpleUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class ReviewWithUser extends Review {
    private SimpleUser user;
}
