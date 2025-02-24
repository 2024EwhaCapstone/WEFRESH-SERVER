package org.wefresh.wefresh_server.common.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FoodErrorCode implements DefaultErrorCode {
    //403 FORBIDDEN
    FOOD_FORBIDDEN(HttpStatus.FORBIDDEN, "해당 음식에 대한 권한이 없습니다."),
    // 404 Not Found
    NOT_FOUND_FOOD(HttpStatus.NOT_FOUND, "음식을 찾을 수 없습니다."),
    ;

    private HttpStatus httpStatus;
    private String message;
}
