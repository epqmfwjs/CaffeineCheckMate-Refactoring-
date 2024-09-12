package com.project.ReCCM.Repository.custom;

import jakarta.persistence.Column;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomPostRequestDto {

    private String customTitle;

    private String customContent;

    private MultipartFile[] imgReal;

    private String imgCopy; // 이미지 카피네임

}
