package org.wefresh.wefresh_server.food.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.wefresh.wefresh_server.food.domain.Category;
import org.wefresh.wefresh_server.food.domain.Food;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class FoodEditor {

    public void updateFood(
            final Food food,
            final String image,
            final String name,
            final Category category,
            final LocalDate date,
            final int count,
            final String memo
            ) {
        food.updateFood(
                image,
                name,
                category,
                date,
                count,
                memo
        );
    }

    public void updateFreshness(
            final Food food,
            final String result) {
        food.updateFreshness(result);
    }
}
