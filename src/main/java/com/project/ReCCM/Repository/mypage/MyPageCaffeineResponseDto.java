package com.project.ReCCM.Repository.mypage;

import com.project.ReCCM.domain.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class MyPageCaffeineResponseDto {

    private String title;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate start; // FullCalendar가 요구하는 날짜 형식 (예: "2024-10-15")

    private double percentage; // 결과값

}
