package org.wefresh.wefresh_server.recipe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wefresh.wefresh_server.common.exception.BusinessException;
import org.wefresh.wefresh_server.common.exception.code.BusinessErrorCode;
import org.wefresh.wefresh_server.recipe.domain.RecipeBase;
import org.wefresh.wefresh_server.recipe.dto.response.RecipeDto;
import org.wefresh.wefresh_server.recipe.manager.RecipeRetriever;
import org.wefresh.wefresh_server.todayRecipe.manager.TodayRecipeRetriever;
import org.wefresh.wefresh_server.user.manager.UserRetriever;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final UserRetriever userRetriever;
    private final RecipeRetriever recipeRetriever;
    private final TodayRecipeRetriever todayRecipeRetriever;

    @Transactional(readOnly = true)
    public RecipeDto getRecipe(
            final Long userId,
            final Long id,
            final String type
    ) {
        userRetriever.findById(userId);

        RecipeBase recipeBase = null;
        if (type.equals("general")) {
            recipeBase = recipeRetriever.findById(id);
        } else if (type.equals("today")) {
            recipeBase = todayRecipeRetriever.findById(id);
        } else {
            throw new BusinessException(BusinessErrorCode.MISSING_REQUIRED_PARAMETER);
        }

        return RecipeDto.from(recipeBase);
    }
}
