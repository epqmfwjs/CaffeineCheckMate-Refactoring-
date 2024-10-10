package com.project.ReCCM.service.custom;

import com.project.ReCCM.domain.custom.CountRepository;
import com.project.ReCCM.domain.custom.Custom;
import com.project.ReCCM.domain.custom.CustomRepository;
import com.project.ReCCM.domain.custom.LikeCount;
import com.project.ReCCM.domain.member.Member;
import com.project.ReCCM.domain.member.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CountService {
    @Autowired
    private final CountRepository countRepository;

    @Autowired
    private final CustomRepository customRepository;

    @Autowired
    private final MemberRepository memberRepository;

    public CountService(CountRepository countRepository, CustomRepository customRepository, MemberRepository memberRepository) {
        this.countRepository = countRepository;
        this.customRepository = customRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void toggleLike(Long postId, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
        Custom custom = customRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));

        // 좋아요가 이미 존재하는지 확인
        if (countRepository.existsByCustomAndMember(custom,member)) {
            // 좋아요를 이미 눌렀다면, 좋아요 취소 (삭제)
            LikeCount likeCount = countRepository.findByMemberAndCustom(member, custom);
            countRepository.delete(likeCount);
            System.out.println("좋아요 취소");
        } else {
            System.out.println("멤버객체" + member);
            System.out.println("커스텀객체" + custom);
            
            // 좋아요가 없으면 새로 추가
            LikeCount newLike = new LikeCount();
            newLike.setCustom(custom);
            newLike.setMember(member);
            countRepository.save(newLike);
            System.out.println("좋아요 추가");

            System.out.println("커스텀 업데이트");
            
        }
    }
    public boolean checkLikeStatus(Long postId, Long memberId) {
        Custom custom = customRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시물이 존재하지 않습니다."));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

        return countRepository.existsByCustomAndMember(custom, member);
    }
/*    public void likeNum(Long postId) {
        System.out.println("Like 서비스 들어옴");

        // 게시물 찾기
        Optional<Custom> optionalCustom = customRepository.findById(postId);
        if (!optionalCustom.isPresent()) {
            throw new IllegalArgumentException("게시물이 존재하지 않습니다."); // 게시물이 없을 경우 처리
        }

        Custom custom = optionalCustom.get();

        Optional<LikeCount> optionalLikeCount = countRepository.findById(postId);
        if (optionalLikeCount.isPresent()) {
            // 기존 LikeCount 업데이트
            LikeCount likeCount = optionalLikeCount.get();
            likeCount.setCount(likeCount.getCount() + 1); // 카운트 증가
            countRepository.save(likeCount); // 업데이트
        } else {
            // 새로운 LikeCount 생성
            LikeCount newLikeCount = new LikeCount();
            newLikeCount.setId(postId); // 게시물 ID 설정
            newLikeCount.setCount(1L); // 초기 카운트 설정
            newLikeCount.setCustom(custom); // Custom 설정
            countRepository.save(newLikeCount); // 새로운 카운트 저장
        }
    }

    public void favorites(Long postId) {
        Optional<Custom> optionalCustom = customRepository.findById(postId);
        if (!optionalCustom.isPresent()) {
            throw new IllegalArgumentException("게시물이 존재하지 않습니다."); // 게시물이 없을 경우 처리
        }

    }*/
}
