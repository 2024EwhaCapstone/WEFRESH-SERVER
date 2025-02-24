package org.wefresh.wefresh_server.food.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wefresh.wefresh_server.food.domain.Category;
import org.wefresh.wefresh_server.food.domain.Food;
import org.wefresh.wefresh_server.food.repository.FoodRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FoodRetriever {

    private final FoodRepository foodRepository;

    public List<Food> findExpiringByUserId(final Long userId) {
        return foodRepository.findExpiringByUserId(userId);
    }

    public List<Food> findBySearch(
            final Long userId,
            final Category category,
            final String name
    ) {
        return foodRepository.findBySearch(userId, category, name);
    }
}
