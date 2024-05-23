package com.ssafy.trip.domain.auth.controller;

import com.ssafy.trip.core.annotation.CurrentUser;
import com.ssafy.trip.core.exception.CustomException;
import com.ssafy.trip.core.exception.ErrorCode;
import com.ssafy.trip.core.properties.AuthProperties;
import com.ssafy.trip.core.response.SuccessResponse;
import com.ssafy.trip.domain.auth.dto.AuthData;
import com.ssafy.trip.domain.auth.dto.AuthData.*;
import com.ssafy.trip.domain.auth.service.AuthService;
import com.ssafy.trip.domain.user.entity.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@Slf4j
@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final AuthProperties authProperties;

    /**
     * 로그인
     *
     * @param signIn   {@link SignIn}
     * @param errors   {@link Errors}
     * @param response {@link HttpServletResponse}
     * @return {@link JwtToken}
     */
    @PostMapping("/sign-in")
    @ResponseStatus(value = HttpStatus.OK)
    public SuccessResponse<JwtToken> signIn(
            @RequestBody @Validated SignIn signIn,
            Errors errors,
            HttpServletResponse response
    ) {
        if (errors.hasErrors()) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, errors);
        }

        JwtToken token = authService.signIn(signIn);
        response.addCookie(createCookie(token));
        return SuccessResponse.of(token);
    }

    /**
     * 신규 회원가입
     *
     * @param signUp {@link SignUp}
     * @param errors {@link Errors}
     * @return 성공 응답
     */
    @PostMapping("/sign-up")
    @ResponseStatus(value = HttpStatus.CREATED)
    public SuccessResponse<Void> signUp(
            @RequestBody @Validated SignUp signUp,
            Errors errors
    ) {
        if (errors.hasErrors()) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, errors);
        }

        authService.signUp(signUp);
        return SuccessResponse.empty();
    }

    /**
     * 로그아웃
     *
     * @param user     {@link User}
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     */
    @DeleteMapping("/sign-out")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public SuccessResponse<Void> signOut(
            @CurrentUser User user,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Cookie cookie = parseRefreshCookie(request);
        authService.signOut(user, cookie.getValue());
        removeCookie(response, "refreshToken");
        return SuccessResponse.empty();
    }

    /**
     * 비밀번호 찾기
     *
     * @param findPassword {@link FindPassword}
     * @param errors       {@link Errors}
     * @return 성공 응답
     */
    @PostMapping("/find-password")
    @ResponseStatus(value = HttpStatus.OK)
    public SuccessResponse<Void> findPassword(
            @RequestBody @Validated FindPassword findPassword,
            Errors errors
    ) {
        if (errors.hasErrors()) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, errors);
        }

        authService.findPassword(findPassword.getEmail());
        return SuccessResponse.empty();
    }

    /**
     * 이메일 인증 코드 확인
     * TODO: POST 방식으로 변경 후 프론트와 연동 해야함.
     *
     * @param confirm {@link Confirm}
     * @param errors  {@link Errors}
     * @return 성공 응답
     */
    @PostMapping("/confirm")
    @ResponseStatus(value = HttpStatus.OK)
    public SuccessResponse<Void> confirm(
            @RequestBody @Validated AuthData.Confirm confirm,
            Errors errors
    ) {
        if (errors.hasErrors()) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, errors);
        }

        if (!authService.confirm(confirm)) {
            throw new IllegalArgumentException("인증 코드가 일치하지 않습니다.");
        }

        return SuccessResponse.empty();
    }

    @PostMapping("/resend-email")
    @ResponseStatus(value = HttpStatus.OK)
    public SuccessResponse<Void> resendEmail(
            @RequestBody @Validated ResendEmail resendEmail,
            Errors errors
    ) {
        if (errors.hasErrors()) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, errors);
        }

        authService.resendEmail(resendEmail.getEmail(), resendEmail.getType());
        return SuccessResponse.empty();
    }

    /**
     * 비밀번호 재설정
     *
     * @param resetPassword {@link ResetPassword}
     * @param errors        {@link Errors}
     * @return 성공 응답
     */
    @PostMapping("/reset-password")
    @ResponseStatus(value = HttpStatus.OK)
    public SuccessResponse<Void> resetPassword(
            @RequestBody @Validated ResetPassword resetPassword,
            Errors errors
    ) {
        if (errors.hasErrors()) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, errors);
        }

        authService.resetPassword(resetPassword);
        return SuccessResponse.empty();
    }

    /**
     * 토큰 재발급
     *
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @return 새로운 토큰
     */
    @PostMapping("/refresh")
    @ResponseStatus(value = HttpStatus.OK)
    public SuccessResponse<JwtToken> refresh(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Cookie cookie = parseRefreshCookie(request);
        try {
            JwtToken newToken = authService.refresh(cookie.getValue());
            response.addCookie(createCookie(newToken));
            return SuccessResponse.of(newToken);
        } catch (CustomException exception) {
            removeCookie(response, "refreshToken");
            throw exception;
        } catch (Exception e) {
            removeCookie(response, "refreshToken");
            throw new CustomException(ErrorCode.INVALID_TOKEN, e.getMessage());
        }
    }


    private Cookie parseRefreshCookie(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("refreshToken"))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_TOKEN, "refresh Token이 존재하지 않습니다."));
    }

    private Cookie createCookie(JwtToken token) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", token.getRefreshToken());
        refreshTokenCookie.setHttpOnly(false);
        refreshTokenCookie.setMaxAge((int) authProperties.getRefreshTokenExpiry() / 1000);
        refreshTokenCookie.setPath("/");
        return refreshTokenCookie;
    }

    private void removeCookie(HttpServletResponse response, String key) {
        Cookie cookie = new Cookie(key, null);
        cookie.setHttpOnly(false);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
