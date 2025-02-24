package org.wefresh.wefresh_server.common.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GptErrorCode implements DefaultErrorCode {
    // 500 INTERNAL_SERVER
    GPT_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "GPT 응답 처리 중 오류 발생."),
    ;

    private HttpStatus httpStatus;
    private String message;
}
