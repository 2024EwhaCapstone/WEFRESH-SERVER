package org.wefresh.wefresh_server.todayRecipe.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wefresh.wefresh_server.todayRecipe.domain.TodayRecipe;
import org.wefresh.wefresh_server.todayRecipe.repository.TodayRecipeRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TodayRecipeSaver {

    private final TodayRecipeRepository todayRecipeRepository;

    public List<TodayRecipe> saveAll(final List<TodayRecipe> recipes) {
        return todayRecipeRepository.saveAll(recipes);
    }
}
