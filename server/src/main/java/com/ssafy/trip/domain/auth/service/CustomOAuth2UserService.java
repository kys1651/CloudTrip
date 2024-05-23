package com.ssafy.trip.domain.auth.service;

import com.ssafy.trip.core.exception.CustomException;
import com.ssafy.trip.core.exception.ErrorCode;
import com.ssafy.trip.core.service.EmailService;
import com.ssafy.trip.domain.auth.attribute.OAuth2Attribute;
import com.ssafy.trip.domain.auth.attribute.OAuth2AttributeFactory;
import com.ssafy.trip.domain.auth.entity.ProviderType;
import com.ssafy.trip.domain.auth.entity.UserPrincipal;
import com.ssafy.trip.domain.auth.repository.AuthCacheRepository;
import com.ssafy.trip.domain.user.entity.User;
import com.ssafy.trip.domain.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final AuthCacheRepository authCacheRepository;
    private final EmailService emailService;
    private final UserMapper userMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2UserService userService = new DefaultOAuth2UserService();
        OAuth2User user = userService.loadUser(userRequest);

        ProviderType provider = ProviderType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
        OAuth2Attribute attribute = OAuth2AttributeFactory.parseAttribute(provider, user.getAttributes());
        log.debug("OAUTH2 기반의 로그인 요청 -> provider: {}, user: {}", provider, user);

        AtomicBoolean isNew = new AtomicBoolean(false);

        // * 해당 요청은 OAuth2 기반의 요청이기에 일치하는 사용자가 없다면 새로운 사용자를 생성한다.
        User findUser = userMapper.findByUsername(attribute.getEmail()).orElseGet(() -> {
            if (authCacheRepository.existsConfirmKey(attribute.getEmail())) {
                throw new CustomException(ErrorCode.ALREADY_EMAIL_SEND);
            }
            emailService.sendSignUpEmail(attribute.getEmail(), attribute.getName());
            User createUser = User.createOAuthUser(attribute, provider);
            userMapper.save(createUser);
            isNew.set(true);
            return createUser;
        });

        // * 기존 인증 방식과 새로운 인증 방식이 다르다면 로그를 남긴다.
        if (findUser.getProviderType() != provider) {
            log.debug("기존 인증 방식: {}, 새로운 인증 방식: {}", findUser.getProviderType(), provider);
        }

        return new UserPrincipal(findUser, isNew.get(), user.getAttributes());
    }
}
