package com.project.ReCCM.domain.custom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomRepository extends JpaRepository<Custom, Long> {

    // 커피 이름 또는 브랜드 또는 종류에 따라 검색
    @Query("SELECT c FROM CoffeeList c WHERE LOWER(c.coffeeName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.coffeeBrand) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.coffeeType) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Custom> searchCoffee(@Param("keyword") String keyword);
}
