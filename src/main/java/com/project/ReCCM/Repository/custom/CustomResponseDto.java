package com.project.ReCCM.Repository.custom;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.ReCCM.domain.custom.Custom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomResponseDto {
    private Long id;
    private String customTitle;
    private String customContent;
    private String imgReal;

    private String memberId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 MM월 dd일")
    private LocalDateTime createdDate;

    private int likesCount;

    public CustomResponseDto(Custom custom) {
        this.customTitle = custom.getCustomTitle();
        this.customContent = custom.getCustomContent();
        this.imgReal = custom.getImgReal();
        this.memberId = custom.getMember().getMemberId();
        this.createdDate = custom.getCreatedDate();
        this.likesCount = custom.getLikesCount();

    }
}
