package com.ssafy.trip.domain.auth.attribute.impl;

import com.ssafy.trip.domain.auth.attribute.OAuth2Attribute;
import org.springframework.util.StringUtils;

import java.util.Map;

public class GoogleAttribute extends OAuth2Attribute {
    public GoogleAttribute(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return super.getAttributes().get("sub").toString();
    }

    @Override
    public String getName() {
        return super.getAttributes().get("name").toString();
    }

    @Override
    public String getEmail() {
        String email = super.getAttributes().get("email").toString();
        if (!StringUtils.hasText(email)) {
            return this.getName() + "@gmail.com";
        }
        return email;
    }

    @Override
    public String getImageUrl() {
        return super.getAttributes().get("picture").toString();
    }
}
