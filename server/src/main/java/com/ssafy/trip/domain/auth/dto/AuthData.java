package com.ssafy.trip.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

public class AuthData {
    @AllArgsConstructor
    @Getter
    public static class JwtToken {
        private String accessToken;
        private String refreshToken;
    }

    @Data
    public static class SignIn {
        @NotBlank(message = "이메일을 입력해주세요.")
        private String username;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password;
    }

    @Data
    @AllArgsConstructor(staticName = "of")
    public static class SignUp {
        @Email(message = "이메일 형식이 아닙니다.")
        @NotBlank(message = "이메일을 입력해주세요.")
        private String username;

        private String nickname;

        private String email;

        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password;
    }

    @Data
    public static class FindPassword {
        @Email(message = "이메일 형식이 아닙니다.")
        @NotBlank(message = "이메일을 입력해주세요.")
        private String email;
    }

    @Data
    public static class ResetPassword {
        @Email(message = "이메일 형식이 아닙니다.")
        @NotBlank(message = "이메일을 입력해주세요.")
        private String email;

        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password;
    }

    @Data
    public static class Confirm {
        @NotBlank(message = "타입을 입력해주세요.")
        private String type;

        @NotBlank(message = "인증 코드를 입력해주세요.")
        private String code;

        @NotBlank(message = "이메일을 입력해주세요.")
        private String email;
    }

    @Data
    public static class ResendEmail {
        @Email(message = "이메일 형식이 아닙니다.")
        @NotBlank(message = "이메일을 입력해주세요.")
        private String email;

        @NotNull(message = "타입을 입력해주세요.")
        private String type;
    }
}
