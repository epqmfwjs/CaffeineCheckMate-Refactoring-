package com.project.ReCCM.Repository.mypage;

import com.project.ReCCM.domain.member.Member;
import com.project.ReCCM.domain.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MyPageMemberResponseDto {

    private String memberId;
    private String memberName;
//    private String imgReal;

    public MyPageMemberResponseDto(String memberId, String memberName) {
        this.memberId = memberId;
        this.memberName = memberName;
    }
}
