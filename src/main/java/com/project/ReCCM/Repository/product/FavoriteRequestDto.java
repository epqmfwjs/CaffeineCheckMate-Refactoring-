package com.project.ReCCM.Repository.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteRequestDto {

    private Long postId;
    private Long memberId;

}
