package org.wefresh.wefresh_server.openAi.dto.response;

import org.wefresh.wefresh_server.food.domain.Food;
import org.wefresh.wefresh_server.recipe.domain.Recipe;

import java.util.List;

public record RecommendRecipeDto(
        Long recipeId,
        String name,
        String image,
        int difficulty,
        List<IngredientDto> ingredients,
        int time,
        int calorie,
        int likes
) {
    public static RecommendRecipeDto from(Recipe recipe, List<Food> userFoods) {
        return new RecommendRecipeDto(
                recipe.getId(),
                recipe.getName(),
                recipe.getImage(),
                recipe.getDifficulty(),
                IngredientDto.matchIngredients(List.of(recipe.getIngredients().split(", ")), userFoods),
                recipe.getTime(),
                recipe.getCalorie(),
                recipe.getLikeCount()
        );
    }
}
