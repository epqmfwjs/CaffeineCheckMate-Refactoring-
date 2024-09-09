package com.project.ReCCM.controller;

import com.project.ReCCM.domain.CoffeeList;
import com.project.ReCCM.domain.CoffeeListRepository;
import com.project.ReCCM.service.CoffeeListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api")
@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    private CoffeeListService coffeeListService;
    @Autowired
    private CoffeeListRepository coffeeListRepository;

    @GetMapping("/coffeeList")
    public List<CoffeeList> coffeeList() {
        System.out.println("커피리스트 조회 들어옴");

        List<CoffeeList> coffeeList = coffeeListRepository.findAll();

        // 리스트 생성 db 연결전 임시 데이터
//        List<String> coffeeList = new ArrayList<>();
//        coffeeList.add("Espresso - 3000원");
//        coffeeList.add("Latte - 4000원");
//        coffeeList.add("Cappuccino - 4500원");

        // 리스트 반환
        return coffeeList;
    }
}
