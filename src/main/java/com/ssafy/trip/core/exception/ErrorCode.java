package com.ssafy.trip.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Brandu 서버에서 발생하는 예외 코드 <br/>
 *
 * @author : sunsuking
 * @fileName : ErrorCode
 * @since : 4/16/24
 */
@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    // Common (1000번대 에러 발생)
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, 1000, "올바르지 않은 입력 값 입니다. 다시 한번 확인해주세요."),
    EMAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, 1001, "이메일 전송에 실패했습니다. 다시 시도해주세요."),
    ALREADY_EMAIL_SEND(HttpStatus.INTERNAL_SERVER_ERROR, 1002, "이미 이메일이 전송되었습니다. 잠시 후 다시 시도해주세요."),
    FAIL_TO_CREATE_FILE(HttpStatus.INTERNAL_SERVER_ERROR, 1003, "파일 생성에 실패했습니다."),
    FAIL_TO_DELETE_FILE(HttpStatus.INTERNAL_SERVER_ERROR, 1004, "파일 삭제에 실패했습니다."),
    NOT_SUPPORTED_EXTENTION(HttpStatus.BAD_REQUEST, 1005, "지원하지 않는 파일 확장자입니다."),
    EMPTY_FILE(HttpStatus.BAD_REQUEST, 1006, "빈 파일입니다."),
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, 1007, "해당 항목을 찾을 수 없습니다."),

    // Auth & User (2000번대 에러 발생)
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, 2000, "올바르지 않은 인증 토큰입니다. 다시 한번 확인해주세요."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, 2001, "접근 권한이 없습니다."),
    NOT_SUPPORTED_PROVIDER(HttpStatus.BAD_REQUEST, 2002, "지원하지 않는 소셜 로그인 제공자입니다."),
    USER_NOT_MATCH(HttpStatus.BAD_REQUEST, 2003, "사용자 정보가 일치하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 2004, "사용자를 찾을 수 없습니다."),
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, 2005, "이미 존재하는 사용자입니다."),
    USER_LOCKED(HttpStatus.FORBIDDEN, 2006, "계정이 잠겼습니다. 관리자에게 문의해주세요."),
    USER_EMAIL_NOT_VERIFIED(HttpStatus.FORBIDDEN, 2007, "이메일 인증이 필요합니다."),
    USER_ALREADY_SIGN_OUT(HttpStatus.BAD_REQUEST, 2008, "이미 로그아웃된 사용자입니다."),

    ;
    private final HttpStatus status;
    private final int code;
    private final String message;
}
