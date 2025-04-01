package org.wefresh.wefresh_server.openAi.dto.response.gpt;

import org.wefresh.wefresh_server.food.domain.Food;
import org.wefresh.wefresh_server.recipe.domain.Recipe;

import java.util.List;

public record RecommendRecipesDto(
        List<RecommendRecipeDto> recipes
) {
    public static RecommendRecipesDto from(List<Recipe> recipes, List<Food> userFoods) {
        return new RecommendRecipesDto(
                recipes.stream()
                        .map(recipe -> RecommendRecipeDto.from(recipe, userFoods))
                        .toList()
        );
    }
}
