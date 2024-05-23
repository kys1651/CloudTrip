package com.ssafy.trip.domain.auth.service;

import com.ssafy.trip.core.properties.AuthProperties;
import com.ssafy.trip.domain.auth.dto.AuthData.JwtToken;
import com.ssafy.trip.domain.auth.entity.UserPrincipal;
import com.ssafy.trip.domain.user.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtTokenService {
    private final AuthProperties authProperties;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(authProperties.getSecretKey());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    private Map<String, Object> createHeader() {
        return Map.of("typ", "JWT", "alg", "HS256");
    }

    /**
     * OAuth2 인증을 통해 토큰 생성
     *
     * @param principal {@link UserPrincipal}
     * @return JWT 토큰
     */
    public JwtToken generateTokenByOAuth2(UserPrincipal principal) {
        return generateToken(principal.getUsername(), principal.getAuthorities());
    }

    /**
     * 로컬 인증을 통해 토큰 생성
     *
     * @param user {@link User}
     * @return JWT 토큰
     */
    public JwtToken generateTokenByLocal(User user) {
        Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRoleType().name()));
        return generateToken(user.getUsername(), authorities);
    }

    /**
     * refreshToken을 통해 토큰 생성
     *
     * @param refreshToken 리프레시 토큰
     * @return JWT 토큰
     */
    public JwtToken generateTokenByRefreshToken(String refreshToken) throws Exception {
        boolean isValidated = validateToken(refreshToken);
        if (isValidated) {
            Claims claims = parseClaims(refreshToken);
            String username = claims.getSubject();
            Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(claims.get("authorities").toString()));
            return generateToken(username, authorities);
        }
        throw new Exception("유효하지 않은 토큰입니다.");
    }

    /**
     * token을 통해 사용자 이름 반환
     *
     * @param token JWT 토큰
     * @return 사용자 이름
     */
    public String getUsername(String token) throws Exception {
        Claims claims = parseClaims(token);
        return claims.getSubject();
    }

    /**
     * 토큰 생성
     *
     * @param username    사용자 이름
     * @param authorities 권한
     * @return JWT 토큰
     */
    private JwtToken generateToken(String username, Collection<? extends GrantedAuthority> authorities) {
        String authority = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = System.currentTimeMillis();

        Date accessTokenExpire = new Date(now + authProperties.getTokenExpiry());
        Date refreshTokenExpire = new Date(now + authProperties.getRefreshTokenExpiry());

        String accessToken = Jwts.builder()
                .setHeader(createHeader())
                .setSubject(username)
                .claim("authorities", authority)
                .setIssuedAt(new Date(now))
                .setExpiration(accessTokenExpire)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(username)
                .claim("authorities", authority)
                .setIssuedAt(new Date(now))
                .setExpiration(refreshTokenExpire)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return new JwtToken(accessToken, refreshToken);
    }


    /**
     * accessToken을 파싱하여 Authentication 객체를 반환
     *
     * @param accessToken JWT 토큰
     * @return 인증된 Authentication 객체
     */
    public Authentication parseAuthentication(String accessToken) throws Exception {
        Claims claims = parseClaims(accessToken);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("authorities").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        UserDetails principal = new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    /**
     * 토큰 유효성 검사
     *
     * @param accessToken JWT 토큰
     * @return 유효한 토큰이면 true, 그렇지 않으면 false
     */
    public boolean validateToken(String accessToken) throws Exception {
        return parseClaims(accessToken) != null;
    }

    /**
     * accessToken 파싱
     *
     * @param accessToken JWT 토큰
     * @return 토큰의 클레임
     */
    private Claims parseClaims(String accessToken) throws Exception {
        String message;
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            message = "유효기간이 만료된 토큰입니다.";
        } catch (MalformedJwtException e) {
            message = "잘못된 형식의 토큰입니다.";
        } catch (IllegalArgumentException e) {
            message = "잘못된 인자입니다.";
        } catch (Exception e) {
            message = "토큰 파싱 중 에러가 발생했습니다.";
        }
        throw new Exception(message);
    }
}
