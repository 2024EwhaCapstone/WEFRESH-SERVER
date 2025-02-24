package org.wefresh.wefresh_server.recipe.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wefresh.wefresh_server.common.exception.BusinessException;
import org.wefresh.wefresh_server.common.exception.code.RecipeErrorCode;
import org.wefresh.wefresh_server.recipe.domain.RecipeBase;
import org.wefresh.wefresh_server.recipe.repository.RecipeRepository;

@Component
@RequiredArgsConstructor
public class RecipeRetriever {

    private final RecipeRepository recipeRepository;

    public RecipeBase findById(final Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(RecipeErrorCode.RECIPE_NOT_FOUND));
    }
}
