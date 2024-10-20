package com.project.ReCCM.Repository.member;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberJoinDto {

    @NotEmpty(message = "아이디는 필수 항목입니다.")
    private String memberId;

    @NotEmpty(message = "비밀번호는 필수 입력 항목입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()_+\\-=\\[\\]{};':\",./<>?])(?=.*[A-Za-z\\d~!@#$%^&*()_+\\-=\\[\\]{};':\",./<>?]).{8,}$",
            message = "비밀번호는 영문 대소문자, 숫자, 특수문자를 모두 포함해야 합니다.")
    private String password;        // 유저 PW : memberPW

    @NotEmpty(message = "비밀번호 확인은 필수 입력 항목입니다.")
    private String confirmPassword; // 유저 비밀번호 확인 : memberPW


    @NotEmpty(message = "이름은 필수 항목입니다.")
    private String memberName;

    @NotEmpty(message = "이메일은 필수 항목입니다.")
    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    private String email;

    @NotNull(message = "생년월일은 필수 입력 항목입니다.")
    @Past(message = "생년월일은 과거 날짜여야 합니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate memberAge;  // 생년월일을 LocalDate 타입으로 설정

    @NotNull(message = "회원의 체중은 null이 될 수 없습니다.")
    private Double memberWeight;

    @NotEmpty(message = "전화번호는 필수 항목입니다.")
    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "전화번호는 000-0000-0000 형식이어야 합니다.")
    private String memberPhone;
    
    private String address; // 카카오맵 주소
    
    private String detailAddress; // 상세주소

    private String memberGender;

    private MultipartFile imgReal;

}
