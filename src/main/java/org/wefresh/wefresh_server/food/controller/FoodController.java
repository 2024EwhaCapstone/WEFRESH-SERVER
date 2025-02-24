package org.wefresh.wefresh_server.food.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wefresh.wefresh_server.common.auth.annotation.UserId;
import org.wefresh.wefresh_server.common.dto.ResponseDto;
import org.wefresh.wefresh_server.food.dto.request.FoodRegisterDto;
import org.wefresh.wefresh_server.food.dto.response.FoodDto;
import org.wefresh.wefresh_server.food.dto.response.FoodListsDto;
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

    @GetMapping("/foods/expiring")
    public ResponseEntity<ResponseDto<FoodListsDto>> getExpiringFood(
            @UserId final Long userId
    ) {
        return ResponseEntity.ok().body(ResponseDto.success(foodService.getExpiringFood(userId)));
    }

    @GetMapping("/foods")
    public ResponseEntity<ResponseDto<FoodListsDto>> getFoods(
            @UserId final Long userId,
            @RequestParam(required = false) final String category,
            @RequestParam(required = false) final String name
    ) {
        return ResponseEntity.ok().body(ResponseDto.success(foodService.getFoods(userId, category, name)));
    }

    @GetMapping("foods/{foodId}")
    public ResponseEntity<ResponseDto<FoodDto>> getFood(
            @UserId final Long userId,
            @PathVariable final Long foodId
    ) {
        return ResponseEntity.ok().body(ResponseDto.success(foodService.getFood(userId, foodId)));
    }

    @PutMapping("foods/{foodId}")
    public ResponseEntity<ResponseDto<Void>> updateFood(
            @UserId final Long userId,
            @PathVariable final Long foodId,
            @ModelAttribute final FoodRegisterDto foodRegisterDto
    ) {
        foodService.updateFood(userId, foodId, foodRegisterDto);
        return ResponseEntity.ok().body(ResponseDto.success());
    }

}
