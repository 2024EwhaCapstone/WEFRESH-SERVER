package org.wefresh.wefresh_server.food.repository.custom;

import org.wefresh.wefresh_server.food.domain.Category;
import org.wefresh.wefresh_server.food.domain.Food;

import java.util.List;

public interface FoodRepositoryCustom {
    List<Food> findBySearch(Long userId, Category category, String name);
}
