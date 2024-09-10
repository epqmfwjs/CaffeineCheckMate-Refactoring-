package com.project.ReCCM.service;

import com.project.ReCCM.domain.CoffeeList;
import com.project.ReCCM.domain.CoffeeListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoffeeListService {
    @Autowired
    private CoffeeListRepository coffeeListRepository;

    // 키워드를 이용한 커피 검색
    public List<CoffeeList> searchCoffee(String keyword) {
        return coffeeListRepository.searchCoffee(keyword);
    }
}
