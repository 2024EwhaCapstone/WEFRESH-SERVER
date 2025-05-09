package org.wefresh.wefresh_server.common.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AwsErrorCode implements DefaultErrorCode {
    // 400 BAD_REQUEST
    INVALID_IMAGE_EXTENSION(HttpStatus.BAD_REQUEST, "이미지 확장자는 jpg, png, webp만 가능합니다."),
    IMAGE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "이미지 사이즈는 5MB를 넘을 수 없습니다."),
    // 404 NOT_FOUND
    NOT_FOUND_IMAGE(HttpStatus.NOT_FOUND,"삭제할 이미지를 찾을 수 없습니다."),
    ;

    private HttpStatus httpStatus;
    private String message;
}

