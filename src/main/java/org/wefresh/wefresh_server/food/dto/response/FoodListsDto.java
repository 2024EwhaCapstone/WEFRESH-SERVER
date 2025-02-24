package org.wefresh.wefresh_server.food.dto.response;

import org.wefresh.wefresh_server.food.domain.Food;

import java.util.List;

public record FoodListsDto(
        List<FoodListDto> foods
) {
    public static FoodListsDto from(List<Food> foods) {
        return new FoodListsDto(
                foods.stream()
                        .map(FoodListDto::from)
                        .toList()
        );
    }
}
