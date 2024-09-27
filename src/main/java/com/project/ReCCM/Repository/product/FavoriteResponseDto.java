package com.project.ReCCM.Repository.product;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class FavoriteResponseDto {
    private boolean isFavorited;

    public FavoriteResponseDto(boolean isFavorited) {

        this.isFavorited = isFavorited;
    }
}
