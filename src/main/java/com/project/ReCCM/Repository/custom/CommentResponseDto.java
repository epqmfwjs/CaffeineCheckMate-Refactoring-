package com.project.ReCCM.Repository.custom;

import com.project.ReCCM.domain.custom.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponseDto {

    private Long id;
    private String text;
    private String memberId;

    public CommentResponseDto(Comment comment) {
        System.out.println("댓글 작성할때 저장 후 댓글리스트불러오기 DTO 1");
        this.id = comment.getId();
        this.text = comment.getText();
        this.memberId = comment.getMember().getMemberId();
    }

    public CommentResponseDto(Long id, String text, String memberId) {
        System.out.println("특정게시글 댓글리스트불러오기 DTO 2");
        this.id = id;
        this.text = text;
        this.memberId = memberId;
    }
}
