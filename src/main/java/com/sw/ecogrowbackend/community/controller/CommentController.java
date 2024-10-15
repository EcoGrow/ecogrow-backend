package com.sw.ecogrowbackend.community.controller;

import com.sw.ecogrowbackend.community.dto.CommentDto;
import com.sw.ecogrowbackend.community.entity.Comment;
import com.sw.ecogrowbackend.community.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 1. 댓글 작성 메서드 호출 (profileId 포함)
    public ResponseEntity<Comment> addComment(@PathVariable Long postId, @PathVariable Long profileId, @RequestBody CommentDto commentDto) {
        Comment comment = commentService.addComment(postId, profileId, commentDto);
        return ResponseEntity.ok(comment);
    }

    // 2. 특정 게시글의 댓글 조회 (페이징 처리)
    @GetMapping
    public ResponseEntity<Page<Comment>> getCommentsByPost(@PathVariable Long postId, @RequestParam int page) {
        Page<Comment> comments = commentService.getCommentsByPost(postId, page);
        return ResponseEntity.ok(comments);
    }
}