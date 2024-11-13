package com.sw.ecogrowbackend.domain.community.repository;

import com.sw.ecogrowbackend.domain.community.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // 게시글을 공지사항이 우선적으로 출력되도록 정렬 후 페이징 처리
    Page<Post> findByOrderByIsNoticeDescCreatedAtDesc(Pageable pageable);

    // 게시글 제목과 내용으로 검색
    Page<Post> findByTitleContainingOrContentContaining(String titleKeyword, String contentKeyword, Pageable pageable);
}