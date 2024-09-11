package com.project.ReCCM.controller.custom;

import com.project.ReCCM.domain.custom.Custom;
import com.project.ReCCM.domain.custom.CustomRepository;
import com.project.ReCCM.domain.product.CoffeeList;
import com.project.ReCCM.domain.product.CoffeeListRepository;
import com.project.ReCCM.service.custom.CustomService;
import com.project.ReCCM.service.product.CoffeeListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api")
@RestController
public class CustomRestController {


    @Autowired
    private CustomService customService;
    @Autowired
    private CustomRepository customRepository;

    @GetMapping("/customList")
    public List<Custom> customList() {
        System.out.println("커스텀리스트 조회 들어옴");
        return customRepository.findAll();
    }

    @GetMapping("/searchCustom")
    public List<Custom> searchCustom(@RequestParam("keyword") String keyword){
        return customService.searchCoffee(keyword);
    }
}
