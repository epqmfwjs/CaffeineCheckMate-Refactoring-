package com.project.ReCCM.controller;

import com.project.ReCCM.domain.CoffeeList;
import com.project.ReCCM.domain.CoffeeListRepository;
import com.project.ReCCM.service.CoffeeListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

        // 리스트 반환
        return coffeeList;
    }

    @GetMapping("/searchCoffee")
    public List<CoffeeList> searchCoffee(@RequestParam("keyword") String keyword){
        return coffeeListService.searchCoffee(keyword);
    }
}
