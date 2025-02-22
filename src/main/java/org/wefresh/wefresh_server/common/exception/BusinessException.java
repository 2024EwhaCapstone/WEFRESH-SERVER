package org.wefresh.wefresh_server.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.wefresh.wefresh_server.common.exception.code.DefaultErrorCode;

@Getter
@AllArgsConstructor
public class BusinessException extends RuntimeException {
    private final DefaultErrorCode errorCode;
}
