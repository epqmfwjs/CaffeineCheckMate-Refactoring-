package com.project.ReCCM.Repository.mypage;

import com.project.ReCCM.domain.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MyPageFavoriteResponseDto {

    private Long id;
    private String coffeeName;
    private String coffeeBrand;
    private String coffeeType;
    private String imgReal;
    private String coffeeContent;
    private int caffeine; //카페인양
    private int saccharide; //당분양
    private int calorie; //칼로리양

    public MyPageFavoriteResponseDto(Product product) {
        this.coffeeName = product.getCoffeeName();
        this.coffeeBrand = product.getCoffeeBrand();
        this.coffeeType = product.getCoffeeType();
        this.imgReal = product.getImgReal();
        this.caffeine = product.getCaffeine();
        this.saccharide = product.getSaccharide();
        this.calorie = product.getCalorie();
        this.id = product.getId();
        this.coffeeContent = product.getCoffeeContent();
    }
}
