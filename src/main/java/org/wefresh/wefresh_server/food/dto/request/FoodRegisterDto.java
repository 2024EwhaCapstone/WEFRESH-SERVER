package org.wefresh.wefresh_server.food.dto.request;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import org.wefresh.wefresh_server.food.domain.Category;

import java.time.LocalDate;

public record FoodRegisterDto(
        MultipartFile image,
        String name,
        String category,
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        int count,
        String memo
) {
        public Category getCategoryEnum() {
                return Category.fromString(category);
        }
}

