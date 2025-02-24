package org.wefresh.wefresh_server.common.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BookmarkErrorCode implements DefaultErrorCode {
    //403 FORBIDDEN
    BOOKMARK_FORBIDDEN(HttpStatus.FORBIDDEN, "해당 북마크에 대한 권한이 없습니다."),
    // 404 Not Found
    BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "북마크를 찾을 수 없습니다."),
    ;

    private HttpStatus httpStatus;
    private String message;
}
