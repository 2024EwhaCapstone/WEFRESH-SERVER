package org.wefresh.wefresh_server.food.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Category {
    FRUIT("과일"),
    MEAT("고기"),
    FROZEN("냉동식품"),
    VEGETABLE("채소"),
    SIDE_DISH("반찬");

    private String content;
}
