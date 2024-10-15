package com.project.ReCCM.Repository.mypage;

import com.project.ReCCM.domain.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MyPageCaffeineResponseDto {

    private String title;
    private String start; // FullCalendar가 요구하는 날짜 형식 (예: "2024-10-15")
    private String caffeineLevel; // high, medium, low 등의 카페인 섭취량 정보

}
