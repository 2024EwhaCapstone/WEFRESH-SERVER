package org.wefresh.wefresh_server.food.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.wefresh.wefresh_server.food.domain.Food;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public record FoodDto(
        Long foodId,
        String name,
        String category,
        int count,
        String image,
        @JsonFormat(pattern = "yyyy년 MM월 dd일")
        LocalDate date,
        String memo,
        int dday,
        String color
) {
    public static FoodDto from(Food food) {
        int dday = (int) ChronoUnit.DAYS.between(food.getDate(), LocalDate.now());

        return new FoodDto(
                food.getId(),
                food.getName(),
                food.getCategory().getContent(),
                food.getCount(),
                food.getImage(),
                food.getDate(),
                food.getMemo(),
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
