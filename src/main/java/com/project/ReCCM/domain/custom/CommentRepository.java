package com.project.ReCCM.domain.custom;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByCustomId(Long postId); // 게시물 ID로 댓글 조회

}
