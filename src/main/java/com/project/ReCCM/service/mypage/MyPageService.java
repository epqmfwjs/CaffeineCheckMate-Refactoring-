package com.project.ReCCM.service.mypage;


import com.project.ReCCM.Repository.mypage.MyPageFavoriteResponseDto;
import com.project.ReCCM.domain.member.Member;
import com.project.ReCCM.domain.member.MemberRepository;
import com.project.ReCCM.domain.product.Favorite;
import com.project.ReCCM.domain.product.FavoriteRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class MyPageService {


    @Autowired
    private final MemberRepository memberRepository;

    @Autowired
    private final FavoriteRepository favoriteRepository;

    public MyPageService(MemberRepository memberRepository, FavoriteRepository favoriteRepository) {
        this.memberRepository = memberRepository;
        this.favoriteRepository = favoriteRepository;
    }




    public List<MyPageFavoriteResponseDto> getFavoriteListByMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 존재하지 않습니다."));

        List<Favorite> favorites = favoriteRepository.findAllByMember(member);
        return favorites.stream()
                .map(favorite -> new MyPageFavoriteResponseDto(favorite.getProduct()))  // Product 정보를 DTO로 변환
                .collect(Collectors.toList());

    }
}
