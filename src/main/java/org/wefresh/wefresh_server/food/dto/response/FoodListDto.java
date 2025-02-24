package org.wefresh.wefresh_server.food.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.wefresh.wefresh_server.food.domain.Food;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public record FoodListDto(
        Long foodId,
        String name,
        String image,
        @JsonFormat(pattern = "yyyy년 MM월 dd일")
        LocalDate date,
        int dday,
        String color
) {
    public static FoodListDto from(Food food) {
        int dday = (int) ChronoUnit.DAYS.between(food.getDate(), LocalDate.now());

        return new FoodListDto(
                food.getId(),
                food.getName(),
                food.getImage(),
                food.getDate(),
                dday,
                getColor(dday)
        );
    }

    private static String getColor(int dday) {
        if (dday <= -8) return "#56D34F";
        if (dday <= -4) return "#F2DC60";
        if (dday <= 0) return "#F46161";
        return "#676767";
    }
}
