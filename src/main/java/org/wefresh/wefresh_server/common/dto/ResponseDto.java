package org.wefresh.wefresh_server.common.dto;

import org.springframework.http.HttpStatus;
import org.wefresh.wefresh_server.common.exception.code.DefaultErrorCode;

public record ResponseDto<T> (
        boolean isSuccess,
        T data,
        ErrorDto error
) {
    // 결과가 없는 성공 응답
    public static <T> ResponseDto<T> success() {
        return new ResponseDto<>(true, null, null);
    }

    // 성공 응답
    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>(true, data, null);
    }

    // 실패 응답
    public static <T> ResponseDto<T> fail(DefaultErrorCode errorCode) {
        return new ResponseDto<>(false, null, ErrorDto.of(errorCode.getHttpStatus().value(), errorCode.getMessage()));
    }

    public static <T> ResponseDto<T> validFail(String errorMessage) {
        return new ResponseDto<>(false, null, ErrorDto.of(HttpStatus.BAD_REQUEST.value(), errorMessage));
    }
}

