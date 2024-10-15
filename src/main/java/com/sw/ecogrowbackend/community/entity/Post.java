package com.sw.ecogrowbackend.community.entity;

import com.sw.ecogrowbackend.domain.profile.entity.Profile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile author;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private int views = 0;

    @Column(nullable = false)
    private int recommendations = 0;

    @Column(nullable = false)
    private boolean isNotice = false;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Post(Profile author, String title, String content, boolean isNotice) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.isNotice = isNotice;
    }

    public void updatePost(String title, String content) {
        this.title = title;
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }

    public void incrementViews() {
        this.views++;
    }

    public void incrementRecommendations() {
        this.recommendations++;
    }
}