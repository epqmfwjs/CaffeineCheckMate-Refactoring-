package com.project.ReCCM.Repository.member;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class MemberInfoDto {

    private String memberId;

    private String memberName;

    private String memberEmail;

    private int memberAge;

    private Double memberWeight;

    private String memberPhone;

    private String memberGender;

    private String imgReal;

    public MemberInfoDto(String memberId, String memberName, String memberEmail, int memberAge, double memberWeight, String memberPhone, String memberGender, String imgReal) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
        this.memberAge = memberAge;
        this.memberWeight = memberWeight;
        this.memberPhone = memberPhone;
        this.memberGender = memberGender;
        this.imgReal = imgReal;
    }
}
