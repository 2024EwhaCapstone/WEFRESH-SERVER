package org.wefresh.wefresh_server.common.exception.code;

import org.springframework.http.HttpStatus;

public interface DefaultErrorCode {
    HttpStatus getHttpStatus();
    String getMessage();
}
