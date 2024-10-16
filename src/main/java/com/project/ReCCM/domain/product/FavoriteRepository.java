package com.project.ReCCM.domain.product;

import com.project.ReCCM.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    // 즐겨찾기 추가 삭제 관련
    Optional<Favorite> findByMemberAndProduct(Member member, Product product);
    
    //  게시물의 관하여 특정 아이디의 즐겨찾기 현재 상태 확인
    boolean existsByMemberAndProduct(Member member, Product product);

    // mypage 에서 접근하는 특정 아이디의 즐겨찾기 리스트 조회
    List<Favorite> findAllByMember(Member member);

    List<Favorite> findByMemberId(Long memberId);
}
