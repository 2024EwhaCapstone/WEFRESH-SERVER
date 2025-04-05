package org.wefresh.wefresh_server.todayRecipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wefresh.wefresh_server.todayRecipe.domain.TodayRecipe;

import java.time.LocalDate;
import java.util.List;

public interface TodayRecipeRepository extends JpaRepository<TodayRecipe, Long> {

    void deleteByUserId(Long id);

    List<TodayRecipe> findAllByUserIdAndRecommendedDate(Long userId, LocalDate date);

}
