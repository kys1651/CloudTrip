package com.ssafy.trip.core.annotation;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : @fetchUser.apply(#this)", errorOnInvalidType = true)
public @interface CurrentUser {
}