package org.wefresh.wefresh_server.recipe.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.wefresh.wefresh_server.common.base.BaseTimeEntity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recipe extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String image;

    private int time;

    private int calorie;

    private int like;

    private int difficulty;

    @Column(columnDefinition = "TEXT")
    private String ingredients;

    @Column(columnDefinition = "TEXT")
    private String recipe;

    @Builder
    public Recipe(String name, String image, int time, int calorie, int like, int difficulty, String ingredients, String recipe) {
        this.name = name;
        this.image = image;
        this.time = time;
        this.calorie = calorie;
        this.like = like;
        this.difficulty = difficulty;
        this.ingredients = ingredients;
        this.recipe = recipe;
    }
}
