package org.wefresh.wefresh_server.common.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements DefaultErrorCode {
    // 400 Bad Request
    INVALID_PROVIDER(HttpStatus.BAD_REQUEST, "유효하지 않은 소셜 플랫폼입니다."),

    // 401 UNAUTHORIZED
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"인증되지 않은 사용자입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED,"액세스 토큰의 형식이 올바르지 않습니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED,"액세스 토큰이 만료되었습니다."),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED,"지원하지 않는 토큰 형식입니다."),
    EMPTY_TOKEN(HttpStatus.UNAUTHORIZED,"토큰이 제공되지 않았습니다."),
    MISMATCH_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED,"리프레시 토큰이 일치하지 않습니다."),
    UNKNOWN_TOKEN(HttpStatus.UNAUTHORIZED, "알 수 없는 토큰입니다."),
    ;

    private HttpStatus httpStatus;
    private String message;
}
