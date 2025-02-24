package org.wefresh.wefresh_server.food.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.wefresh.wefresh_server.common.base.BaseTimeEntity;
import org.wefresh.wefresh_server.user.domain.User;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Food extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String image;

    @Enumerated(EnumType.STRING)
    private Category category;

    private LocalDate date;

    private int count;

    private String memo;

    private Double fresh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Food(User user, String name, String image, Category category, LocalDate date, int count, String memo, Double fresh) {
        this.user = user;
        this.name = name;
        this.image = image;
        this.category = category;
        this.date = date;
        this.count = count;
        this.memo = memo;
        this.fresh = fresh;
    }
}
