package com.project.ReCCM.service.custom;

import com.project.ReCCM.Repository.custom.CommentResponseDto;
import com.project.ReCCM.domain.custom.Comment;
import com.project.ReCCM.domain.custom.CommentRepository;
import com.project.ReCCM.domain.custom.Custom;
import com.project.ReCCM.domain.custom.CustomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final CustomRepository customRepository;

    public CommentService(CommentRepository commentRepository, CustomRepository customRepository) {
        this.commentRepository = commentRepository;
        this.customRepository = customRepository;
    }

    // 댓글 생성 로직
    public CommentResponseDto createComment(Long postId, String text) {
        Custom custom = customRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        Comment comment = new Comment();
        comment.setText(text);
        comment.setCustom(custom); // 댓글을 게시글과 연결

        Comment savedComment = commentRepository.save(comment);
        return new CommentResponseDto(savedComment); // 저장된 댓글을 DTO로 변환하여 반환
    }

    // 특정 게시물의 댓글 리스트 조회
    public List<CommentResponseDto> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByCustomId(postId);
        return comments.stream()
                .map(CommentResponseDto::new) // Comment 엔티티를 DTO로 변환
                .toList();
    }
}
