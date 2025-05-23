package org.wefresh.wefresh_server.food.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.wefresh.wefresh_server.common.exception.BusinessException;
import org.wefresh.wefresh_server.common.exception.code.FoodErrorCode;
import org.wefresh.wefresh_server.food.domain.Category;
import org.wefresh.wefresh_server.food.domain.Food;
import org.wefresh.wefresh_server.food.repository.FoodRepository;

import java.util.List;
import java.util.Optional;

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

    public Food findById(final Long foodId) {
        return foodRepository.findById(foodId)
                .orElseThrow(() -> new BusinessException(FoodErrorCode.NOT_FOUND_FOOD));
    }

    public List<Food> findByIdIn(List<Long> ids) {
        return foodRepository.findByIdIn(ids);
    }

    public List<Food> findRandomFoods(Long userId, int count) {
        return foodRepository.findRandomFoodsByUser(userId, PageRequest.of(0, count));
    }

    public Optional<Food> findByUserIdAndName(Long userId, String name) {
        return foodRepository.findByUserIdAndName(userId, name);
    }
}
