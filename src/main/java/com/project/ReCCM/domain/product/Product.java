package com.project.ReCCM.domain.product;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK값

    @Column
    private String coffeeName; //커피이름

    @Column
    private String coffeeBrand; //브랜드이름

    @Column
    private int caffeine; //카페인양

    @Column
    private int saccharide; //당분양

    @Column
    private int calorie; //칼로리양

    @Column
    private String coffeeContent; //커피설명

    @Column
    private String coffeeType; //종류 예) tea,coffee,Latte 등등

    @Column
    private boolean favorite; // 즐겨찾기 유무

    @Column
    private String imgReal; // 이미지 오리지널네임
}
