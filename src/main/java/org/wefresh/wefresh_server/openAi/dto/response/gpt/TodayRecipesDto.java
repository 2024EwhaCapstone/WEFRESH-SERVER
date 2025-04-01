package org.wefresh.wefresh_server.openAi.dto.response.gpt;

import org.wefresh.wefresh_server.todayRecipe.domain.TodayRecipe;

import java.util.List;

public record TodayRecipesDto(
        List<TodayRecipeDto> recipes
) {
    public static TodayRecipesDto from(List<TodayRecipe> recipes) {
        return new TodayRecipesDto(
                recipes.stream()
                        .map(TodayRecipeDto::from)
                        .toList()
        );
    }
}
