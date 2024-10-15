package com.sw.ecogrowbackend.community.repository;

import com.sw.ecogrowbackend.community.entity.Comment;
import com.sw.ecogrowbackend.community.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 특정 게시글에 대한 댓글을 페이징 처리하여 조회
    Page<Comment> findByPost(Post post, Pageable pageable);
}