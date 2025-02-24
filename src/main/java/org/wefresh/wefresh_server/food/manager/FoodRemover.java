package org.wefresh.wefresh_server.food.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wefresh.wefresh_server.food.repository.FoodRepository;

@Component
@RequiredArgsConstructor
public class FoodRemover {

    private final FoodRepository foodRepository;

    public void deleteByUserId(final Long id) {
        foodRepository.deleteByUserId(id);
    }

    public void deleteById(final Long id) {
        foodRepository.deleteById(id);
    }
}
