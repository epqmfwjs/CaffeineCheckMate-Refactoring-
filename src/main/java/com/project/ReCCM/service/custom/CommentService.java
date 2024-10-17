package com.project.ReCCM.service.custom;

import com.project.ReCCM.Repository.custom.CommentResponseDto;
import com.project.ReCCM.Repository.custom.CustomResponseDto;
import com.project.ReCCM.domain.custom.Comment;
import com.project.ReCCM.domain.custom.CommentRepository;
import com.project.ReCCM.domain.custom.Custom;
import com.project.ReCCM.domain.custom.CustomRepository;
import com.project.ReCCM.domain.member.Member;
import com.project.ReCCM.domain.member.MemberRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final CustomRepository customRepository;

    private final MemberRepository memberRepository;

    public CommentService(CommentRepository commentRepository, CustomRepository customRepository, MemberRepository memberRepository) {
        this.commentRepository = commentRepository;
        this.customRepository = customRepository;
        this.memberRepository = memberRepository;
    }

    // 댓글 생성 로직
    public CommentResponseDto createComment(Long memberPK, Long postId, String text) {
        Custom custom = customRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        
        Member member = memberRepository.findById(memberPK)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 존재하지 않습니다."));

        Comment comment = new Comment();
        comment.setMember(member);
        comment.setText(text);
        comment.setCustom(custom); // 댓글을 게시글과 연결

        Comment savedComment = commentRepository.save(comment);
        return new CommentResponseDto(savedComment); // 저장된 댓글을 DTO로 변환하여 반환
    }

    // 특정 게시물의 댓글 리스트 조회
    public List<CommentResponseDto> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByCustomId(postId);

        List<CommentResponseDto> response = comments.stream()
                .map(comment -> new CommentResponseDto(
                        comment.getId(),
                        comment.getText(),
                        comment.getMember().getMemberId(),
                        comment.getCreatedDate(),
                        comment.getMember().getImgReal()
                        ))
                .collect(Collectors.toList());
        return response;

    }
}
