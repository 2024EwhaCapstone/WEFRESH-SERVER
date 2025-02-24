package org.wefresh.wefresh_server.common.advice;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.wefresh.wefresh_server.common.dto.ResponseDto;
import org.wefresh.wefresh_server.common.exception.BusinessException;
import org.wefresh.wefresh_server.common.exception.code.BusinessErrorCode;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {BusinessException.class})
    public ResponseEntity<ResponseDto<BusinessErrorCode>> handleBusinessException(BusinessException e) {
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(ResponseDto.fail(e.getErrorCode()));
    }

    @ExceptionHandler(value = {MissingRequestHeaderException.class})
    public ResponseEntity<ResponseDto<BusinessErrorCode>> handleMissingHeaderException(MissingRequestHeaderException e) {
        return ResponseEntity
                .status(BusinessErrorCode.MISSING_REQUIRED_HEADER.getHttpStatus())
                .body(ResponseDto.fail(BusinessErrorCode.MISSING_REQUIRED_HEADER));
    }

    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    public ResponseEntity<ResponseDto<String>> handleMissingParameterException(MissingServletRequestParameterException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseDto.fail(BusinessErrorCode.MISSING_REQUIRED_PARAMETER));
    }

    @ExceptionHandler(value = {MissingRequestCookieException.class})
    public ResponseEntity<ResponseDto<BusinessErrorCode>> handleMissingCookieException(MissingRequestCookieException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseDto.fail(BusinessErrorCode.MISSING_REQUIRED_COOKIE));
    }

    // 존재하지 않는 요청에 대한 예외
    @ExceptionHandler(value = {NoHandlerFoundException.class})
    public ResponseEntity<ResponseDto<BusinessErrorCode>> handleNoPageFoundException(NoHandlerFoundException e) {
        log.warn("GlobalExceptionHandler catch NoHandlerFoundException : {}", BusinessErrorCode.NOT_FOUND_END_POINT.getMessage());
        return ResponseEntity
                .status(BusinessErrorCode.NOT_FOUND_END_POINT.getHttpStatus())
                .body(ResponseDto.fail(BusinessErrorCode.NOT_FOUND_END_POINT));
    }

    // 잘못된 Method로 요청한 경우
    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ResponseDto<BusinessErrorCode>> handleNoPageFoundException(HttpRequestMethodNotSupportedException e) {
        log.warn("GlobalExceptionHandler catch NoHandlerFoundException : {}", BusinessErrorCode.NOT_FOUND_END_POINT.getMessage());
        return ResponseEntity
                .status(BusinessErrorCode.METHOD_NOT_ALLOWED.getHttpStatus())
                .body(ResponseDto.fail(BusinessErrorCode.METHOD_NOT_ALLOWED));
    }

    @ExceptionHandler(value = {
            HandlerMethodValidationException.class,
            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<ResponseDto<BusinessErrorCode>> handleValidationException(Exception e) {
        log.warn("GlobalExceptionHandler catch MethodArgumentNotValidException : {}", e.getMessage());
        return ResponseEntity
                .status(BusinessErrorCode.INVALID_ARGUMENTS.getHttpStatus())
                .body(ResponseDto.fail(BusinessErrorCode.INVALID_ARGUMENTS));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ResponseDto<BusinessErrorCode>> handleMaxSizeException(MaxUploadSizeExceededException e) {
        log.warn("GlobalExceptionHandler catch MaxUploadSizeExceededException : {}", e.getMessage());
        return ResponseEntity
                .status(BusinessErrorCode.PAYLOAD_TOO_LARGE.getHttpStatus())
                .body(ResponseDto.fail(BusinessErrorCode.PAYLOAD_TOO_LARGE));
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ResponseDto<BusinessErrorCode>> handleException(java.lang.Exception e) {
        log.error("Unhandled exception occurred: {}", e.getMessage(), e);
        return ResponseEntity
                .status(BusinessErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(ResponseDto.fail(BusinessErrorCode.INTERNAL_SERVER_ERROR));
    }
}
