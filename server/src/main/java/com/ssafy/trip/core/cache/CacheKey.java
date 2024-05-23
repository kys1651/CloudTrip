package com.ssafy.trip.core.cache;

public class CacheKey {
    public static String authenticationKey(String username) {
        return "authentication#" + username;
    }

    public static String emailConfirmCodeKey(String email) {
        return "emailConfirmCode#" + email;
    }

    public static String findPasswordCodeKey(String email) {
        return "findPasswordCodeKey#" + email;
    }
}
