package org.wefresh.wefresh_server.recipe.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wefresh.wefresh_server.common.exception.BusinessException;
import org.wefresh.wefresh_server.common.exception.code.RecipeErrorCode;
import org.wefresh.wefresh_server.recipe.domain.Recipe;
import org.wefresh.wefresh_server.recipe.repository.RecipeRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RecipeSaver {

    private final RecipeRepository recipeRepository;

    public List<Recipe> saveAll(final List<Recipe> recipes) {
        return recipeRepository.saveAll(recipes);
    }
}
