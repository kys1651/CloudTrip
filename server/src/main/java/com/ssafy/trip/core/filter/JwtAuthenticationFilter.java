package com.ssafy.trip.core.filter;

import com.ssafy.trip.core.cache.CacheKey;
import com.ssafy.trip.core.exception.CustomException;
import com.ssafy.trip.core.exception.ErrorCode;
import com.ssafy.trip.domain.auth.entity.TokenValidate;
import com.ssafy.trip.domain.auth.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtTokenService jwtTokenService;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = resolveToken(request);
        try {
            if (StringUtils.hasText(accessToken) && jwtTokenService.validateToken(accessToken)) {
                Authentication authentication = jwtTokenService.parseAuthentication(accessToken);
                validateToken(request, authentication);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("Security Context에 '{}' 인증 정보를 저장했습니다", authentication.getName());
            } else {
                throw new JwtException("유효한 JWT 토큰이 없습니다");
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            request.setAttribute("error-message", e.getMessage());
        }
        doFilter(request, response, filterChain);
    }

    private void validateToken(HttpServletRequest request, Authentication authentication) {
        String key = CacheKey.authenticationKey(authentication.getName());
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        if (!entries.isEmpty()) {
            TokenValidate validate = TokenValidate.fromMap(entries);
            Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("refreshToken")).findFirst().ifPresent(cookie -> {
                if (validate.getRefreshToken().equals(cookie.getValue())) {
                    SecurityContextHolder.clearContext();
                    throw new CustomException(ErrorCode.USER_ALREADY_SIGN_OUT);
                }
            });
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}