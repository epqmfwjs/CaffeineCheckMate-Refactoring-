package com.project.ReCCM.service.custom;

import com.project.ReCCM.domain.custom.Custom;
import com.project.ReCCM.domain.custom.CustomRepository;
import com.project.ReCCM.domain.product.CoffeeList;
import com.project.ReCCM.domain.product.CoffeeListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomService {
    @Autowired
    private CustomRepository customRepository;

    // 키워드를 이용한 커피 검색
    public List<Custom> searchCoffee(String keyword) {
        return customRepository.searchCoffee(keyword);
    }
}
