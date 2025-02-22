package org.wefresh.wefresh_server.bookmark.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.wefresh.wefresh_server.common.base.BaseTimeEntity;
import org.wefresh.wefresh_server.recipe.domain.Recipe;
import org.wefresh.wefresh_server.todayRecipe.domain.TodayRecipe;
import org.wefresh.wefresh_server.user.domain.User;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "today_recipe_id")
    private TodayRecipe todayRecipe;

    @Builder
    public Bookmark(User user, Recipe recipe, TodayRecipe todayRecipe) {
        this.user = user;
        this.recipe = recipe;
        this.todayRecipe = todayRecipe;
    }




}
