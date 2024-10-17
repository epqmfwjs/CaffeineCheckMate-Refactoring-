package com.project.ReCCM.Repository.custom;

import com.project.ReCCM.domain.custom.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponseDto {

    private Long id;
    private String text;
    private String memberId;
    private String createdDate;
    private String imgReal;

    public CommentResponseDto(Comment comment) {
        System.out.println("댓글 작성할때 저장 후 댓글리스트불러오기 DTO 1");
        this.id = comment.getId();
        this.text = comment.getText();
        this.memberId = comment.getMember().getMemberId();
        this.createdDate = formatDateTime(comment.getCreatedDate());
        this.imgReal = comment.getMember().getImgReal();
    }

    public CommentResponseDto(Long id, String text, String memberId, LocalDateTime createdDate, String imgReal) {
        System.out.println("특정게시글 댓글리스트불러오기 DTO 2");
        this.id = id;
        this.text = text;
        this.memberId = memberId;
        this.createdDate = formatDateTime(createdDate);
        this.imgReal = imgReal;
    }

    // LocalDateTime을 String으로 변환하는 메서드
    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); // 원하는 형식으로 변경
            return dateTime.format(formatter);
        }
        return null;
    }
}
