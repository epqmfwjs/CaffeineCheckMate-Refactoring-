package com.project.ReCCM.domain.custom;

import com.project.ReCCM.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CountRepository extends JpaRepository<LikeCount, Long> {
    // 특정 사용자와 게시글로 좋아요 여부 확인
    boolean existsByMemberAndCustom(@Param("member") Member member, @Param("custom") Custom custom);

    // 사용자와 게시글로 좋아요 레코드 찾기
    LikeCount findByMemberAndCustom(@Param("member") Member member, @Param("custom") Custom custom);


    boolean existsByCustomAndMember(Custom custom, Member member);
}
