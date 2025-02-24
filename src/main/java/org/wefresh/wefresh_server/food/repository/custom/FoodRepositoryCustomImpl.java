package org.wefresh.wefresh_server.food.repository.custom;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.wefresh.wefresh_server.food.domain.Category;
import org.wefresh.wefresh_server.food.domain.Food;

import java.util.List;

import static org.wefresh.wefresh_server.food.domain.QFood.food;

@Repository
@RequiredArgsConstructor
public class FoodRepositoryCustomImpl implements FoodRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Food> findBySearch(Long userId, Category category, String name) {
        return queryFactory
                .selectFrom(food)
                .where(
                        categoryEq(category),
                        nameLike(name)
                )
                .orderBy(food.date.asc())
                .fetch();
    }

    private BooleanExpression categoryEq(Category category) {
        return category != null ? food.category.eq(category) : null;
    }

    private BooleanExpression nameLike(String name) {
        return name != null ? food.name.stringValue().contains(name) : null;
    }

}
