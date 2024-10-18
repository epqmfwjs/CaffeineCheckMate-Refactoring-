package com.project.ReCCM.service.custom;

import com.project.ReCCM.domain.custom.CountRepository;
import com.project.ReCCM.domain.custom.Custom;
import com.project.ReCCM.domain.custom.CustomRepository;
import com.project.ReCCM.domain.custom.LikeCount;
import com.project.ReCCM.domain.member.Member;
import com.project.ReCCM.domain.member.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CountService {

    private final CountRepository countRepository;
    private final CustomRepository customRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public CountService(CountRepository countRepository, CustomRepository customRepository, MemberRepository memberRepository) {
        this.countRepository = countRepository;
        this.customRepository = customRepository;
        this.memberRepository = memberRepository;
    }

    // 공통된 멤버와 게시물 조회 로직을 별도 메서드로 추출
    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
    }

    private Custom getCustomById(Long postId) {
        return customRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));
    }

    @Transactional
    public void toggleLike(Long postId, Long memberId) {
        Member member = getMemberById(memberId);
        Custom custom = getCustomById(postId);

        // 좋아요가 이미 존재하는지 확인
        if (countRepository.existsByCustomAndMember(custom, member)) {
            // 좋아요 취소
            LikeCount likeCount = countRepository.findByMemberAndCustom(member, custom);
            countRepository.delete(likeCount);
            System.out.println("좋아요 취소");
        } else {
            // 좋아요 추가
            LikeCount newLike = new LikeCount();
            newLike.setCustom(custom);
            newLike.setMember(member);
            countRepository.save(newLike);
            System.out.println("좋아요 추가");
        }
    }

    public boolean checkLikeStatus(Long postId, Long memberId) {
        Member member = getMemberById(memberId);
        Custom custom = getCustomById(postId);
        return countRepository.existsByCustomAndMember(custom, member);
    }
}
