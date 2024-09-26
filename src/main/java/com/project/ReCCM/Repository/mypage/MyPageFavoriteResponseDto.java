package com.project.ReCCM.Repository.mypage;

import com.project.ReCCM.domain.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MyPageFavoriteResponseDto {

    private String coffeeName;
    private String coffeeBrand;
    private String coffeeType;

    public MyPageFavoriteResponseDto(Product product) {
        this.coffeeName = product.getCoffeeName();
        this.coffeeBrand = product.getCoffeeBrand();
        this.coffeeType = product.getCoffeeType();
    }
}
