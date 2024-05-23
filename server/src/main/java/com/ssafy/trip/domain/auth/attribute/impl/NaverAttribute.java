package com.ssafy.trip.domain.auth.attribute.impl;

import com.ssafy.trip.domain.auth.attribute.OAuth2Attribute;
import org.springframework.util.StringUtils;

import java.util.Map;
public class NaverAttribute extends OAuth2Attribute {
    private Map<String, Object> properties;

    public NaverAttribute(Map<String, Object> attributes) {
        super(attributes);
        properties = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public String getId() {
        return this.properties.get("id").toString();
    }

    @Override
    public String getName() {
        return properties.get("nickname").toString();
    }

    @Override
    public String getEmail() {
        String email = (String) properties.get("email");
        if (!StringUtils.hasText(email)) {
            return getName() + "@naver.com";
        }
        return email;
    }

    @Override
    public String getImageUrl() {
        return properties.get("profile_image").toString();
    }
}
