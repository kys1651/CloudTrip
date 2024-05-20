package com.ssafy.trip.domain.auth.attribute.impl;

import com.ssafy.trip.domain.auth.attribute.OAuth2Attribute;
import org.springframework.util.StringUtils;

import java.util.Map;

public class KakaoAttribute extends OAuth2Attribute {
    private final Map<String, Object> properties;
    private final Map<String, Object> kakaoAccount;

    public KakaoAttribute(Map<String, Object> attributes) {
        super(attributes);
        System.out.println(attributes);
        this.properties = (Map<String, Object>) attributes.get("properties");
        this.kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
    }

    @Override
    public String getId() {
        return super.getAttributes().get("id").toString();
    }

    @Override
    public String getName() {
        return properties.get("nickname").toString();
    }

    @Override
    public String getEmail() {
        String email = (String) kakaoAccount.get("email");
        if (!StringUtils.hasText(email)) {
            return this.getName() + "@kakao.com";
        }
        return email;
    }

    @Override
    public String getImageUrl() {
        return this.properties.get("profile_image").toString();
    }
}
