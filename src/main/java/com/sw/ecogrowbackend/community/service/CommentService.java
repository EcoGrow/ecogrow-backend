package com.sw.ecogrowbackend.community.service;

import com.sw.ecogrowbackend.common.exception.CustomException;
import com.sw.ecogrowbackend.common.exception.ErrorCode;
import com.sw.ecogrowbackend.community.dto.CommentDto;
import com.sw.ecogrowbackend.community.entity.Comment;
import com.sw.ecogrowbackend.community.entity.Post;
import com.sw.ecogrowbackend.community.repository.CommentRepository;
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
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final ProfileRepository profileRepository;

    // 1. 댓글 작성 메서드 (profileId를 필요로 하는 경우)
    @Transactional
    public Comment addComment(Long postId, Long profileId, CommentDto commentDto) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        Profile profile = profileRepository.findById(profileId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (commentDto.getContent() == null || commentDto.getContent().trim().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_COMMENT_DATA);
        }

        Comment comment = new Comment(post, profile, commentDto.getContent());
        return commentRepository.save(comment);
    }

    // 2. 특정 게시글의 댓글을 페이징 처리하여 조회
    @Transactional(readOnly = true)
    public Page<Comment> getCommentsByPost(Long postId, int page) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        return commentRepository.findByPost(post, PageRequest.of(page, 10));
    }
}