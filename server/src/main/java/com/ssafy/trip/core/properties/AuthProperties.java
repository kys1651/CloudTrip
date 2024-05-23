package com.ssafy.trip.core.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties(prefix = "auth")
@Component
public class AuthProperties {
    private String redirectUrl;
    private String secretKey;
    private long tokenExpiry;
    private long refreshTokenExpiry;
}
