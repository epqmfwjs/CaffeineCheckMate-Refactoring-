package com.project.ReCCM.Repository.mypage;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.ReCCM.domain.custom.Custom;
import com.project.ReCCM.domain.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MyPageLikeResponseDto {

    private String customTitle;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 MM월 dd일")
    private LocalDateTime createdDate;

    private String imgReal;

    public MyPageLikeResponseDto(Custom custom) {
        this.customTitle = custom.getCustomTitle();
        this.createdDate = custom.getCreatedDate();
        this.imgReal = custom.getImgReal();
    }
}
