package org.wefresh.wefresh_server.todayRecipe.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wefresh.wefresh_server.common.exception.BusinessException;
import org.wefresh.wefresh_server.common.exception.code.RecipeErrorCode;
import org.wefresh.wefresh_server.recipe.domain.RecipeBase;
import org.wefresh.wefresh_server.todayRecipe.domain.TodayRecipe;
import org.wefresh.wefresh_server.todayRecipe.repository.TodayRecipeRepository;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TodayRecipeRetriever {

    private final TodayRecipeRepository todayRecipeRepository;

    public TodayRecipe findById(final Long id) {
        return todayRecipeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(RecipeErrorCode.RECIPE_NOT_FOUND));
    }

    public List<TodayRecipe> findTodayRecipes(Long userId, LocalDate date) {
        return todayRecipeRepository.findAllByUserIdAndRecommendedDate(userId, date);
    }
}
