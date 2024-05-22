package com.ssafy.trip.domain.auth.handler;

import com.ssafy.trip.core.properties.AuthProperties;
import com.ssafy.trip.domain.auth.dto.AuthData.JwtToken;
import com.ssafy.trip.domain.auth.entity.UserPrincipal;
import com.ssafy.trip.domain.auth.service.JwtTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenService jwtTokenService;
    private final AuthProperties authProperties;

    @Value("${frontend.url}")
    private String frontEnd;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        System.out.println(principal);
        String redirectUrl;
        if (principal.isEnabled()) {
            JwtToken token = jwtTokenService.generateTokenByOAuth2(principal);
            redirectUrl = UriComponentsBuilder.fromUriString(frontEnd + "/oauth/redirect")
                    .queryParam("access_token", token.getAccessToken())
                    .queryParam("refresh_token", token.getRefreshToken())
                    .toUriString();
            Cookie cookie = createCookie(token.getRefreshToken());
            response.addCookie(cookie);
        } else {
            redirectUrl = UriComponentsBuilder.fromUriString(frontEnd + "/oauth/redirect")
                    .queryParam("isNew", principal.isNew())
                    .queryParam("error", "이메일 인증을 완료해주세요.")
                    .toUriString();
        }

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private Cookie createCookie(String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(false);
        refreshTokenCookie.setMaxAge((int) (authProperties.getRefreshTokenExpiry() / 1000));
        refreshTokenCookie.setPath("/");
        return refreshTokenCookie;
    }
}
