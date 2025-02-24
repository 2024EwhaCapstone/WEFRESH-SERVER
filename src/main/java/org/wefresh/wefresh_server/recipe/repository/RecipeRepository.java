package org.wefresh.wefresh_server.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wefresh.wefresh_server.recipe.domain.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
