package com.project.ReCCM.Repository.mypage;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.ReCCM.domain.custom.Custom;
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

    private String memberId;

    private String customContent;

    private int likesCount;

    private Long id;

    private String brand;
    private String syrup;
    private String whipped;
    private String shot;
    private String milk;
    private String coffeeType;

    public MyPageLikeResponseDto(Custom custom) {
        this.customTitle = custom.getCustomTitle();
        this.createdDate = custom.getCreatedDate();
        this.imgReal = custom.getImgReal();
        this.memberId = custom.getMember().getMemberId();
        this.customContent = custom.getCustomContent();
        this.likesCount = custom.getLikesCount();
        this.id = custom.getId();
        this.brand = custom.getBrand();
        this.syrup = custom.getSyrup();
        this.whipped = custom.getWhipped();
        this.shot = custom.getShot();
        this.milk = custom.getMilk();
        this.coffeeType = custom.getCoffeeType();
    }
}
