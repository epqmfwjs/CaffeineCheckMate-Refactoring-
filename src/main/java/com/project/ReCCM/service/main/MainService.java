package com.project.ReCCM.service.main;


import com.project.ReCCM.Repository.main.CalculatorResponseDto;
import com.project.ReCCM.Repository.mypage.MyPageFavoriteResponseDto;
import com.project.ReCCM.Repository.mypage.MyPageLikeResponseDto;
import com.project.ReCCM.Repository.mypage.MyPageMemberResponseDto;
import com.project.ReCCM.domain.custom.CountRepository;
import com.project.ReCCM.domain.custom.LikeCount;
import com.project.ReCCM.domain.member.Member;
import com.project.ReCCM.domain.member.MemberRepository;
import com.project.ReCCM.domain.product.Favorite;
import com.project.ReCCM.domain.product.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class MainService {


    @Autowired
    private final MemberRepository memberRepository;

    @Autowired
    private final FavoriteRepository favoriteRepository;

    @Autowired
    private final CountRepository countRepository;

    public MainService(MemberRepository memberRepository, FavoriteRepository favoriteRepository, CountRepository countRepository) {
        this.memberRepository = memberRepository;
        this.favoriteRepository = favoriteRepository;
        this.countRepository = countRepository;
    }
    
    // 카페인 계산기
    public CalculatorResponseDto calculator(int caffeine, Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 존재하지 않습니다."));
        int maxCaffeine;
        int age = calculateAge(member.getMemberAge()) + 1;

        // 성인, 청소년, 임신여부 에 따른 카페인 적정량 값 지정
        if(age>20){ // 성인 카페인 적정량 지정
            maxCaffeine = 400;
        }else if(age<20){ // 청소년 카페인 적정량 지정
            maxCaffeine = (int) (member.getMemberWeight()*2.5);
        }else{// 임신조건 나중에 설정해야함
            maxCaffeine = 300;
        }

        // 퍼센트 계산
        double resultCaffeine = Math.round(((double) caffeine / maxCaffeine) * 100 * 100) / 100.0;

        CalculatorResponseDto calculatorResponseDto = new CalculatorResponseDto(
                member.getMemberId(), // memberId
                maxCaffeine,          // maxCaffeine
                caffeine,             // 섭취한 카페인
                age,                  // 나이
                resultCaffeine        // 계산된 퍼센트 값
        );
        return calculatorResponseDto;
    }

    // 나이를 계산하는 메서드
    public int calculateAge(LocalDate memberAge) {
        if (memberAge == null) {
            throw new IllegalArgumentException("생년월일 제대로 안들어옴");
        }
        LocalDate currentDate = LocalDate.now();
        return Period.between(memberAge, currentDate).getYears();
    }
}
