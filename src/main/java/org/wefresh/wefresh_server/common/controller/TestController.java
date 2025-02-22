package org.wefresh.wefresh_server.common.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wefresh.wefresh_server.common.dto.ResponseDto;
import org.wefresh.wefresh_server.common.exception.BusinessException;
import org.wefresh.wefresh_server.common.exception.code.BusinessErrorCode;

@RestController
@RequiredArgsConstructor
public class TestController {

    @GetMapping("/test/default")
    public ResponseEntity<ResponseDto<Void>> testDefault() {
        throw new RuntimeException();
    }

    @GetMapping("/test/business")
    public ResponseEntity<ResponseDto<Void>> testBusiness() {
        throw new BusinessException(BusinessErrorCode.BAD_REQUEST);
    }

    @GetMapping("/test/success")
    public ResponseEntity<ResponseDto<Void>> testSuccess() {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success());
    }
}
