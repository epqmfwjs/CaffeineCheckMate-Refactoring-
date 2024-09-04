package com.project.ReCCM.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CoffeeList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK값

    private String coffeeName; //커피이름

    private String coffeeBrand; //브랜드이름

    private int caffeine; //카페인양

    private int saccharide; //당분양

    private int calorie; //칼로리양

    private String coffeeContent; //커피설명

    private String coffeeType; //종류 예) tea,coffee,Latte 등등

    private Long favorite; // 즐겨찾기 유무

    private String imgReal; // 이미지 오리지널네임

    private String imgCopy; // 이미지 카피네임
    // 다른 필드들

}
