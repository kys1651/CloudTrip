package com.ssafy.trip.domain.auth.attribute.impl;

import com.ssafy.trip.domain.auth.attribute.OAuth2Attribute;
import org.springframework.util.StringUtils;

import java.util.Map;

public class GithubAttribute extends OAuth2Attribute {
    public GithubAttribute(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return super.getAttributes().get("id").toString();
    }

    @Override
    public String getName() {
        return super.getAttributes().get("name").toString();
    }

    @Override
    public String getEmail() {
        String email = super.getAttributes().get("email").toString();
        if (!StringUtils.hasText(email)) {
            return this.getName() + "@gihub.com";
        }
        return email;
    }

    @Override
    public String getImageUrl() {
        return super.getAttributes().get("avatar_url").toString();
    }
}
