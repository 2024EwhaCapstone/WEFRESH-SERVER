package org.wefresh.wefresh_server.todayRecipe.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wefresh.wefresh_server.todayRecipe.repository.TodayRecipeRepository;

@Component
@RequiredArgsConstructor
public class TodayRecipeRemover {

    private final TodayRecipeRepository todayRecipeRepository;

    public void deleteByUserId(final Long id) {
        todayRecipeRepository.deleteByUserId(id);
    }
}
