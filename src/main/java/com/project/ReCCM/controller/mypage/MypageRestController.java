package com.project.ReCCM.controller.mypage;

import com.project.ReCCM.Repository.mypage.MyPageFavoriteResponseDto;
import com.project.ReCCM.Repository.mypage.MyPageLikeResponseDto;
import com.project.ReCCM.Repository.mypage.MyPageMemberResponseDto;
import com.project.ReCCM.domain.product.FavoriteRepository;
import com.project.ReCCM.service.mypage.MyPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api")
@RestController
public class MypageRestController {

    @Autowired
    private final FavoriteRepository favoriteRepository;

    @Autowired
    private final MyPageService myPageService;

    public MypageRestController(FavoriteRepository favoriteRepository, MyPageService myPageService) {
        this.favoriteRepository = favoriteRepository;
        this.myPageService = myPageService;
    }


    // 특정 멤버의 즐겨찾기 리스트 반환 main 과 mypage 사용
    @GetMapping("/favoriteList")
    public ResponseEntity<List<MyPageFavoriteResponseDto>> getFavoriteList(@RequestParam("memberId") Long memberId) {
        System.out.println("favoriteList 메소드 들어옴");
        List<MyPageFavoriteResponseDto> favoriteList = myPageService.getFavoriteListByMember(memberId);
        System.out.println("favoriteList 메소드 들어옴 : " + favoriteList.toString());
        return ResponseEntity.ok(favoriteList);
    }

    // 특정 멤버의 즐겨찾기 리스트 반환
    @GetMapping("/likeList")
    public ResponseEntity<List<MyPageLikeResponseDto>> getLikeList(@RequestParam("memberId") Long memberId) {
        System.out.println("likeList 메소드 들어옴");
        List<MyPageLikeResponseDto> likeList = myPageService.getLikeListByMember(memberId);
        System.out.println("favoriteList 메소드 들어옴 : " + likeList.toString());
        return ResponseEntity.ok(likeList);
    }
    
    // 마이페이지 멤버정보 반환
    @GetMapping("/memberInfo")
    public MyPageMemberResponseDto getMemberInfo(@RequestParam("memberId") Long memberId) {
        System.out.println("memberInfo 메소드 들어옴");
        return myPageService.getMemberInfoList(memberId);
    }
}
