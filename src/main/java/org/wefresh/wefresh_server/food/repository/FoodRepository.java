package org.wefresh.wefresh_server.food.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.wefresh.wefresh_server.food.domain.Food;
import org.wefresh.wefresh_server.food.repository.custom.FoodRepositoryCustom;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long>, FoodRepositoryCustom {

    void deleteByUserId(Long id);

    @Query("""
      SELECT f FROM Food f
      WHERE f.user.id = :userId
      ORDER BY f.date ASC
      LIMIT 4
      """)
    List<Food> findExpiringByUserId(Long userId);
}
