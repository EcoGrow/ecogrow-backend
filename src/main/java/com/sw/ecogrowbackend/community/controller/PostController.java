package com.sw.ecogrowbackend.community.controller;

import com.sw.ecogrowbackend.community.dto.PostDto;
import com.sw.ecogrowbackend.community.entity.Post;
import com.sw.ecogrowbackend.community.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 1. 게시글 생성
    @PostMapping("/{profileId}")
    public ResponseEntity<Post> createPost(@PathVariable Long profileId, @RequestBody PostDto postDto) {
        Post post = postService.createPost(profileId, postDto);
        return ResponseEntity.ok(post);
    }

    // 2. 전체 게시글 조회 (페이징)
    @GetMapping
    public ResponseEntity<Page<Post>> getAllPosts(@RequestParam int page) {
        Page<Post> posts = postService.getAllPosts(page);
        return ResponseEntity.ok(posts);
    }

    // 3. 게시글 수정
    @PutMapping("/{postId}")
    public ResponseEntity<Post> updatePost(@PathVariable Long postId, @RequestBody PostDto postDto) {
        Post updatedPost = postService.updatePost(postId, postDto);
        return ResponseEntity.ok(updatedPost);
    }

    // 4. 게시글 추천
    @PostMapping("/{postId}/recommend")
    public ResponseEntity<Void> recommendPost(@PathVariable Long postId, @RequestParam Long userId) {
        postService.recommendPost(postId, userId);
        return ResponseEntity.ok().build();
    }

    // 5. 게시글 검색
    @GetMapping("/search")
    public ResponseEntity<Page<Post>> searchPosts(@RequestParam String keyword, @RequestParam int page) {
        Page<Post> searchResults = postService.searchPosts(keyword, page);
        return ResponseEntity.ok(searchResults);
    }
    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable Long postId) {
        Post post = postService.getPostById(postId);
        return ResponseEntity.ok(post);
    }
}