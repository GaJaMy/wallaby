package com.wallaby.moamoa.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    ACCESS_TOKEN_IS_EMPTY(HttpStatus.OK,"AU001","인증에 실패 하였습니다."
            ,"not exist access token in token header"),

    REFRESH_TOKEN_IS_EMPTY(HttpStatus.OK, "AU002","인증에 실패 하였습니다."
            ,"not exist refresh token in redis"),

    ACCESS_TOKEN_INVALID(HttpStatus.OK, "AU003", "인증에 실패 하였습니다."
            , "invalid access token"),

    REFRESH_TOKEN_INVALID(HttpStatus.OK, "AU004", "인증에 실패 하였습니다."
            , "invalid refresh token"),

    AUTHORIZATION_VALUE_TOKEN_TYPE_INVALID(HttpStatus.OK, "AU005","인증에 실패 하였습니다."
            , "authorization type is not Bearer"),

    AUTHORIZATION_HEADER_IS_EMPTY(HttpStatus.OK, "AU006","로그인이 필요합니다."
            , "not exist authorization value in token header"),

    ALREADY_SIGN_OUTED_CUSTOMER(HttpStatus.OK, "AU007","이미 로그 아웃된 사용자 입니다."
            ,"already sign outed customer"),

    INCORRECT_AUTH_INFO(HttpStatus.OK,"AU008", "아이디 또는 비밀번호가 틀렸습니다."
            ,"invalid userId or password"),

    ACCESS_DENIED(HttpStatus.OK,"AU09", "접근 권한이 없습니다."
            ,"access denied"),

    UNREGISTERED_ID(HttpStatus.OK,"AU010", "등록되지 않은 아이디 입니다."
            , "Unregistered id"),

    NEED_AUTHENTICATION(HttpStatus.OK, "AU011", "인증되지 않은 사용자 입니다. 로그인을 진행해 주세요."
            , "need authentication"),

    INVALID_ACCESS(HttpStatus.OK, "AU012", "잘못된 접근 입니다. 모든 세션이 종료됩니다."
            , "invalid access terminate all session"),

    RESTRICTED_TOKEN(HttpStatus.OK, "AU013", "접근이 제한된 토큰입니다. 로그인을 진행해 주세요."
            , "restricted token"),

    SIGN_OFF_CUSTOMER(HttpStatus.OK, "AU016", "탈퇴한 회원 입니다."
            , "signoff customer"),

    NOT_MATCHED_PASSWORD(HttpStatus.OK, "AU017", "비밀번호가 일치하지 않습니다."
            , "not matched password"),

    SUCCESS(HttpStatus.OK, "SU000", "ok"
            , "ok");

    private final HttpStatus status;
    private final String code;
    private String msg;
    private final String logMsg;

    ErrorCode(HttpStatus status, String errorCode, String msg, String logMsg) {
        this.status = status;
        this.code = errorCode;
        this.msg = msg;
        this.logMsg = logMsg;
    }

    public void modifyMsg(String msg) {
        this.msg = msg;
    }
}
