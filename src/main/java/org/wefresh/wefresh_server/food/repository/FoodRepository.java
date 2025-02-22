package org.wefresh.wefresh_server.food.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wefresh.wefresh_server.food.domain.Food;

public interface FoodRepository extends JpaRepository<Food, Long> {
}
