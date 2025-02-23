package org.wefresh.wefresh_server.todayRecipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wefresh.wefresh_server.todayRecipe.domain.TodayRecipe;

public interface TodayRecipeRepository extends JpaRepository<TodayRecipe, Long> {

    void deleteByUserId(Long id);
}
