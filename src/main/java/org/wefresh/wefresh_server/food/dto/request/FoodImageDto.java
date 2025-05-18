package org.wefresh.wefresh_server.food.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record FoodImageDto(
        MultipartFile image
) {
}
