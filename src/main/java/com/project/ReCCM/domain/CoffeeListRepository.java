package com.project.ReCCM.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoffeeListRepository extends JpaRepository<CoffeeList, Long> {


}
