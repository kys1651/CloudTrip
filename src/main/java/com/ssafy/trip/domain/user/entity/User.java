package com.ssafy.trip.domain.user.entity;

import com.ssafy.trip.domain.auth.attribute.OAuth2Attribute;
import com.ssafy.trip.domain.auth.dto.AuthData;
import com.ssafy.trip.domain.auth.entity.ProviderType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.StringUtils;

import java.util.*;

@Getter
@ToString
public class User {
    public static final String DEFAULT_IMAGE = "https://trip-media-server.s3.ap-northeast-2.amazonaws.com/28f83eeb-6noProfile.png";
    private Long userId;
    private String username;
    private String email;
    private String password;
    private String nickname;
    private String profileImage;
    private ProviderType providerType;
    private RoleType roleType = RoleType.USER;
    private int cityCode;
    private int townCode;
    private boolean isEmailVerified = false;
    private boolean isLocked;

    /**
     * 소셜 로그인 기반 신규 유저 생성
     *
     * @param attribute {@link OAuth2Attribute}
     * @param provider  {@link ProviderType}
     * @return {@link User}
     */
    public static User createOAuthUser(OAuth2Attribute attribute, ProviderType provider) {
        User user = new User();
        user.username = attribute.getEmail();
        user.password = UUID.randomUUID().toString();
        user.nickname = attribute.getName();
        if(!StringUtils.hasText(user.nickname)){
            user.nickname = createNickName();
        }
        user.profileImage = attribute.getImageUrl();
        if(Objects.isNull(user.profileImage)){
            user.profileImage = DEFAULT_IMAGE;
        }
        user.email = attribute.getEmail();
        user.providerType = provider;
        user.roleType = RoleType.USER;
        user.isLocked = false;
        return user;
    }

    /**
     * 로컬 로그인 기반 신규 유저 생성
     *
     * @param signUp {@link AuthData.SignUp}
     * @return {@link User}
     */
    public static User createLocalUser(AuthData.SignUp signUp) {
        User user = new User();
        user.username = signUp.getUsername();
        user.nickname = signUp.getNickname();
        if(!StringUtils.hasText(user.nickname)){
            user.nickname = createNickName();
        }
        user.email = signUp.getEmail();
        user.password = signUp.getPassword();
        user.profileImage = DEFAULT_IMAGE;
        user.providerType = ProviderType.LOCAL;
        user.roleType = RoleType.USER;
        user.isLocked = false;
        return user;
    }

    public void confirmEmail() {
        this.isEmailVerified = true;
    }

    public void resetPassword(String password) {
        this.password = password;
    }

    public void updateProfile(String nickname, int cityCode, int townCode,String profileImage ){
        this.nickname = nickname;
        this.cityCode = cityCode;
        this.townCode = townCode;
        this.profileImage = profileImage;
    }

    static final List<String> ADJECTIVES = Arrays.asList(
            "행복한", "슬픈", "빠른", "느린", "웃기는", "진지한", "강한", "약한", "똑똑한", "어리석은",
            "용감한", "겁많은", "부지런한", "게으른", "친절한", "무례한", "현명한", "어리석은", "잘생긴", "못생긴"
    );

    static final List<String> VERBS = Arrays.asList(
            "달리는", "점프하는", "먹는", "자고 있는", "노래하는", "춤추는", "웃는", "울고 있는", "생각하는", "말하는",
            "그리는", "읽는", "쓰는", "걷는", "날아다니는", "숨쉬는", "수영하는", "운전하는", "게임하는", "공부하는"
    );

    static final List<String> ANIMALS = Arrays.asList(
            "펭귄", "호랑이", "코끼리", "팬더", "여우", "곰", "토끼", "사자", "늑대", "사슴",
            "다람쥐", "고양이", "강아지", "말", "양", "염소", "돼지", "닭", "오리", "독수리"
    );
    private static final Random RANDOM = new Random();

    private static String createNickName(){
        StringBuilder sb = new StringBuilder();
        sb.append(ADJECTIVES.get(RANDOM.nextInt(ADJECTIVES.size()))).append(' ');
        sb.append(VERBS.get(RANDOM.nextInt(VERBS.size()))).append(' ');
        sb.append(ANIMALS.get(RANDOM.nextInt(ANIMALS.size())));
        return sb.toString();
    }
}
