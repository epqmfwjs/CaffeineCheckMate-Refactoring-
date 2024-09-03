package com.project.ReCCM.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api")
@org.springframework.web.bind.annotation.RestController
public class RestController {

    @GetMapping("/coffeeList")
    public List<String> coffeeList() {
        System.out.println("커피리스트 조회 들어옴");

        // 리스트 생성
        List<String> coffeeList = new ArrayList<>();
        coffeeList.add("Espresso - 3000원");
        coffeeList.add("Latte - 4000원");
        coffeeList.add("Cappuccino - 4500원");

        // 리스트 반환
        return coffeeList;
    }
}
