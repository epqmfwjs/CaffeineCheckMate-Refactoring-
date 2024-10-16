package com.project.ReCCM.service.mypage;


import com.project.ReCCM.Repository.mypage.MyPageCaffeineResponseDto;
import com.project.ReCCM.Repository.mypage.MyPageFavoriteResponseDto;
import com.project.ReCCM.Repository.mypage.MyPageLikeResponseDto;
import com.project.ReCCM.Repository.mypage.MyPageMemberResponseDto;
import com.project.ReCCM.domain.custom.CountRepository;
import com.project.ReCCM.domain.custom.LikeCount;
import com.project.ReCCM.domain.main.Calculator;
import com.project.ReCCM.domain.main.CalculatorRepository;
import com.project.ReCCM.domain.member.Member;
import com.project.ReCCM.domain.member.MemberRepository;
import com.project.ReCCM.domain.product.Favorite;
import com.project.ReCCM.domain.product.FavoriteRepository;
import com.project.ReCCM.service.main.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class MyPageService {


    @Autowired
    private final MemberRepository memberRepository;

    @Autowired
    private final FavoriteRepository favoriteRepository;

    @Autowired
    private final CountRepository countRepository;
    @Autowired
    private final CalculatorRepository calculatorRepository;

    @Autowired
    private final MainService mainService;

    public MyPageService(MemberRepository memberRepository, FavoriteRepository favoriteRepository, CountRepository countRepository, CalculatorRepository calculatorRepository, MainService mainService) {
        this.memberRepository = memberRepository;
        this.favoriteRepository = favoriteRepository;
        this.countRepository = countRepository;
        this.calculatorRepository = calculatorRepository;
        this.mainService = mainService;
    }



    // 마이페이지에서 즐겨찾기 목록요청서비스
    public List<MyPageFavoriteResponseDto> getFavoriteListByMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 존재하지 않습니다."));

        List<Favorite> favorites = favoriteRepository.findAllByMember(member);
        return favorites.stream()
                .map(favorite -> new MyPageFavoriteResponseDto(favorite.getProduct()))  // Product 정보를 DTO로 변환
                .collect(Collectors.toList());
    }

    // 마이페이제에서 좋아요 리스트 요청서비스
    public List<MyPageLikeResponseDto> getLikeListByMember(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 존재하지 않습니다."));

        List<LikeCount> likeLists = countRepository.findAllByMember(member);
        return likeLists.stream()
                .map(Like -> new MyPageLikeResponseDto(Like.getCustom()))  // Product 정보를 DTO로 변환
                .collect(Collectors.toList());
    }

    // 마이페이지에서 멤버 정보 요청서비스
    public MyPageMemberResponseDto getMemberInfoList(Long memberId) {

        return memberRepository.findById(memberId)
                .map(member -> new MyPageMemberResponseDto(member.getMemberId(), member.getMemberName()))
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 존재하지 않습니다."));
    }

    // 달력 데이터 가져오기
    public List<MyPageCaffeineResponseDto> getCafffeineCalendar(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 존재하지 않습니다."));

        List<Calculator> calculators = calculatorRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("카페인 기록이 존재하지 않습니다."));

        return mainService.getCafffeineCalendar(memberId,calculators);
    }
}
