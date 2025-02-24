package org.wefresh.wefresh_server.food.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wefresh.wefresh_server.food.domain.Food;
import org.wefresh.wefresh_server.food.repository.FoodRepository;

@Component
@RequiredArgsConstructor
public class FoodSaver {

    private final FoodRepository foodRepository;


    public void save(final Food food) {
        foodRepository.save(food);
    }
}
