package org.wefresh.wefresh_server.food.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
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

    @Column(columnDefinition = "TEXT")
    private String fresh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Food(User user, String name, String image, Category category, LocalDate date, int count, String memo, String fresh) {
        this.user = user;
        this.name = name;
        this.image = image;
        this.category = category;
        this.date = date;
        this.count = count;
        this.memo = memo;
        this.fresh = fresh;
    }

    public void updateFood(String image, String name, Category category, LocalDate date, int count, String memo) {
        this.image = image;
        this.name = name;
        this.category = category;
        this.date = date;
        this.count = count;
        this.memo = memo;
    }

    public void updateFreshness(String result) {
        this.fresh = result;
    }

    public void decrementCount() {
        this.count = this.count - 1;
    }
}
