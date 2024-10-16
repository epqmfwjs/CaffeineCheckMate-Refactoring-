package com.project.ReCCM.domain.main;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CalculatorRepository extends JpaRepository<Calculator, Long> {

    Optional<Calculator> findByMemberIdAndCreatedDate(Long memberId, LocalDate createdDate);

    Optional<List<Calculator>> findByMemberId(Long memberId);

}
