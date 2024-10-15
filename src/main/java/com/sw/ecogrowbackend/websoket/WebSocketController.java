package com.sw.ecogrowbackend.websoket;

import com.sw.ecogrowbackend.community.dto.CommentDto;
import com.sw.ecogrowbackend.community.entity.Comment;
import com.sw.ecogrowbackend.community.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final CommentService commentService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/posts/{postId}/comments")
    public void sendNewComment(Long postId, Long profileId, CommentDto commentDto) {
        // 댓글을 생성하는 서비스 호출
        Comment newComment = commentService.addComment(postId, profileId, commentDto);

        // WebSocket을 통해 실시간으로 생성된 댓글을 전송
        messagingTemplate.convertAndSend("/topic/posts/" + postId + "/comments", newComment);
    }
}