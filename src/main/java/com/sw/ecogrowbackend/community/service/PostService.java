package com.sw.ecogrowbackend.community.service;

import com.sw.ecogrowbackend.common.exception.CustomException;
import com.sw.ecogrowbackend.common.exception.ErrorCode;
import com.sw.ecogrowbackend.community.dto.PostDto;
import com.sw.ecogrowbackend.community.entity.Post;
import com.sw.ecogrowbackend.community.repository.PostRepository;
import com.sw.ecogrowbackend.domain.profile.entity.Profile;
import com.sw.ecogrowbackend.domain.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ProfileRepository profileRepository;
    //게시글 ID로 게시글을 찾는 메서드
    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }
    // 1. 게시글 생성
    @Transactional
    public Post createPost(Long profileId, PostDto postDto) {
        Profile profile = profileRepository.findById(profileId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Post post = new Post(profile, postDto.getTitle(), postDto.getContent(), postDto.isNotice());
        return postRepository.save(post);
    }

    // 2. 전체 게시글 조회 (페이징 처리)
    @Transactional(readOnly = true)
    public Page<Post> getAllPosts(int page) {
        return postRepository.findByOrderByIsNoticeDescCreatedAtDesc(PageRequest.of(page, 10));
    }

    // 3. 게시글 수정
    @Transactional
    public Post updatePost(Long postId, PostDto postDto) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        post.updatePost(postDto.getTitle(), postDto.getContent());
        return postRepository.save(post);
    }

    // 4. 게시글 추천
    @Transactional
    public void recommendPost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if (post.getAuthor().getId().equals(userId)) {
            throw new CustomException(ErrorCode.CANNOT_RECOMMEND_OWN_POST);
        }

        if (hasAlreadyRecommended(postId, userId)) {
            throw new CustomException(ErrorCode.DUPLICATE_RECOMMENDATION);
        }

        post.incrementRecommendations();
        postRepository.save(post);
    }

    // 5. 게시글 검색
    @Transactional(readOnly = true)
    public Page<Post> searchPosts(String keyword, int page) {
        return postRepository.findByTitleContainingOrContentContaining(keyword, keyword, PageRequest.of(page, 10));
    }

    // 이미 추천했는지 확인하는 메서드
    private boolean hasAlreadyRecommended(Long postId, Long userId) {
        // 추천 여부 확인 로직 (가상 구현)
        return false;
    }
}