package org.wefresh.wefresh_server.recipe.dto.response;

import org.wefresh.wefresh_server.common.exception.BusinessException;
import org.wefresh.wefresh_server.common.exception.code.GptErrorCode;
import org.wefresh.wefresh_server.recipe.domain.Recipe;
import org.wefresh.wefresh_server.recipe.domain.RecipeBase;

import java.util.ArrayList;
import java.util.List;

public record RecipeDto(
        Long recipeId,
        String name,
        String image,
        int difficulty,
        List<IngredientDto> ingredients,
        int time,
        int calorie,
        String recipe
) {
    public static RecipeDto from(RecipeBase recipe) {
        return new RecipeDto(
                recipe.getId(),
                recipe.getName(),
                recipe.getImage(),
                recipe.getDifficulty(),
                parseIngredients(recipe.getIngredients()),
                recipe.getTime(),
                recipe.getCalorie(),
                recipe.getRecipe()
        );
    }

    private static List<IngredientDto> parseIngredients(String ingredients) {
        List<IngredientDto> ingredientList = new ArrayList<>();

        if (ingredients == null || ingredients.isBlank()) {
            return ingredientList;
        }

        String[] items = ingredients.split(",");
        for (String item : items) {
            ingredientList.add(IngredientDto.from(item.trim()));
        }
        return ingredientList;
    }

    public record IngredientDto(
            String name,
            String amount
    ) {
        public static IngredientDto from(String ingredient) {
            String[] parts = ingredient.split(" ", 2);
            if (parts.length == 2) {
                return new IngredientDto(parts[0], parts[1]);
            }
            return new IngredientDto(ingredient, "-");
        }
    }
}
