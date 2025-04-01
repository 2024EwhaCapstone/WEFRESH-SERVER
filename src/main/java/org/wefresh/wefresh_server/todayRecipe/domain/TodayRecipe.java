package org.wefresh.wefresh_server.todayRecipe.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.wefresh.wefresh_server.common.base.BaseTimeEntity;
import org.wefresh.wefresh_server.recipe.domain.RecipeBase;
import org.wefresh.wefresh_server.user.domain.User;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TodayRecipe extends BaseTimeEntity implements RecipeBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 2083)
    private String image;

    private int time;

    private int calorie;

    private int likeCount;

    private int difficulty;

    @Column(columnDefinition = "TEXT")
    private String ingredients;

    @Column(columnDefinition = "TEXT")
    private String recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public TodayRecipe(User user, String name, String image, int time, int calorie, int likeCount, int difficulty, String ingredients, String recipe) {
        this.user = user;
        this.name = name;
        this.image = image;
        this.time = time;
        this.calorie = calorie;
        this.likeCount = likeCount;
        this.difficulty = difficulty;
        this.ingredients = ingredients;
        this.recipe = recipe;
    }
}
