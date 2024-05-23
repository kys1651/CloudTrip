package com.ssafy.trip.domain.auth.attribute;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
@Getter
public abstract class OAuth2Attribute {
    private final Map<String, Object> attributes;

    public abstract String getId();

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getImageUrl();
}
