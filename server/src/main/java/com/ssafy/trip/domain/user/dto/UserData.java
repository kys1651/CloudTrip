package com.ssafy.trip.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.trip.domain.user.entity.RoleType;
import com.ssafy.trip.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

public class UserData {
    @Data
    public static class LoginUser {
        @JsonProperty("isLogin")
        private boolean isLogin;
        private Profile profile;

        public static LoginUser authenticated(User user) {
            LoginUser loginUser = new LoginUser();
            loginUser.setLogin(true);
            loginUser.setProfile(Profile.of(user));
            return loginUser;
        }

        public static LoginUser unauthenticated() {
            LoginUser loginUser = new LoginUser();
            loginUser.setLogin(false);
            return loginUser;
        }
    }

    @Data
    @Builder
    public static class Profile {
        private Long id;
        private String username;
        private String email;
        private String nickname;
        private String profileImage;
        private int cityCode;
        private int townCode;
        private RoleType roleType;
        private boolean isEmailVerified;

        public static Profile of(User user) {
            return Profile.builder()
                    .id(user.getUserId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .profileImage(user.getProfileImage())
                    .cityCode(user.getCityCode())
                    .townCode(user.getTownCode())
                    .roleType(user.getRoleType())
                    .isEmailVerified(user.isEmailVerified())
                    .build();
        }
    }

    @Data
    public static class Update {
        private String nickname;
        private int cityCode;
        private int townCode;
        private boolean defaultImage;
    }

    @Data
    public static class Password {
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password;
    }

    @Data
    public static class SimpleReview {
        private Long reviewId;
        private Long userId;
        private String content;
        private LocalDateTime createdAt;
        private List<String> images;
    }

    @Data
    public static class SimpleProfile {
        private Long userId;
        private String email;
        private String nickname;
        private String profileImage;
        private String city;
        private String town;
        @JsonProperty("isFollowing")
        private boolean isFollowing;
    }
}
