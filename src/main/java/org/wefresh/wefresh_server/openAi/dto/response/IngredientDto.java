package org.wefresh.wefresh_server.openAi.dto.response;

import org.wefresh.wefresh_server.food.domain.Food;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public record IngredientDto(
        String name,
        Integer dday,
        String color
) {
    public static IngredientDto from(Food food) {
        int dday = (int) ChronoUnit.DAYS.between(food.getDate(), LocalDate.now());

        return new IngredientDto(
                food.getName(),
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

    public static List<IngredientDto> matchIngredients(List<String> gptIngredients, List<Food> userFoods) {
        List<IngredientDto> ingredientList = new ArrayList<>();

        for (String gptIngredient : gptIngredients) {
            String[] parts = gptIngredient.split(" ", 2); // "연어 200g" -> ["연어", "200g"]
            String ingredientName = parts[0];

            // 사용자가 보유한 재료 중 같은 이름의 재료 찾기
            Food matchingFood = userFoods.stream()
                    .filter(food -> food.getName().equalsIgnoreCase(ingredientName))
                    .findFirst()
                    .orElse(null);

            if (matchingFood != null) {
                // 사용자가 가진 재료와 일치하면 유통기한 정보 추가
                ingredientList.add(IngredientDto.from(matchingFood));
            } else {
                // 사용자가 없는 재료라면 dday = 0, color 기본값
                ingredientList.add(new IngredientDto(ingredientName, null, "#FFFFFF"));
            }
        }
        return ingredientList;
    }
}
