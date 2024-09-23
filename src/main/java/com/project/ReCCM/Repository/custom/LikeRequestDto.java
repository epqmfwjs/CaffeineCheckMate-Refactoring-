package com.project.ReCCM.Repository.custom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikeRequestDto {
    private Long postId;  // 게시글 ID
    private Long memberId;  // 사용자 ID
}