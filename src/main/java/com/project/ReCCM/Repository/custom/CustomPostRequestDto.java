package com.project.ReCCM.Repository.custom;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomPostRequestDto {

    private String customTitle;
    private String customContent;
    private MultipartFile[] imgReal;
    private Long memberPK;
    // 이넘 으로 관리
    private Brand brand;
    private Syrup syrup;
    private Whipped whipped;
    private Shot shot;
    private Milk milk;
    private CoffeeType coffeeType;

}
