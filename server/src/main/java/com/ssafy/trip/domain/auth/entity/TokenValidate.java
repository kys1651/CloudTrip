package com.ssafy.trip.domain.auth.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenValidate {
    private String username;
    private String refreshToken;

    public static TokenValidate fromMap(Map<Object, Object> map) {
        return TokenValidate.of(map.get("userId").toString(), map.get("refreshToken").toString());
    }

    public Map<String, String> toMap() {
        return Map.of("userId", username, "refreshToken", refreshToken);
    }
}
