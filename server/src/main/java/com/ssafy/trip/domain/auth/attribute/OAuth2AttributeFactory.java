package com.ssafy.trip.domain.auth.attribute;


import com.ssafy.trip.domain.auth.attribute.impl.GithubAttribute;
import com.ssafy.trip.domain.auth.attribute.impl.GoogleAttribute;
import com.ssafy.trip.domain.auth.attribute.impl.KakaoAttribute;
import com.ssafy.trip.domain.auth.attribute.impl.NaverAttribute;
import com.ssafy.trip.domain.auth.entity.ProviderType;

import java.util.Map;

public class OAuth2AttributeFactory {
    public static OAuth2Attribute parseAttribute(ProviderType provider, Map<String, Object> attributes) {
        return switch (provider) {
            case GOOGLE -> new GoogleAttribute(attributes);
            case GITHUB -> new GithubAttribute(attributes);
            case KAKAO -> new KakaoAttribute(attributes);
            case NAVER -> new NaverAttribute(attributes);
            default -> throw new RuntimeException("지원하지 않는 소셜 로그인입니다.");
        };
    }
}
