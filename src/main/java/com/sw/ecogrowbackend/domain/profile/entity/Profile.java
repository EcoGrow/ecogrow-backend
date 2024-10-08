package com.sw.ecogrowbackend.domain.profile.entity;

import com.sw.ecogrowbackend.domain.auth.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor

@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 유저와 1:1 매핑 관계
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(length = 100)
    private String bio;

    @Column(length = 100)
    private String profileImageUrl;


    public Profile(User user, String bio, String profileImageUrl) {
        this.user = user;
        this.bio = bio;
        this.profileImageUrl = profileImageUrl;
    }
}