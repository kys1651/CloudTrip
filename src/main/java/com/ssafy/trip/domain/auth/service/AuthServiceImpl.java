package com.ssafy.trip.domain.auth.service;

import com.ssafy.trip.core.exception.CustomException;
import com.ssafy.trip.core.exception.ErrorCode;
import com.ssafy.trip.core.service.EmailService;
import com.ssafy.trip.domain.auth.entity.TokenValidate;
import com.ssafy.trip.domain.auth.repository.AuthCacheRepository;
import com.ssafy.trip.domain.user.entity.User;
import com.ssafy.trip.domain.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.ssafy.trip.domain.auth.dto.AuthData.*;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {
    private final EmailService emailService;
    private final UserMapper userMapper;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthCacheRepository authCacheRepository;

    /**
     * 로컬 사용자 로그인
     *
     * @param signIn {@link SignIn}
     * @return {@link JwtToken}
     */
    @Transactional(readOnly = true)
    @Override
    public JwtToken signIn(SignIn signIn) {
        // * 사용자 아이디 기반으로 사용자 정보 조회
        User user = userMapper.findByUsername(signIn.getUsername()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_MATCH)
        );

        // * 비밀번호 검사 로직 수행
        if (!passwordEncoder.matches(signIn.getPassword(), user.getPassword()))
            throw new CustomException(ErrorCode.USER_NOT_MATCH);

        // * 사용자 상태 검사 - 계정 상태 확인
        if (user.isLocked()) throw new CustomException(ErrorCode.USER_LOCKED);

        // * 사용자 상태 검사 - 이메일 인증 여부 확인
        if (!user.isEmailVerified()) throw new CustomException(ErrorCode.USER_EMAIL_NOT_VERIFIED);

        return jwtTokenService.generateTokenByLocal(user);
    }

    /**
     * 신규 회원가입
     *
     * @param signUp {@link SignUp}
     */
    @Override
    public void signUp(SignUp signUp) {
        if (existsByUsername(signUp.getUsername())) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }

        // 비밀번호 암호화 추가
        signUp.setPassword(passwordEncoder.encode(signUp.getPassword()));
        // 이메일 인증 코드 전송
        if (authCacheRepository.existsConfirmKey(signUp.getEmail()))
            throw new CustomException(ErrorCode.ALREADY_EMAIL_SEND);
        emailService.sendSignUpEmail(signUp.getEmail(), signUp.getNickname());
        userMapper.save(User.createLocalUser(signUp));
    }

    /**
     * 로그아웃
     *
     * @param user         {@link User}
     * @param refreshToken 인증 토큰 재발급용 리프레시 토큰
     */
    @Transactional(readOnly = true)
    @Override
    public void signOut(User user, String refreshToken) {
        TokenValidate tokenValidate = TokenValidate.of(user.getUsername(), refreshToken);
        authCacheRepository.saveTokenValidate(user.getUsername(), tokenValidate.toMap());
    }

    /**
     * 이메일 인증 코드 확인
     *
     * @param confirm {@link Confirm}
     * @return 성공 여부
     */
    @Override
    public boolean confirm(Confirm confirm) {
        switch (confirm.getType()) {
            case "sign-up" -> {
                String code = authCacheRepository.confirm(confirm.getEmail());
                if (confirm.getCode().equals(code)) {
                    userMapper.findByEmail(confirm.getEmail()).ifPresent(user -> {
                        user.confirmEmail();
                        userMapper.update(user);
                    });
                    authCacheRepository.deleteConfirm(confirm.getEmail());
                    return true;
                }
            }
            case "find-password" -> {
                String code = authCacheRepository.findPassword(confirm.getEmail());
                if (confirm.getCode().equals(code)) {
                    return true;
                }
            }
            default -> throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }
        return false;
    }

    /**
     * 비밀번호 찾기
     *
     * @param email 이메일
     */
    @Override
    public void findPassword(String email) {
        if (authCacheRepository.existsFindPasswordKey(email))
            throw new CustomException(ErrorCode.ALREADY_EMAIL_SEND);
        emailService.sendFindPasswordEmail(email);
    }

    @Override
    public void resendEmail(String email, String type) {
        userMapper.findByEmail(email).ifPresent(user -> {
            switch (type) {
                case "sign-up" -> {
                    if (authCacheRepository.existsFindPasswordKey(email))
                        throw new CustomException(ErrorCode.ALREADY_EMAIL_SEND);
                    emailService.sendSignUpEmail(email, email);
                }
                case "find-password" -> {
                    if (authCacheRepository.existsFindPasswordKey(email))
                        throw new CustomException(ErrorCode.ALREADY_EMAIL_SEND);
                    emailService.sendFindPasswordEmail(email);
                }
                default -> throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
            }
        });
    }

    /**
     * 비밀번호 재설정
     *
     * @param resetPassword {@link ResetPassword}
     */
    @Override
    public void resetPassword(ResetPassword resetPassword) {
        userMapper.findByEmail(resetPassword.getEmail()).ifPresent(user -> {
            user.resetPassword(passwordEncoder.encode(resetPassword.getPassword()));
            userMapper.update(user);
        });
    }

    /**
     * 토큰 재발급
     *
     * @param refreshToken 재발급을 위한 리프레시 토큰
     * @return newJwtToken {@link JwtToken}
     */
    @Override
    public JwtToken refresh(String refreshToken) throws Exception {
        // * 토큰 유효성 검사
        String username = jwtTokenService.getUsername(refreshToken);
        if (username == null) throw new CustomException(ErrorCode.INVALID_TOKEN);

        Object redisRefreshToken = authCacheRepository.findRefreshKeyInTokenValidate(username);
        if (Objects.equals(redisRefreshToken, refreshToken)) {
            throw new CustomException(ErrorCode.USER_ALREADY_SIGN_OUT);
        }

        return jwtTokenService.generateTokenByRefreshToken(refreshToken);
    }

    private boolean existsByUsername(String username) {
        return userMapper.existsByUsername(username);
    }
}
