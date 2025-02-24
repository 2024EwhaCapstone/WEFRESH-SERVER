package org.wefresh.wefresh_server.food.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Category {
    FRUIT("과일"),
    MEAT("고기"),
    FROZEN("냉동식품"),
    VEGETABLE("채소"),
    SIDE_DISH("반찬");

    private final String content;

    @JsonValue
    public String getContent() {
        return content;
    }

    @JsonCreator
    public static Category fromString(String value) {
        return Arrays.stream(Category.values())
                .filter(category -> category.content.equals(value) || category.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 카테고리 값: " + value));
    }
}
