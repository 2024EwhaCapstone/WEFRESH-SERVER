package org.wefresh.wefresh_server.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.wefresh.wefresh_server.common.base.BaseTimeEntity;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    private String providerId;

    private String nickname;

    private String profileImg;

    private String email;

    @Builder
    public User(
            Provider provider,
            String providerId,
            String nickname,
            String profileImg,
            String email
    ) {
        this.provider = provider;
        this.providerId = providerId;
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.email = email;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}
