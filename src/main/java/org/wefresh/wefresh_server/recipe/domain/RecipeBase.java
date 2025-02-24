package org.wefresh.wefresh_server.recipe.domain;

public interface RecipeBase {
    Long getId();
    String getName();
    String getImage();
    int getDifficulty();
    String getIngredients();
    int getTime();
    int getCalorie();
    String getRecipe();
}

