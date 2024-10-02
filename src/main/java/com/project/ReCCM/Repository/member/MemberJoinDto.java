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
public class MemberJoinDto {

    @NotEmpty(message = "아이디는 필수 항목입니다.")
    private String memberId;

    @NotEmpty(message = "비밀번호는 필수 항목입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String password;

    @NotEmpty(message = "이름은 필수 항목입니다.")
    private String memberName;

    @NotEmpty(message = "이메일은 필수 항목입니다.")
    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    private String memberEmail;

//    @NotEmpty(message = "생년월일은 필수 항목입니다.")
//    private LocalDate memberAge;

    @NotNull(message = "생년월일은 필수 입력 항목입니다.")
    @Past(message = "생년월일은 과거 날짜여야 합니다.")
    private LocalDate memberAge;

    @NotNull(message = "회원의 체중은 null이 될 수 없습니다.")
    private Double memberWeight;

    @NotEmpty(message = "전화번호는 필수 항목입니다.")
    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "전화번호는 000-0000-0000 형식이어야 합니다.")
    private String memberPhone;

    private String memberGender;

    private MultipartFile imgReal;

}
