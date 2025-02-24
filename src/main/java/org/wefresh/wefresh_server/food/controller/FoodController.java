package org.wefresh.wefresh_server.food.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wefresh.wefresh_server.common.auth.annotation.UserId;
import org.wefresh.wefresh_server.common.dto.ResponseDto;
import org.wefresh.wefresh_server.food.dto.request.FoodRegisterDto;
import org.wefresh.wefresh_server.food.service.FoodService;

@RestController
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @PostMapping("/foods")
    public ResponseEntity<ResponseDto<Void>> registerFood(
            @UserId final Long userId,
            @ModelAttribute final FoodRegisterDto foodRegisterDto
    ) {
        foodService.registerFood(userId, foodRegisterDto);
        return ResponseEntity.ok().body(ResponseDto.success());
    }
}
