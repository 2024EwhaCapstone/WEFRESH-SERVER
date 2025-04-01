package org.wefresh.wefresh_server.openAi.dto.response.gpt;

import org.wefresh.wefresh_server.todayRecipe.domain.TodayRecipe;

import java.util.List;

public record TodayRecipeDto(
        Long todayRecipeId,
        String name,
        String image,
        List<String> ingredients
) {
    public static TodayRecipeDto from(TodayRecipe recipe) {
        return new TodayRecipeDto(
                recipe.getId(),
                recipe.getName(),
                recipe.getImage(),
                List.of(recipe.getIngredients().split(", "))
        );
    }
}
