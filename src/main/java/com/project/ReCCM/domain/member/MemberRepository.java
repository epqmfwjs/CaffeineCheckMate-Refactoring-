package com.project.ReCCM.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberId(String memberId);

    Optional<Object> findByMemberIdAndMemberEmail(String username, String email);

    List<Member> findAllByMemberNameAndMemberEmail(String name, String email);
}
